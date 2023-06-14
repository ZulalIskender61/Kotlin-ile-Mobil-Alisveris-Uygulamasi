package com.example.kitapdunyasi.activityler

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.adapter.AdreslerAdapter
import com.example.kitapdunyasi.fragmentler.AnaSayfa
import com.example.kitapdunyasi.model.AdresPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class KullaniciAdresleri : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: AdreslerAdapter
    private var listeAdresler = ArrayList<AdresPost>()
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val ADRES_DUZENLEME_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kullanici_adresleri)


        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerViewAdreslerim)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerViewAdapter = AdreslerAdapter(listeAdresler)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.addItemDecoration(KullaniciAdresleri.BottomSpacingDecoration(10))


        recyclerViewAdapter.setOnItemClickListener(object : AdreslerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, item: AdresPost) {
                val intent = Intent(applicationContext, AdresDuzenleme::class.java)
                intent.putExtra("adresBasligi", item.AdresBasligi)
                intent.putExtra("adres", item.Adres)
                intent.putExtra("sehirIlce", item.SehirIlce)
                intent.putExtra("postaKodu", item.PostaKodu)
                intent.putExtra("telefonNumarasi", item.TelefonNumarasi)
                startActivityForResult(intent, ADRES_DUZENLEME_REQUEST_CODE)
            }
        })

        val guncelKullanici = auth.currentUser?.email.toString()
        val adreslerimRef = db.collection("Sepet").document(guncelKullanici).collection("Adresler")

        adreslerimRef.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val AdresBasligi = document.getString("AdresBasligi").toString()
                val Adres = document.getString("Adres").toString()
                val SehirIlce = document.getString("SehirIlce").toString()
                val PostaKodu = document.getString("PostaKodu").toString()
                val TelefonNumarasi = document.getString("TelefonNumarasi").toString()
                val adres = AdresPost(AdresBasligi, Adres, SehirIlce, PostaKodu, TelefonNumarasi)
                listeAdresler.add(adres)
            }
            recyclerViewAdapter.notifyDataSetChanged()
        }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents", exception)
            }
        val btnAdresEkle=findViewById<Button>(R.id.btnYeniAdresEkle)
        btnAdresEkle.setOnClickListener(){
            val AdresEklemeIntent=Intent(applicationContext,KullaniciAdresEkleme::class.java)
            intent.putExtra("key","profilAdresEkleme")
            startActivity(AdresEklemeIntent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADRES_DUZENLEME_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val guncellenenVeriler = data?.getBooleanExtra("guncellenenVeriler", false)
                if (guncellenenVeriler == true) {
                    // Adreslerin güncellendiği işlemler yapılabilir
                    recreate()

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
}
