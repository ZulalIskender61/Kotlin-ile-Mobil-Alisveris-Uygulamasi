package com.example.kitapdunyasi.activityler

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.databinding.ActivityGirisYapBinding
import com.google.firebase.auth.FirebaseAuth

class GirisYap : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityGirisYapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris_yap)
        binding=ActivityGirisYapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()//başlatmak için

        binding.btnGiris.setOnClickListener(){
            val grsEmail = binding.email.text.toString()
            val grsParola=binding.parola.text.toString()
            auth.signInWithEmailAndPassword(grsEmail,grsParola).addOnCompleteListener { task->
                if (task.isSuccessful){ //giriş yapma işlemi doğru mu
                    val guncelKullanici=auth.currentUser?.email.toString()
                    if (guncelKullanici=="zulal@gmail.com"){
                        Toast.makeText(this,"Hoşgeldin : ${guncelKullanici}", Toast.LENGTH_LONG).show()
                        val intent=Intent(applicationContext, Y_MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                   else{
                        Toast.makeText(this,"Hoşgeldin : ${guncelKullanici}", Toast.LENGTH_LONG).show()
                        val intent= Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()}
                }
            }.addOnFailureListener { exception->
                Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnKayit.setOnClickListener(){
            intent=Intent(applicationContext, KayitOl::class.java)
            startActivity(intent)
        }
    }
}