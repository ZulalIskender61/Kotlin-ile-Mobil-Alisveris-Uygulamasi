package com.example.kitapdunyasi.activityler


import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kitapdunyasi.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class FotografEkle : AppCompatActivity() {
    lateinit var secilenGorsel: Uri
    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private lateinit var editText3: EditText
    private lateinit var editText4: EditText
    private lateinit var klasorAdi: String
    private lateinit var imageView: ImageView
    private lateinit var database: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_ekle)

        database=FirebaseFirestore.getInstance()
        val btnPaylas = findViewById<Button>(R.id.btnGorselEkle)
        imageView = findViewById(R.id.imageView)
        editText1 = findViewById(R.id.kitapAd)
        editText2 = findViewById(R.id.kitapFiyat)
        editText3 = findViewById(R.id.kitapYazar)
        editText4 = findViewById(R.id.kitapSayfaSayisi)

        // Spinner bileşeninin referansını alın
        val mySpinner: Spinner = findViewById(R.id.anaSayfaSpinner)

        // Spinner için veri kümesi oluşturun
        val items = listOf("İlgi Görenler", "Çok Satanlar", "Yeni Çıkanlar","Türk Edebiyatı",
            "Araştırma Tarih","Bilim","Edebiyat","Felsefe","Şiir Kitapları")

        // ArrayAdapter oluşturun
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        // Spinner'a adapteri atayın
        mySpinner.adapter = adapter
        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Seçilen öğenin adını klasorAdi değişkenine ata
                klasorAdi = items[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Bir şey seçilmediğinde yapılacak işlemler buraya yazılabilir
            }
        }

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
        btnPaylas.setOnClickListener {
            gorselEkle()
        }





    }
    private fun gorselEkle() {
        val inputStream = contentResolver.openInputStream(secilenGorsel)
        val bytes = inputStream!!.readBytes()

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection(klasorAdi)

        val uuid = UUID.randomUUID()
        val imageName = "${uuid}.jpg"

        val additionalData = hashMapOf(
            "Alan Adı" to klasorAdi,
            "Kitabın adı" to editText1.text.toString(),
            "Kitabın yazarı" to editText3.text.toString(),
            "Kitabın sayfa sayısı" to editText4.text.toString(),
            "Kitabın fiyatı" to editText2.text.toString().toDouble()
        )

        val storageRef = FirebaseStorage.getInstance().getReference(klasorAdi).child(imageName)
        val uploadTask = storageRef.putBytes(bytes)
        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                additionalData["imageUrl"] = imageUrl // imageUrl'i additionalData hashmap'ine ekleyin

                collectionRef.document(imageName)
                    .set(additionalData)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot added with ID: $imageName")
                        Toast.makeText(applicationContext, "Yükleme başarılı!", Toast.LENGTH_SHORT).show()
                        editText1.text.clear()
                        editText2.text.clear()
                        editText3.text.clear()
                        editText4.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                        Toast.makeText(applicationContext, "Yükleme başarısız oldu!", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {
            Log.e(TAG, "Upload failed: $it")
            Toast.makeText(applicationContext, "Yükleme başarısız oldu!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // görselin urisini aldım
            secilenGorsel= data?.data!!
            // secilen görseli image view de gösterdim
            imageView.setImageURI(secilenGorsel)
        }
    }


}