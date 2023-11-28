package com.example.sadra2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class CreatePostActivity : AppCompatActivity() {

    private lateinit var publishButton: Button
    private lateinit var postEditText: EditText
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        auth = FirebaseAuth.getInstance()
        publishButton = findViewById(R.id.publish_btn)
        postEditText = findViewById(R.id.post_et)

        publishButton.setOnClickListener {
            val postString = postEditText.text.toString()
            val date = Date()
            val userName = auth.currentUser!!.displayName

            val post = Post(postString, date, userName)

            db.collection("posts").add(post)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener {
                    Utils.showError(this, it.message.toString())
                }
        }
    }
}
