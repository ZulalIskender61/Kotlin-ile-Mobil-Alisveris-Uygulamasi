package com.example.kitapdunyasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.model.OnaylananKullaniciSiparisleriPost

class OnaylananKullaniciSiparisiAdapter (val postList: ArrayList<OnaylananKullaniciSiparisleriPost>) :
    RecyclerView.Adapter<OnaylananKullaniciSiparisiAdapter.PostHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
    class PostHolder(itemView: View, private val postList: List<OnaylananKullaniciSiparisleriPost>) :
        RecyclerView.ViewHolder(itemView) {
       private val recyclerButton=itemView.findViewById<Button>(R.id.recycler_btnTumSiparislerimDetaylar)

        fun bind(item: OnaylananKullaniciSiparisleriPost, listener: OnItemClickListener?) {
            recyclerButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnaylananKullaniciSiparisiAdapter.PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_siparislerim, parent, false)
        return OnaylananKullaniciSiparisiAdapter.PostHolder(view, postList)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val currentPost = postList[position]
        val tarihBilgisi = holder.itemView.findViewById<TextView>(R.id.recycler_tumSiparislerimTarih)
        val toplamBilgisi = holder.itemView.findViewById<TextView>(R.id.recycler_tumSiparislerimToplam)
        val kargoBilgisi=holder.itemView.findViewById<TextView>(R.id.recycler_tumSiparislerimKargoBilgisi)

        tarihBilgisi?.text = "Tarih: "+currentPost.Tarih
        toplamBilgisi?.text = "Toplam: "+currentPost.Toplam.toString()
        kargoBilgisi.text="Kargo Bilgisi: "+currentPost.kargoBilgisi
        holder.bind(currentPost, onItemClickListener)

    }

}