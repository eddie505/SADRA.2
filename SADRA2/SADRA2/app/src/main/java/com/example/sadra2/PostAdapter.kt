package com.example.sadra2

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class PostAdapter(
    private val activity: Activity,
    private val dataset: List<Post>,
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(val layout: View) : RecyclerView.ViewHolder(layout) {
        val namePersonTextView: TextView = layout.findViewById(R.id.namePerson_tv)
        val postTextView: TextView = layout.findViewById(R.id.post_tv)
        val fechaTextView: TextView = layout.findViewById(R.id.fecha_tv)
        val shareButton: Button = layout.findViewById(R.id.share_btn)
        val likesCountTextView: TextView = layout.findViewById(R.id.likesCount_tv)
        val likeButton: Button = layout.findViewById(R.id.like_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.card_post, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = dataset[position]
        val likes = post.likes!!.toMutableList()
        var liked = likes.contains(auth.uid)

        holder.likesCountTextView.text = "${likes.size} likes"
        holder.namePersonTextView.text = post.userName
        holder.postTextView.text = post.post

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a")

        holder.fechaTextView.text = sdf.format(post.date)
        setColor(liked, holder.likeButton)

        holder.likeButton.setOnClickListener {
            liked = !liked
            setColor(liked, holder.likeButton)

            if (liked) likes.add(auth.uid!!)
            else likes.remove(auth.uid)

            val doc = db.collection("posts").document(post.uid!!)

            db.runTransaction {
                it.update(doc,"likes", likes)


                null
            }
        }


        holder.shareButton.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, post.post)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            activity.startActivity(shareIntent)
        }
    }
    private fun setColor(liked: Boolean, likeButton: Button){
        if (liked) likeButton.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent))
        else likeButton.setTextColor(Color.BLACK)
    }
}
