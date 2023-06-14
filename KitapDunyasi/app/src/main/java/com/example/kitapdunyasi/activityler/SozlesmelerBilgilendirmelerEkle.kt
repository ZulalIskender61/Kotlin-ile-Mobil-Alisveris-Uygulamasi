package com.example.kitapdunyasi.activityler

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kitapdunyasi.R
import com.google.firebase.firestore.FirebaseFirestore

class SozlesmelerBilgilendirmelerEkle : AppCompatActivity() {
    private lateinit var Secilen: String
    private lateinit var db: FirebaseFirestore
    private lateinit var klasorAdi: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sozlesmeler_bilgilendirmeler_ekle)
        db=FirebaseFirestore.getInstance()
        val SvBspinner: Spinner =findViewById(R.id.SvB_Spinner)//spinner ı kullanmak için id sini , türünü alıp değişkenime atadım.
        val items = listOf("Cayma Hakkı","Ön Bilgilendirme Koşulları","Mesafeli Satış Sözleşmesi")// Spinner için veri kümesi oluşturdum.
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)//Spinner ı adaptere atadım.
        SvBspinner.adapter=adapter //Spinner ı adaptere atadım.

        SvBspinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Secilen=items[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Bir şey seçilmediğinde yapılacak işlemler buraya yazılabilir
            }
        }

        val btnVerileriEkle: Button= findViewById(R.id.btnVerileriEkle)
        val editText: EditText=findViewById(R.id.EditTextVeriler)
        btnVerileriEkle.setOnClickListener {
            val veri = editText.text.toString() // editText'ten girilen veriyi aldım
            val dokumanVerisi = hashMapOf("veri" to veri) // Veriyi bir HashMap olarak tanımladım
            val klasorAdi = Secilen // Spinner'dan seçilen klasör adını aldım

            // Firestore'a verileri kaydedin
            db.collection("Sozlesmeler ve Bilgilendirmeler")
                .document(klasorAdi)
                .set(dokumanVerisi)
                .addOnSuccessListener {
                    // Başarıyla kaydedildiğinde yapılacak işlemler
                    Toast.makeText(applicationContext, "İşlem başarıyla gerçekleştirildi", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Kaydetme sırasında bir hata oluştuğunda yapılacak işlemler
                }
            editText.text.clear()
        }



        }

}

