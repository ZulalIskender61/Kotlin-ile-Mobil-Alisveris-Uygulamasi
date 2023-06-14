package com.example.kitapdunyasi.activityler

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kitapdunyasi.fragmentler.AnaSayfa
import com.example.kitapdunyasi.fragmentler.Kategori
import com.example.kitapdunyasi.fragmentler.YProfil
import com.example.kitapdunyasi.fragmentler.YSepet
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.databinding.ActivityYmainBinding
import com.google.firebase.auth.FirebaseAuth

class Y_MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityYmainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYmainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(AnaSayfa())

        auth= FirebaseAuth.getInstance()


        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {


                R.id.anaSayfa -> {
                    replaceFragment(AnaSayfa())
                    true
                }
                R.id.kategori -> {
                    replaceFragment(Kategori())
                    true
                }
                R.id.y_sepet -> {
                    replaceFragment(YSepet())
                    true
                }
                R.id.y_profil -> {
                    val guncelKullanici=auth.currentUser
                    if (guncelKullanici!=null){
                        replaceFragment(YProfil())
                        true
                    }
                    else{
                        val intent= Intent(applicationContext, GirisYap::class.java)
                        startActivity(intent)

                        true
                    }
                }
                else -> false
            }

        }
    }
    private fun replaceFragment(fragment: Fragment){ //replace : yer değiştirmek

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView,fragment)
        fragmentTransaction.commit()

    }
}