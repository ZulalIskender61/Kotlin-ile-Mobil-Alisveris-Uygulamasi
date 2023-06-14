package com.example.kitapdunyasi.activityler

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.adapter.OnaylananKullaniciSiparisiAdapter
import com.example.kitapdunyasi.databinding.ActivityKullaniciSiparisVerisiBinding
import com.example.kitapdunyasi.model.OnaylananKullaniciSiparisleriPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class KullaniciSiparisVerisi : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: OnaylananKullaniciSiparisiAdapter
    private lateinit var binding: ActivityKullaniciSiparisVerisiBinding
    private var listeSiparisler = ArrayList<OnaylananKullaniciSiparisleriPost>()
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kullanici_siparis_verisi)
        binding = ActivityKullaniciSiparisVerisiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = binding.recyclerViewOnaylananSiparisler
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerViewAdapter = OnaylananKullaniciSiparisiAdapter(listeSiparisler)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.addItemDecoration(KullaniciSiparisVerisi.BottomSpacingDecoration(10))

        recyclerViewAdapter.setOnItemClickListener(object : OnaylananKullaniciSiparisiAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickedItem = listeSiparisler[position]
                showAlertDialog(clickedItem)
            }
        })

        val guncelKullanici = auth.currentUser?.email.toString()
        val siparisGecmisiRef = db.collection("Siparis Gecmisi")
        siparisGecmisiRef.get().addOnSuccessListener { querySnapshot ->
            val eklenenVeriler = ArrayList<String>()
            for (document in querySnapshot.documents) {
                val kullanici = document.getString("kullanici").toString()
                if (kullanici == guncelKullanici) {
                    val tarih = document.getString("tarih").toString()
                    val adresBasligi = document.getString("adresBasligi").toString()
                    val kargoBilgisi = document.getString("kargoBilgisi").toString()
                    val adres=document.getString("adres").toString()
                    val telefonNumarasi=document.getString("telefonNumarasi").toString()
                    val toplam = document.getDouble("toplam")
                    val parsedToplam: Double = toplam ?: 0.0
                    val alinanVeri = OnaylananKullaniciSiparisleriPost(
                        guncelKullanici,
                        tarih,
                        adresBasligi,
                        kargoBilgisi,
                        adres,
                        telefonNumarasi,
                        parsedToplam
                    )
                    val veriKey = tarih + kullanici
                    if (!eklenenVeriler.contains(veriKey)) {
                        listeSiparisler.add(alinanVeri)
                        eklenenVeriler.add(veriKey)
                    }
                }
            }
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }
    private fun showAlertDialog(clickedItem: OnaylananKullaniciSiparisleriPost) {
        val builder = AlertDialog.Builder(this)


        builder.setTitle("Detaylar")
            .setMessage(clickedItem.AdresBasligi +"\n"+ clickedItem.Adres +"\n"+ clickedItem.TelefonNumarasi)
            .setPositiveButton("Tamam") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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

