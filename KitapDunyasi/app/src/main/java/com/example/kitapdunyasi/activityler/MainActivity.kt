package com.example.kitapdunyasi.activityler

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kitapdunyasi.fragmentler.*
import com.example.kitapdunyasi.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val YSepetGelenVeri = intent.getStringExtra("anahtar")
        //Anahtar kelime bize yönetici sayfasındaki kargo verme işleminden
        // sonra sayfanın güncel olmasını sağlamak için kullanılmıştır.
        if (YSepetGelenVeri != null && YSepetGelenVeri == "Kargoya Verildi") {
            replaceFragment(YSepet())//Eger yönetici kargo verme işlemi yaptıysa güncel bir
        // YSepet fragmenti olması için MainActivity üzerinde yönlendirme yapılmıştır.
        } else {
            replaceFragment(AnaSayfa())//Eğer YSepet üzerinde kargo verme işlemi gerçekleşmediyse
        // Uygulama herzaman AnaSayfa adlı fragment ile başlar.
        }
        auth=FirebaseAuth.getInstance() //Firebase Authentication başlatılmıştır.
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        //Fragmentler arası geçiş burada sağlanmıştır.
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.anaSayfa -> {
                    replaceFragment(AnaSayfa())
                    true
                }
                R.id.kategori -> {
                    replaceFragment(Kategori())
                    true
                }
                R.id.sepet -> {
                    val guncelKullanici=auth.currentUser
                    if(guncelKullanici!=null){
                        val a=auth.currentUser?.email.toString()
                        if (a=="zulal@gmail.com"){
                            replaceFragment(YSepet())
                        }else{
                            replaceFragment(Sepet())
                        }
                    }
                    true
                }
                R.id.profil -> {
                    val guncelKullanici=auth.currentUser //Güncel kullanıcının durumuna göre yönetici
                    // veya kullanıcı profillerine yönlendirme yapılmıştır.
                    if (guncelKullanici!=null){
                        val a=auth.currentUser?.email.toString()
                        if(a=="zulal@gmail.com"){
                            replaceFragment(YProfil()) //Yönticinin hesabına gider
                        }else{
                            replaceFragment(Profil()) //Kullanıcıların hesabına gider
                        }
                        true
                    } else{
                        val intent=Intent(applicationContext, GirisYap::class.java)
                        startActivity(intent)
                        true
                    }
                }
                else -> false
            }
        }
    }
    private fun replaceFragment(fragment: Fragment){//Fragmentler arası geçiş için oluşturulan fonksiyon

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView,fragment)
        fragmentTransaction.commit()

    }
}
