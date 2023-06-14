package com.example.kitapdunyasi.fragmentler

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.activityler.KullaniciAdresleri
import com.example.kitapdunyasi.activityler.KullaniciSiparisVerisi
import com.example.kitapdunyasi.activityler.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class Profil : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= FirebaseAuth.getInstance()
        db=FirebaseFirestore.getInstance()
        database=FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        textView1 = view.findViewById(R.id.KullanicininAdiSoyadi)
        textView2 = view.findViewById(R.id.kullanicininEmaili)


        if (auth.currentUser != null) {
            val guncelKullaniciId=auth.currentUser?.uid.toString()
            println("güncel kullanıcı: $guncelKullaniciId")
            // Devam eden işlemler
            //veri tabanından kullanıcının bilgilerini alıp profil ekranında göstermek için yazıldı .
            val KullaniciRef = db.collection("Kullanicilar").document(guncelKullaniciId)
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

        val btnSiparislerim = view.findViewById<Button>(R.id.btnSiparisler)
        btnSiparislerim.setOnClickListener {
            val intentSiparislerim = Intent(context, KullaniciSiparisVerisi::class.java)
            startActivity(intentSiparislerim)
        }


        val btnAdreslerim=view.findViewById<Button>(R.id.btnAdresler)
        btnAdreslerim.setOnClickListener{
            val intetKullaniciAdresleri=Intent(context, KullaniciAdresleri::class.java)
            startActivity(intetKullaniciAdresleri)
        }

        val btnBizeUlasin = view.findViewById<Button>(R.id.btnBizeUlasin)
        btnBizeUlasin.setOnClickListener {
            val dialogFragment = MyDialogFragment()
            dialogFragment.show(parentFragmentManager, "MyDialogFragment")
        }

        val btnUygulamaHakkinda=view.findViewById<Button>(R.id.btnUygulamaHakkinda)
        btnUygulamaHakkinda.setOnClickListener{
            val dialogFragment2=MyDialogFragment2()
            dialogFragment2.show(parentFragmentManager,"MyDialogFragment2")
        }

        val cikisButton=view.findViewById<Button>(R.id.cikisYap)
        cikisButton.setOnClickListener{
            auth.signOut()
            //Toast.makeText(context as Activity, "Başarılı", Toast.LENGTH_SHORT).show()
            val intent=Intent(context as Activity, MainActivity::class.java)
            startActivity(intent)
        }



        super.onViewCreated(view, savedInstanceState)
    }


    class MyDialogFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Dialog içeriğini burada oluşturun
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Bize Ulaşın")
            builder.setMessage("Email: "+"KitapDunyasi@example.com"+"\n"+"Telefon: "+"0 500 006 001 61")
            builder.setPositiveButton("Tamam") { dialog, _ ->
                // Tamam düğmesine tıklandığında yapılacak işlemler
                dialog.dismiss() // Dialog penceresini kapatmak için
            }
            return builder.create()
        }
    }


    class MyDialogFragment2 : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Dialog içeriğini burada oluşturun
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Uygulama Hakkında")
            builder.setMessage("Bu uygulama Kitap alışverisi üzerine mobil bir uygulamadır.Uygulama Zülal İSKENDER tarafından yapılmıştır.")
            builder.setPositiveButton("Tamam") { dialog, _ ->
                // Tamam düğmesine tıklandığında yapılacak işlemler
                dialog.dismiss() // Dialog penceresini kapatmak için
            }
            return builder.create()
        }
    }
}