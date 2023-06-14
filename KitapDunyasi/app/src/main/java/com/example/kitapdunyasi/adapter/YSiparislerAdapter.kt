package com.example.kitapdunyasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.model.YSiparislerPost


class YSiparislerAdapter(val postList: ArrayList<YSiparislerPost>) : RecyclerView.Adapter<YSiparislerAdapter.PostHolder>() {

    private var onKargoyaVerClickListener: OnKargoyaVerClickListener? = null

    class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val kullaniciEmailiTextView: TextView = itemView.findViewById(R.id.gs_kullaniciEmaili)
        val toplamFiyat:TextView = itemView.findViewById(R.id.recycler_gs_toplamFiyat)
        val tarih: TextView = itemView.findViewById(R.id.recycler_gs_tarih)
        val kargoyaVerButton: Button = itemView.findViewById(R.id.kargoyaVer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_gelen_siparisler, parent, false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val currentPost = postList[position]

        holder.kullaniciEmailiTextView.text ="Kullanıcı: "+currentPost.kullanici
        holder.toplamFiyat.text ="Toplam: "+currentPost.toplam.toString()
        holder.tarih.text="Tarih: "+currentPost.tarih


        holder.kargoyaVerButton.setOnClickListener {
            onKargoyaVerClickListener?.onKargoyaVerClick(currentPost)
        }
    }

    fun setOnKargoyaVerClickListener(listener: OnKargoyaVerClickListener) {
        onKargoyaVerClickListener = listener
    }

    interface OnKargoyaVerClickListener {
        fun onKargoyaVerClick(post: YSiparislerPost)

    }
}
