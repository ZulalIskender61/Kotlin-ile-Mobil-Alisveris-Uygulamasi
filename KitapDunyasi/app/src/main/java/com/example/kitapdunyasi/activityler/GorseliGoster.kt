package com.example.kitapdunyasi.activityler

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kitapdunyasi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class GorseliGoster : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_gorseli_goster)



        imageView = findViewById(R.id.gorsel)
        val text1 = findViewById<TextView>(R.id.kitabinAdiText)
        val text2 = findViewById<TextView>(R.id.kitabinFiyatiText)
        val text3 = findViewById<TextView>(R.id.kitabinYazariText)
        val text4 = findViewById<TextView>(R.id.kitabinSayfaSayisiText)
        val btnSepeteEkle = findViewById<Button>(R.id.btnSatinAl)

        val gorselUrl = intent.getStringExtra("imageUrl")
        val kitapAd = intent.getStringExtra("KitapAdi")
        val kitapFiyat = intent.getDoubleExtra("kitapFiyati",0.0)
        val kitapYazar = intent.getStringExtra("kitapYazari")
        val kitapSayfaSayisi = intent.getStringExtra("kitapSayfaSayisi")

        Picasso.get().load(gorselUrl).into(imageView)
        text1.text=("Kitabın Adı: ${kitapAd}")
        text3.text=("Yazar: ${kitapYazar}")
        text4.text=("Sayfa Sayısı: ${kitapSayfaSayisi}")
        text2.text=("Kitabın Fiyatı: ${kitapFiyat} TL")

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        btnSepeteEkle.setOnClickListener {
            //butona tıklandığında Firebase FireStore ye ekleme yapar
            val guncelKullanici = auth.currentUser?.email.toString()
            val db = FirebaseFirestore.getInstance()
            val collectionRef = db.collection("Sepet")
            /*
            val uuid = UUID.randomUUID()
            val imageName = "${uuid}.jpg"*/

            // Oluşturulan action
            val sepetAction = hashMapOf(
                "gorselUrl" to gorselUrl,
                "KitapAdi" to kitapAd,
                "kitapFiyati" to kitapFiyat
            )

            // Firestore'da Sepet koleksiyonuna action'ı ekleyin
            collectionRef.document(guncelKullanici)
                .collection("Ürünler")
                .add(sepetAction)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Action added with ID: $gorselUrl")
                    Toast.makeText(applicationContext, "Sepete eklendi!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding action", e)
                    Toast.makeText(applicationContext, "Hata oluştu, sepete eklenemedi!", Toast.LENGTH_SHORT).show()
                }
        }


    }

}