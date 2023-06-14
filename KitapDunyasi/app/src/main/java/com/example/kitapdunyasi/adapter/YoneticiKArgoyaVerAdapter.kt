package com.example.kitapdunyasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.model.ItemModel
import com.squareup.picasso.Picasso

class YoneticiKArgoyaVerAdapter : RecyclerView.Adapter<YoneticiKArgoyaVerAdapter.ViewHolder>() {
    private val itemList = mutableListOf<ItemModel>()

    fun setItemList(items: List<ItemModel>) {
        itemList.clear()
        itemList.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_ykv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.recycler_ykv_imageView)
        private val kitabinAdiTextView: TextView = itemView.findViewById(R.id.recycler_ykv_kitabinAdi)
        private val kitabinFiyatiTextView: TextView = itemView.findViewById(R.id.recycler_ykv_kitabinFiyati)

        fun bind(item: ItemModel) {
            kitabinAdiTextView.text = item.kitabinAdi
            kitabinFiyatiTextView.text = item.kitabinFiyati.toString()

            // Resimleri yüklemek için bir resim yükleme kütüphanesi kullanabilirsiniz
            // Örneğin: Picasso, Glide, Coil, vb.
            // İlgili görüntüyü yüklemek için:
             Picasso.get().load(item.gorselUrl).into(imageView)
        }
    }
}


