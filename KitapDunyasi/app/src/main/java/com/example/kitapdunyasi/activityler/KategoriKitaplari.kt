package com.example.kitapdunyasi.activityler

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.adapter.KategoriRecyclerViewAdapter
import com.example.kitapdunyasi.databinding.ActivityKategoriKitaplariBinding
import com.example.kitapdunyasi.fragmentler.AnaSayfa
import com.example.kitapdunyasi.model.KategoriPost
import com.google.firebase.firestore.FirebaseFirestore

class KategoriKitaplari : AppCompatActivity() {
    private lateinit var binding: ActivityKategoriKitaplariBinding
    private lateinit var recyclerViewAdapter: KategoriRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private var listeKategori = ArrayList<KategoriPost>()
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriKitaplariBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseFirestore.getInstance()

        recyclerView = binding.kategoriRecyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerViewAdapter = KategoriRecyclerViewAdapter(listeKategori)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.addItemDecoration(KategoriKitaplari.BottomSpacingDecoration(10))
        recyclerView.addItemDecoration(KategoriKitaplari.LeftSpacingDecoration(10))


        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            val TurkEdebiyati = extras.getBoolean("TurkEdebiyati", false)
            val arastirmaTarih = extras.getBoolean("ArastirmaTarih", false)
            val bilim = extras.getBoolean("Bilim", false)
            val edebiyat = extras.getBoolean("Edebiyat", false)
            val felsefe = extras.getBoolean("Felsefe", false)
            val siirKitaplari = extras.getBoolean("SiirKitaplari", false)

            var alan = ""
            if (TurkEdebiyati) alan = "Türk Edebiyatı"
            if (arastirmaTarih) alan = "Araştırma Tarih"
            if (bilim) alan = "Bilim"
            if (edebiyat) alan = "Edebiyat"
            if (felsefe) alan = "Felsefe"
            if (siirKitaplari) alan = "Şiir Kitapları"

            database.collection(alan).addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle the exception
                } else {
                    if (snapshot != null && !snapshot.isEmpty) {
                        val documents = snapshot.documents
                        listeKategori.clear()
                        for (document in documents) {
                            val kitabinAlani = document.getString("Alan Adı") ?: ""
                            val kitapAdi = document.getString("Kitabın adı") ?: ""
                            val gorselUrl = document.getString("imageUrl") ?: ""
                            val kitabinYazari = document.getString("Kitabın yazarı") ?: ""
                            val kitabinSayfaSayisi = document.getString("Kitabın sayfa sayısı") ?: ""
                            val kitabinFiyati = document.getDouble("Kitabın fiyatı") ?: 0.0

                            val alinanVeri = KategoriPost(
                                kitabinAlani,
                                gorselUrl,
                                kitapAdi,
                                kitabinYazari,
                                kitabinSayfaSayisi,
                                kitabinFiyati
                            )

                            listeKategori.add(alinanVeri)
                        }
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    class BottomSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = spacing
        }
    }
    class LeftSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.left = spacing
        }
    }
}
