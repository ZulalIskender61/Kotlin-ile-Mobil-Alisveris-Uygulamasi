package com.example.kitapdunyasi.activityler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.adapter.YoneticiKArgoyaVerAdapter
import com.example.kitapdunyasi.model.ItemModel
import com.example.kitapdunyasi.model.YSiparislerPost
import com.google.firebase.firestore.FirebaseFirestore

class YoneticiKargoyaVer : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: YoneticiKArgoyaVerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yonetici_kargoya_ver)
        db = FirebaseFirestore.getInstance()
        val post = intent.getSerializableExtra("kargoyaVerPost") as YSiparislerPost?
        if (post != null) {
            val ykv_kullanici: TextView = findViewById(R.id.ykv_kullanici)
            val ykv_adresBasligi: TextView = findViewById(R.id.ykv_adresBasligi)
            val ykv_adres: TextView = findViewById(R.id.ykv_adres)
            val ykv_TelefonNumarasi: TextView = findViewById(R.id.ykv_telefonNumarasi)
            ykv_kullanici.text = post.kullanici
            ykv_adresBasligi.text = post.AdresBasligi
            ykv_adres.text = post.Adres
            ykv_TelefonNumarasi.text = post.TelefonNumarasi

            recyclerView = findViewById(R.id.ykv_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = YoneticiKArgoyaVerAdapter()
            recyclerView.adapter = adapter

            val koleksiyonReferans = db.collection("Siparis Gecmisi")
            koleksiyonReferans.get().addOnSuccessListener { querySnapshot ->
                val itemList = mutableListOf<ItemModel>()
                for (document in querySnapshot.documents) {
                    val gorselUrl = document.getString("gorselUrl").toString()
                    val kitabinAdi = document.getString("kitapAdi").toString()
                    val kitabinFiyati = document.getDouble("kitapFiyati") ?: 0.0
                    val kullanici = document.getString("kullanici").toString()
                    val adres = document.getString("adres").toString()
                    val tarih = document.getString("tarih").toString()
                    val toplam = document.getDouble("toplam")
                    val parsedToplam: Double = toplam ?: 0.0

                    if (tarih == post.tarih && kullanici == post.kullanici && adres == post.Adres && parsedToplam == post.toplam) {
                        val item = ItemModel(gorselUrl, kitabinAdi, kitabinFiyati)
                        itemList.add(item)
                    }
                }
                adapter.setItemList(itemList)
                adapter.notifyDataSetChanged()
            }

            val btnYkvOnay = findViewById<Button>(R.id.btn_ykv_onayla)
            btnYkvOnay.setOnClickListener {
                koleksiyonReferans.get().addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val tarih = document.getString("tarih").toString()
                        val kullanici = document.getString("kullanici").toString()
                        val adres = document.getString("adres").toString()
                        val parsedToplam = document.getDouble("toplam") ?: 0.0

                        if (tarih == post.tarih && kullanici == post.kullanici && adres == post.Adres && parsedToplam == post.toplam) {
                            val kargoBilgisi = "Kargoya verildi" // Güncel kargo bilgisi değeri
                            document.reference.update("kargoBilgisi", kargoBilgisi)
                                .addOnSuccessListener {
                                    // Güncelleme başarılı
                                    Toast.makeText(this, "Ürün kargoya verildi", Toast.LENGTH_SHORT).show()
                                    val veri="Kargoya Verildi"
                                    val intent=Intent(applicationContext,MainActivity::class.java)
                                    intent.putExtra("anahtar",veri)
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    // Güncelleme başarısız
                                }
                        }
                    }
                }
            }
        }
    }
}
