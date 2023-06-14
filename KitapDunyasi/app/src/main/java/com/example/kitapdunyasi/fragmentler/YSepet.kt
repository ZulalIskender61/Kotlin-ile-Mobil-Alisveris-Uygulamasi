package com.example.kitapdunyasi.fragmentler

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.activityler.YoneticiKargoyaVer
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.adapter.YSiparislerAdapter
import com.example.kitapdunyasi.model.YSiparislerPost
import com.google.firebase.firestore.FirebaseFirestore

class YSepet : Fragment(), YSiparislerAdapter.OnKargoyaVerClickListener {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerViewAdapter: YSiparislerAdapter
    private lateinit var recyclerView: RecyclerView
    private var listeSepet = ArrayList<YSiparislerPost>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_y_sepet, container, false)

        recyclerView = view.findViewById(R.id.ysepet_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerViewAdapter = YSiparislerAdapter(listeSepet)
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.setOnKargoyaVerClickListener(this)

        val koleksiyonReferans = db.collection("Siparis Gecmisi")
        koleksiyonReferans.get()
            .addOnSuccessListener { querySnapshot ->
                val eklenenVeriler = ArrayList<String>()
                for (document in querySnapshot.documents) {
                    val kargoBilgisi = document.getString("kargoBilgisi").toString()
                    if (kargoBilgisi == "Kargoya verildi") {
                        continue
                    }
                    val adresBasligi = document.getString("adresBasligi").toString()
                    val gorselUrl = document.getString("gorselUrl").toString()
                    val kitapAd = document.getString("kitapAdi").toString()
                    val telefonNumarasi=document.getString("telefonNumarasi").toString()
                    val kitapFiyat = document.getDouble("kitapFiyati")
                    val parsedKitabinFiyati: Double = kitapFiyat ?: 0.0
                    val kullanici = document.getString("kullanici").toString()
                    val adres = document.getString("adres").toString()
                    val tarih = document.getString("tarih").toString()
                    val toplam = document.getDouble("toplam")
                    val parsedToplam: Double = toplam ?: 0.0
                    val postId = document.id

                    val post = YSiparislerPost(
                        kullanici,
                        gorselUrl,
                        kitapAd,
                        parsedKitabinFiyati,
                        adresBasligi,
                        adres,
                        tarih,
                        telefonNumarasi,
                        parsedToplam,
                        kargoBilgisi,
                        postId
                    )
                    val veriKey = tarih + kullanici
                    if (!eklenenVeriler.contains(veriKey)) {
                        listeSepet.add(post)
                        eklenenVeriler.add(veriKey)
                    }
                }
                recyclerViewAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("YSepet", "Error getting documents: ", exception)
                Toast.makeText(activity, "Veriler alınırken bir hata oluştu.", Toast.LENGTH_SHORT)
                    .show()
            }
        return view
    }
    override fun onKargoyaVerClick(post: YSiparislerPost) {
        val intent = Intent(activity, YoneticiKargoyaVer::class.java)
        intent.putExtra("kargoyaVerPost", post)
        startActivity(intent)
    }
}
