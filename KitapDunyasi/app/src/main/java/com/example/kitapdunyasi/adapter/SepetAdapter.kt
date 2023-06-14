package com.example.kitapdunyasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.model.Post4
import com.squareup.picasso.Picasso

class SepetAdapter(private val postList: ArrayList<Post4>, private val onDeleteClickListener: (position: Int) -> Unit) :
    RecyclerView.Adapter<SepetAdapter.PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_sepet, parent, false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val currentPost = postList[position]
        holder.bind(currentPost, position)
    }

    inner class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val kitabinAdiTextView: TextView =
            itemView.findViewById(R.id.recycler_sepet_kitabinAdi)
        private val kitabinFiyatiTextView: TextView =
            itemView.findViewById(R.id.recycler_sepet_kitabinFiyati)
        private val imageView: ImageView =
            itemView.findViewById(R.id.recycler_sepet_imageView)
        private val imgDelete: ImageView =
            itemView.findViewById(R.id.recycler_sepet_trash)

        init {
            imgDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener(position)
                }
            }
        }
        fun bind(post: Post4, position: Int){
            kitabinAdiTextView.text=post.kitapAd
            kitabinFiyatiTextView.text=post.kitapFiyat.toString()
            if (post.gorselUrl.isNotEmpty()) {
                Picasso.get().load(post.gorselUrl).into(imageView)
            } else {
                imageView.setImageResource(R.drawable.gorsel)
            }
        }

    }
}
