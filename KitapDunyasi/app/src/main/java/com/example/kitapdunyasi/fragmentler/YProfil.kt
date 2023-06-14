package com.example.kitapdunyasi.fragmentler

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.kitapdunyasi.activityler.FotografEkle
import com.example.kitapdunyasi.activityler.MainActivity
import com.example.kitapdunyasi.activityler.SozlesmelerBilgilendirmelerEkle
import com.example.kitapdunyasi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class YProfil : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_y_profil, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cikisButton=view.findViewById<Button>(R.id.y_cikisYap)
        val GorselOgeEkle=view.findViewById<Button>(R.id.GorselOgeleriEkle)
        val SozlesmelerVeBilgilendirmeler=view.findViewById<Button>(R.id.y_sozlesmeler_bilgilendirmeler)
        textView1 = view.findViewById(R.id.KullanicininAdiSoyadi)
        textView2 = view.findViewById(R.id.kullanicininEmaili)
        database= FirebaseFirestore.getInstance()


        if (auth.currentUser != null) {
            val guncelKullaniciId=auth.currentUser?.uid.toString()
            println("güncel kullanıcı: $guncelKullaniciId")
            // Devam eden işlemler
            //veri tabanından kullanıcının bilgilerini alıp profil ekranında göstermek için yazdım .
            val KullaniciRef = database.collection("Kullanicilar").document(guncelKullaniciId)
            KullaniciRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val veri = documentSnapshot.data
                        if (veri != null) {
                            val uEmail = veri["uEmail"] as String
                            val uAd = veri["uAd"] as String
                            val uSoyad = veri["uSoyad"] as String


                            textView1.setText(uAd+" "+uSoyad)
                            textView2.setText(uEmail)
                        }
                    } else {
                        // Belge bulunamadı
                    }
                }
                .addOnFailureListener { e ->
                    // Hata durumunda yapılacak işlemler
                    Log.e("Firebase", "Veri çekme hatası: ", e)
                }

        } else {
            // Kullanıcı oturumu açmamış, null değerine dikkat edin
            println("kullanıcı girişi olmamış gibi gözüküyor")
        }

        GorselOgeEkle.setOnClickListener(){
            val intentAnaSayfa=Intent(context as Activity, FotografEkle::class.java)
            startActivity(intentAnaSayfa)

        }
        SozlesmelerVeBilgilendirmeler.setOnClickListener(){
            val intentSvB=Intent(context, SozlesmelerBilgilendirmelerEkle::class.java)
            startActivity(intentSvB)
        }
        cikisButton.setOnClickListener(){
            auth.signOut()
            val intent= Intent(context as Activity, MainActivity::class.java)
            startActivity(intent)

        }

        super.onViewCreated(view, savedInstanceState)
    }
}