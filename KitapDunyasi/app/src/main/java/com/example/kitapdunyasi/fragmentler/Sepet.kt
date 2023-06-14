package com.example.kitapdunyasi.fragmentler

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.activityler.SepetOnay
import com.example.kitapdunyasi.adapter.SepetAdapter
import com.example.kitapdunyasi.model.Post4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Sepet : Fragment() {

    private lateinit var recyclerViewAdapter: SepetAdapter
    private lateinit var recyclerView: RecyclerView
    private var listeSepet = ArrayList<Post4>()
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var toplam: Double = 0.0
    private lateinit var btnSiparisVer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // auth nesnesini başlat
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sepet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.SepetRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        btnSiparisVer = view.findViewById<Button>(R.id.btnSiparisVer)

        val guncelKullanici = auth.currentUser?.email.toString()
        db = FirebaseFirestore.getInstance()

        val collectionRef = db.collection("Sepet").document(guncelKullanici)
            .collection("Ürünler")

        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot != null) {
                    listeSepet.clear() // Önceki verileri temizleyin
                    val sepetToplamTextView = view.findViewById<TextView>(R.id.sepetToplam)

                    for (document in querySnapshot.documents) {
                        val gorselUrl = document.getString("gorselUrl").toString()
                        val kitapAd = document.getString("KitapAdi").toString()
                        val kitapFiyat = document.getDouble("kitapFiyati")
                        val parsedKitapFiyat: Double = kitapFiyat ?: 0.0
                        val postId = document.id // Dökümanın ID'sini alın

                        toplam += parsedKitapFiyat

                        val post = Post4(gorselUrl, kitapAd, parsedKitapFiyat, postId)
                        listeSepet.add(post)
                    }

                    sepetToplamTextView.text = "Toplam: $toplam" // Toplam değeri sepetToplam TextView'e yazdırılıyor

                    if (listeSepet.isNotEmpty()) {
                        recyclerViewAdapter = SepetAdapter(listeSepet) { position ->
                            deleteItem(position)
                        }
                        recyclerView.adapter = recyclerViewAdapter
                        btnSiparisVer.isEnabled = true // Veriler varsa butonu aktifleştir
                    } else {
                        btnSiparisVer.isEnabled = false // Veri yoksa butonu devre dışı bırak
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.d(ContentValues.TAG, "Error getting documents: ", e)
            }

        btnSiparisVer.setOnClickListener {
            val intent = Intent(context, SepetOnay::class.java)
            intent.putExtra("toplamFiyat", toplam) // toplam fiyatı intent'e ekle
            startActivity(intent)
        }
    }

    private fun deleteItem(position: Int) {
        val guncelKullanici = auth.currentUser?.email.toString()
        val document = db.collection("Sepet").document(guncelKullanici)
            .collection("Ürünler").document(listeSepet[position].postId)

        val deletedItemPrice: Double = listeSepet[position].kitapFiyat
        toplam -= deletedItemPrice // Çıkarılan öğenin fiyatını toplamdan çıkar

        document.delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Sepetten çıkarıldı", Toast.LENGTH_SHORT).show()
                listeSepet.removeAt(position)
                recyclerViewAdapter.notifyItemRemoved(position)
                // Fiyatı güncelleyerek textView'e yansıt
                val sepetToplamTextView = view?.findViewById<TextView>(R.id.sepetToplam)
                sepetToplamTextView?.text = "Toplam: $toplam"

                if (listeSepet.isEmpty()) {
                    btnSiparisVer.isEnabled = false // Sepet boşsa butonu devre dışı bırak
                }
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error deleting document", e)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Gerekli temizleme işlemlerini burada yapabilirsiniz
        recyclerView.adapter = null // RecyclerView adapter'ını null yaparak referansları temizle
        listeSepet.clear() // ArrayList'i temizle
    }
}
