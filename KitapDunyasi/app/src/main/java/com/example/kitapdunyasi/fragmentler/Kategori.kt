package com.example.kitapdunyasi.fragmentler

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.kitapdunyasi.activityler.KategoriKitaplari
import com.example.kitapdunyasi.R
import com.google.android.material.navigation.NavigationView


class Kategori : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_kategori, container, false)
        val navigationView = rootView.findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.TurkEdebiyati -> {
                    // TurkEdebiyati öğesine tıklandığında yapılacak işlemler
                    val intent=Intent(context as Activity, KategoriKitaplari::class.java)
                    intent.putExtra("TurkEdebiyati",true)
                    startActivity(intent)
                    true
                }
                R.id.ArastirmaTarih -> {
                    // ArastirmaTarih öğesine tıklandığında yapılacak işlemler
                    val intent=Intent(context as Activity, KategoriKitaplari::class.java)
                    intent.putExtra("ArastirmaTarih",true)
                    startActivity(intent)
                    true
                }
                R.id.Bilim -> {
                    // Bilim öğesine tıklandığında yapılacak işlemler
                    val intent=Intent(context as Activity, KategoriKitaplari::class.java)
                    intent.putExtra("Bilim",true)
                    startActivity(intent)
                    true
                }
                R.id.Edebiyat -> {
                    // Edebiyat öğesine tıklandığında yapılacak işlemler
                    val intent=Intent(context as Activity, KategoriKitaplari::class.java)
                    intent.putExtra("Edebiyat",true)
                    startActivity(intent)
                    true
                }
                R.id.Felsefe -> {
                    // Felsefe öğesine tıklandığında yapılacak işlemler
                    val intent=Intent(context as Activity, KategoriKitaplari::class.java)
                    intent.putExtra("Felsefe",true)
                    startActivity(intent)
                    true
                }
                R.id.SiirKitaplari -> {
                    // SiirKitaplari öğesine tıklandığında yapılacak işlemler
                    val intent=Intent(context as Activity, KategoriKitaplari::class.java)
                    intent.putExtra("SiirKitaplari",true)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Inflate the layout for this fragment
        return rootView
    }


}