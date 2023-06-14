package com.example.kitapdunyasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.model.AdresPost

class AdreslerAdapter(val postList: ArrayList<AdresPost>) : RecyclerView.Adapter<AdreslerAdapter.PostHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: AdresPost)
    }


    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class PostHolder(itemView : View, private val postList: List<AdresPost>) : RecyclerView.ViewHolder(itemView)  {
        private val recyclerButton=itemView.findViewById<Button>(R.id.recycler_btn_adres_düzenle)
        fun bind(item: AdresPost, listener: AdreslerAdapter.OnItemClickListener?) {
            recyclerButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(position, item)
                }
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdreslerAdapter.PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_adres, parent, false)
        return AdreslerAdapter.PostHolder(view, postList)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val currentPost = postList[position]
        val kullaniciAdresBasligiTextView = holder.itemView.findViewById<TextView>(R.id.recycler_adres_basligi)
        val kullaniciAdresTextView = holder.itemView.findViewById<TextView>(R.id.recycler_adres_adres)
        val kullaniciAdresSehirIlceTextView = holder.itemView.findViewById<TextView>(R.id.recycler_adres_sehir_ilce)
        val kullaniciAdresPostaKoduTextView = holder.itemView.findViewById<TextView>(R.id.recycler_adres_posta_kodu)
        val kullaniciAdresTelefonNumarasiTextView = holder.itemView.findViewById<TextView>(R.id.recycler_adres_telefon_numarasi)

        kullaniciAdresBasligiTextView.text=currentPost.AdresBasligi
        kullaniciAdresTextView.text=currentPost.Adres
        kullaniciAdresSehirIlceTextView.text=currentPost.SehirIlce
        kullaniciAdresPostaKoduTextView.text=currentPost.PostaKodu
        kullaniciAdresTelefonNumarasiTextView.text=currentPost.TelefonNumarasi

        holder.bind(currentPost, onItemClickListener)


    }
}