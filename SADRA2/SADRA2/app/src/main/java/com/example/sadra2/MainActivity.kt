package com.example.sadra2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        fab = findViewById(R.id.fab)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db.collection("posts").addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val posts = value?.toObjects(Post::class.java) ?: emptyList()

            posts.forEachIndexed { index, post ->
                post.uid = value?.documents?.get(index)?.id
            }

            recyclerView.adapter = PostAdapter(this@MainActivity, posts, auth)
        }

        fab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_item -> {
                auth.signOut()
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
