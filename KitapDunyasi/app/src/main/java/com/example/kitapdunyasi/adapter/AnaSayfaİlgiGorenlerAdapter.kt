package com.example.kitapdunyasi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.activityler.GorseliGoster
import com.example.kitapdunyasi.model.Post2
import com.squareup.picasso.Picasso

class AnaSayfaİlgiGorenlerAdapter(val postList: ArrayList<Post2>) : RecyclerView.Adapter<AnaSayfaİlgiGorenlerAdapter.PostHolder>()  {
    class PostHolder(itemView : View, private val postList: List<Post2>) : RecyclerView.ViewHolder(itemView) {
        private val item: ImageView = itemView.findViewById(R.id.recycler_row_imageView)

        //Kitabın bilgilerini alır ve kitabın bulunduğu alana tıklandığında bilgilerini GorseliGoster adlı activiteye gönderir.
        init {
            item.setOnClickListener {
                val context = itemView.context
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentPost = postList[position]
                    val intent = Intent(context, GorseliGoster::class.java)
                    intent.putExtra("imageUrl", currentPost.imageUrl)
                    intent.putExtra("KitapAdi",currentPost.KitapAdi)
                    intent.putExtra("kitapFiyati",currentPost.kitapFiyati)
                    intent.putExtra("kitapYazari",currentPost.kitapYazari)
                    intent.putExtra("kitapSayfaSayisi",currentPost.kitapSayfaSayisi)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)
        return PostHolder(view, postList)
    }

    override fun getItemCount(): Int {
        return postList.size
    }
    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        val currentPost = postList[position]
        val kitabinAdiTextView = holder.itemView.findViewById<TextView>(R.id.recycler_row_kitabinAdi)
        val kitabinFiyatiTextView = holder.itemView.findViewById<TextView>(R.id.recycler_row_kitabinFiyati)
        val kitabinYazariTextView = holder.itemView.findViewById<TextView>(R.id.recycler_row_kitabinYazari)
        val kitabinSayfaSayisiTextView = holder.itemView.findViewById<TextView>(R.id.recycler_row_kitabinSayfaSayisi)
        val imageView = holder.itemView.findViewById<ImageView>(R.id.recycler_row_imageView)

        kitabinAdiTextView.text = currentPost.KitapAdi
        kitabinFiyatiTextView.text = currentPost.kitapFiyati.toString()
        kitabinYazariTextView.text = currentPost.kitapYazari
        kitabinSayfaSayisiTextView.text = currentPost.kitapSayfaSayisi

        if (currentPost.imageUrl.isNotEmpty()) {
            Picasso.get().load(currentPost.imageUrl).into(imageView)
        } else {
            // Boş URL durumunda yapılacak işlemi buraya ekleyebilirsiniz
            // Örneğin, varsayılan bir resim gösterebilirsiniz:
            imageView.setImageResource(R.drawable.gorsel)
        }

    }
}