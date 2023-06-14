package com.example.kitapdunyasi.activityler

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.kitapdunyasi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdresDuzenleme : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adres_duzenleme)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val guncelKullanici = auth.currentUser?.email.toString()

        val adresBasligi = intent.getStringExtra("adresBasligi").toString()
        val adres = intent.getStringExtra("adres")
        val sehirIlce = intent.getStringExtra("sehirIlce")
        val postaKodu = intent.getStringExtra("postaKodu")
        val telefonNumarasi = intent.getStringExtra("telefonNumarasi")

        // EditText'leri tanımlama
        val editTextAdres: EditText = findViewById(R.id.editTextAdres)
        val editTextSehirIlce: EditText = findViewById(R.id.editTextSehirIlce)
        val editTextPostaKodu: EditText = findViewById(R.id.editTextPostaKodu)
        val editTextTelefonNumarasi: EditText = findViewById(R.id.editTextTelefonNumarasi)
        val btnAdresiDuzenle: Button = findViewById(R.id.btnAdresiDuzenle)

        // Bilgileri görüntülemek için gerekli UI öğelerine erişin ve değerleri atayın
        val adresBasligiTextView = findViewById<TextView>(R.id.adres_basligi_textview)
        val adres_basligi_textview2 = findViewById<TextView>(R.id.adres_basligi_textview2)
        val adresTextView = findViewById<TextView>(R.id.adres_textview)
        val sehirIlceTextView = findViewById<TextView>(R.id.sehir_ilce_textview)
        val postaKoduTextView = findViewById<TextView>(R.id.posta_kodu_textview)
        val telefonNumarasiTextView = findViewById<TextView>(R.id.telefon_numarasi_textview)

        // Butonun etkinlik durumunu başlangıçta devre dışı bırak
        btnAdresiDuzenle.isEnabled = false

        // EditText'lerin durumunu takip etmek için TextWatcher kullanın
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val adres = editTextAdres.text.toString().trim()
                val sehirIlce = editTextSehirIlce.text.toString().trim()
                val postaKodu = editTextPostaKodu.text.toString().trim()
                val telefonNumarasi = editTextTelefonNumarasi.text.toString().trim()

                // EditText'lerin boş olup olmadığını kontrol edin
                val isFieldsEmpty = adres.isEmpty() || sehirIlce.isEmpty() || postaKodu.isEmpty() || telefonNumarasi.isEmpty()

                // Butonun etkinlik durumunu ayarlayın
                btnAdresiDuzenle.isEnabled = !isFieldsEmpty
            }
        }

        // TextWatcher'ı EditText'lere ekle
        editTextAdres.addTextChangedListener(textWatcher)
        editTextSehirIlce.addTextChangedListener(textWatcher)
        editTextPostaKodu.addTextChangedListener(textWatcher)
        editTextTelefonNumarasi.addTextChangedListener(textWatcher)

        val koleksiyonRef = db.collection("Sepet").document(guncelKullanici)
            .collection("Adresler").document(adresBasligi)

        koleksiyonRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val dbAdresBasligi = document.getString("AdresBasligi").toString()
                    val dbAdres = document.getString("Adres").toString()
                    val dbSehirIlce = document.getString("SehirIlce").toString()
                    val dbPostaKodu = document.getString("PostaKodu").toString()
                    val dbTelefonNumarasi = document.getString("TelefonNumarasi").toString()

                    adresBasligiTextView.text = dbAdresBasligi
                    adres_basligi_textview2.text = dbAdresBasligi
                    adresTextView.text = dbAdres
                    sehirIlceTextView.text = dbSehirIlce
                    postaKoduTextView.text = dbPostaKodu
                    telefonNumarasiTextView.text = dbTelefonNumarasi
                } else {
                    // Belge bulunamadı
                }
            }
            .addOnFailureListener { exception ->
                // Hata durumunda burası çalışır
                Log.e("Firestore", "Error getting document", exception)
            }

        btnAdresiDuzenle.setOnClickListener {
            val koleksiyonRef1 = db.collection("Sepet").document(guncelKullanici)
                .collection("Adresler").document(adresBasligi)

            koleksiyonRef1.update(
                "Adres", editTextAdres.text.toString(),
                "SehirIlce", editTextSehirIlce.text.toString(),
                "PostaKodu", editTextPostaKodu.text.toString(),
                "TelefonNumarasi", editTextTelefonNumarasi.text.toString()
            )
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Adresiniz Başarıyla Güncellenmiştir.", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("guncellenenVeriler", true)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                .addOnFailureListener { exception ->
                    // Güncelleme başarısız, hata durumunda burası çalışır
                    Log.e("Firestore", "Error updating document", exception)
                }
        }
    }
}
