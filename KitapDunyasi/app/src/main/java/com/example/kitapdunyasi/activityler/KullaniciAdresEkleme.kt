package com.example.kitapdunyasi.activityler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.model.SepetPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class KullaniciAdresEkleme : AppCompatActivity() {
    private var listeAdres = ArrayList<SepetPost>()
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kullanici_adres_ekleme)
        auth = FirebaseAuth.getInstance() // auth nesnesini başlattım

        val btnAdresKaydet=findViewById<Button>(R.id.btnAdresKaydet)

        btnAdresKaydet.setOnClickListener(){

            val guncelKullanici = auth.currentUser?.email.toString()
            db = FirebaseFirestore.getInstance()

            val editText1=findViewById<EditText>(R.id.adresBasligi)
            val AdresBasligi=editText1.text.toString()
            val collectionRef = db.collection("Sepet").document(guncelKullanici)
                .collection("Adresler")


            collectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot != null) {

                        val editText2=findViewById<EditText>(R.id.adres)
                        val adres=editText2.text.toString()
                        val editText3=findViewById<EditText>(R.id.adresSehir)
                        val adresSehir=editText3.text.toString()
                        val editText4=findViewById<EditText>(R.id.adresTelefonNumarasi)
                        val adresTelefonNumarasi=editText4.text.toString()
                        val editText5=findViewById<EditText>(R.id.adresPostaKodu)
                        val adresPostaKodu=editText5.text.toString()

                        // Veriyi Firestore'a ekleme
                        val additionalData = hashMapOf(
                            "AdresBasligi" to AdresBasligi,
                            "Adres" to adres,
                            "SehirIlce" to adresSehir,
                            "TelefonNumarasi" to adresTelefonNumarasi,
                            "PostaKodu" to adresPostaKodu

                        )


                        collectionRef.document(AdresBasligi).set(additionalData)
                            .addOnSuccessListener {
                                // Başarılı bir şekilde eklendi
                                Toast.makeText(this, "Adres eklendi!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                // Hata durumunda
                                Toast.makeText(this,"Adres ekleme işlemi başarısız!",Toast.LENGTH_LONG).show()
                            }
                    }
        }


            val gelenVeri=intent.getStringExtra("key")
            if(gelenVeri=="profilAdresEkleme"){
                val intent1=Intent(applicationContext,KullaniciAdresleri::class.java)
                startActivity(intent1)
            }
            else if (gelenVeri=="sepetOnayAdresEkleme"){
                val intent2=Intent(applicationContext,SepetOnay::class.java)
                startActivity(intent2)
            }
            else{
                finish()
            }


            }

    }
}