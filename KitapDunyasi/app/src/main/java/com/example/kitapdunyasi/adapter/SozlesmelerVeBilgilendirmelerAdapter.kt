package com.example.kitapdunyasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.model.AdresPost
import com.example.kitapdunyasi.model.SozlesmelerVeBilgilendirmelerPost

class SozlesmelerVeBilgilendirmelerAdapter(val postList: ArrayList<SozlesmelerVeBilgilendirmelerPost>) :
    RecyclerView.Adapter<SozlesmelerVeBilgilendirmelerAdapter.PostHolder>() {
    class PostHolder(itemView : View, private val postList: List<SozlesmelerVeBilgilendirmelerPost>) : RecyclerView.ViewHolder(itemView)  {



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SozlesmelerVeBilgilendirmelerAdapter.PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_adres, parent, false)
        return SozlesmelerVeBilgilendirmelerAdapter.PostHolder(view, postList)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: SozlesmelerVeBilgilendirmelerAdapter.PostHolder, position: Int) {
        val currentPost = postList[position]

        val CaymaHakki = holder.itemView.findViewById<TextView>(R.id.scrollViewCaymaHakkiText)
        val OnBilgilendirmeKosullari = holder.itemView.findViewById<TextView>(R.id.scrollViewOnBilgilendirmeText)
        val MesafeliSatisSozlesmesi = holder.itemView.findViewById<TextView>(R.id.scrollViewMesafeliSatisSozlesmesiText)

        CaymaHakki.text=currentPost.CaymaHakki
        OnBilgilendirmeKosullari.text=currentPost.OnBilgilendirmeKosullari
        MesafeliSatisSozlesmesi.text=currentPost.MesafeliSatisSozlesmesi


    }
}