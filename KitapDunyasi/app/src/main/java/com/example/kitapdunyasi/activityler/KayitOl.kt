
package com.example.kitapdunyasi.activityler
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.databinding.ActivityKayitOlBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class KayitOl : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityKayitOlBinding
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kayit_ol)
        binding=ActivityKayitOlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()//başlatmak için
        db=FirebaseFirestore.getInstance()
        //Hesap oluştur butonuna tıklama
        val database = FirebaseDatabase.getInstance().getReference("Uyeler")
        val uyelerId = database.push().key
        var kayitEmail1=""

        binding.btnHesapOlustur.setOnClickListener(){


            var kayitEmail=binding.kytMail.text.toString()
            val kayitParola=binding.kytParola.text.toString()
            var kayitParolaTekrar=binding.kytParolaTekrar.text.toString()
            var kayitAd=binding.kytAd.text.toString()
            var kayitSoyad=binding.kytSoyad.text.toString()
            var kayitAdres= " "

            //val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            println("kayitemail"+kayitEmail)
            kayitEmail1=kayitEmail.replace("."," ")

            if (kayitParola==kayitParolaTekrar){

                auth.createUserWithEmailAndPassword(kayitEmail,kayitParola).addOnCompleteListener { task ->

                    val guncelKullanici=auth.currentUser?.email.toString()
                    if (task.isSuccessful){ //işlem başarılı olduysa

                        val uid = auth.currentUser?.uid.toString()
                       /* //realtime database kayıt işlemi
                        val uyeler = Uyeler(guncelKullanici, kayitAd+" "+kayitSoyad, kayitSoyad, kayitParola, kayitParolaTekrar, kayitAdres,uid.toString())
                        database.child(uid.toString()).setValue(uyeler)*/

                        val KullaniciRef=db.collection("Kullanicilar").document(uid)
                        val kullanici = hashMapOf(
                            "uEmail" to kayitEmail,
                            "uAd" to kayitAd,
                            "uSoyad" to kayitSoyad,
                            "uParola" to kayitParola,
                            "uParolaTekrar" to kayitParolaTekrar,
                            "uAdres" to kayitAdres,
                            "uId" to uid
                        )

                        KullaniciRef.set(kullanici)
                            .addOnSuccessListener {
                                // Başarıyla eklendiğinde yapılacak işlemler
                                Log.d("Firebase", "Belge başarıyla eklendi.")
                            }
                            .addOnFailureListener { e ->
                                // Hata durumunda yapılacak işlemler
                                Log.e("Firebase", "Belge ekleme hatası: ", e)
                            }



                        Toast.makeText(applicationContext, "Kayıt Başarılı", Toast.LENGTH_LONG).show()

                        //işlem başarılı olduysa diğer aktiviteye git
                        val intent= Intent(this, GirisYap::class.java)
                        startActivity(intent)
                        finish()//yaşam döngüsünü sonlandırmak için. Geri tuşuna basınca kayıt ekranına gelmez.
                    }

                }.addOnFailureListener { exception->

                    Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
                    //kullanıcın anlayabileceği bir dilden gösterilecek mesaj (exception.localizedMessage)
                }
                //işlem bitti dinleyicisi (addOnCompleteListener)
                //task =yapılacak işlem
                //hata olursa dinleyicisi (addOnFailureListener)

            }

            else {
                Toast.makeText(applicationContext,"Şifreler Uyuşmuyor!", Toast.LENGTH_LONG).show()

            }


        }
    }


}