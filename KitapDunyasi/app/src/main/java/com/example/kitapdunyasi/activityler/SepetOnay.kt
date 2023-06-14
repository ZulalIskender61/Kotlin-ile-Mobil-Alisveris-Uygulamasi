package com.example.kitapdunyasi.activityler
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.databinding.ActivitySepetOnayBinding
import com.example.kitapdunyasi.model.Post4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SepetOnay : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySepetOnayBinding
    private var listeSepet = ArrayList<Post4>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val toplamFiyat = intent.getDoubleExtra("toplamFiyat",0.0)
        val toplam = if (toplamFiyat != null) toplamFiyat.toDouble() else 0.0



        binding = ActivitySepetOnayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance() // auth nesnesini başlat

        val btnOnaylaBitir = findViewById<Button>(R.id.btnOnaylaBitir)
        btnOnaylaBitir.isEnabled = false
        val button2=findViewById<Button>(R.id.YeniAdresEkle)
        button2.setOnClickListener(){
            val intent2=Intent(applicationContext, KullaniciAdresEkleme::class.java)
            intent2.putExtra("key","sepetOnayAdresEkleme")
            startActivity(intent2)
        }

        val guncelKullanici = auth.currentUser?.email.toString()
        val adreslerCollectionRef = db.collection("Sepet").document(guncelKullanici)
            .collection("Adresler")//Kullanıcının adresini almak için

        val adreslerList = ArrayList<String>()
        val mySpinner: Spinner = findViewById(R.id.AdreslerSpinner)


        adreslerCollectionRef.get()//spinnerda adresBaslığına göre seçilen adresin bilgilerini kullanmak için
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val adres = document.getString("AdresBasligi")
                    if (adres != null) {
                        adreslerList.add(adres)
                    }
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, adreslerList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                mySpinner.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting addresses", e)
            }



        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateButtonStatus(btnOnaylaBitir)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                updateButtonStatus(btnOnaylaBitir)
            }
        }

        val checkbox1 = findViewById<CheckBox>(R.id.checkboxKapidaOdeme)
        val checkbox2 = findViewById<CheckBox>(R.id.checkboxSozlesme)

        checkbox1.setOnCheckedChangeListener { _, _ ->
            updateButtonStatus(btnOnaylaBitir)
        }

        checkbox2.setOnCheckedChangeListener { _, _ ->
            updateButtonStatus(btnOnaylaBitir)
        }

        btnOnaylaBitir.setOnClickListener {
            val guncelKullanici = auth.currentUser?.email.toString()
            val secilenAdres = mySpinner.selectedItem.toString()
            val uuid = UUID.randomUUID()
            val cal = Calendar.getInstance() // Geçerli tarih ve zamanı alır
            val currentDateTime = cal.time // Geçerli tarih ve zamanı elde eder
            val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale("tr"))
            val formattedDateTime = dateFormat.format(currentDateTime)
            //Yönetici için kullanıcının sipariş verdiği ürün(leri) siparişVerilenler diye yeni bir collection oluşturarak
            //yoneticini sepet ekranına recyclerWiev aracılığıyla getirecegim.
            val yeniKoleksiyonRef = db.collection("Siparisler")

            //bitiş ve başlangıç arasında kullanıcının aldığı ürünlerin verilerini Ysepete aktarıyoruz
            //------------------------------------------------------------------Başlangıç
            adreslerCollectionRef.document(secilenAdres).get()//veritabanında seçilen adres başlığının içinde bulunduğu adres bilgilerini almak için
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null && snapshot.exists()) {
                        // Belge başarıyla alındı
                        val adresBasligi = snapshot.getString("AdresBasligi")
                        val adres = snapshot.getString("Adres")
                        val sehirIlce = snapshot.getString("SehirIlce")
                        val telefonNumarasi = snapshot.getString("TelefonNumarasi")
                        val postaKodu = snapshot.getString("PostaKodu")
                        val adress=adres+" "+sehirIlce+" "+" "+postaKodu //Tüm adresi toplu bir şekilde göstermek için
                        //println("Kullanıcının adresi"+adresBasligi+adres+sehirIlce+telefonNumarasi+postaKodu)
                        val collectionRef = db.collection("Sepet").document(guncelKullanici)
                            .collection("Ürünler") //
                        collectionRef.get()
                            .addOnSuccessListener { querySnapshot ->
                                if (querySnapshot != null) {
                                    listeSepet.clear() // Önceki verileri temizleme
                                    for (document in querySnapshot.documents) {
                                        val gorselUrl = document.getString("gorselUrl").toString()
                                        val kitapAd = document.getString("KitapAdi").toString()
                                        val kitapFiyat = document.getDouble("kitapFiyati")
                                        val postId = document.id // Dökümanın ID'sini aldım

                                        val yeniBelge = hashMapOf(
                                            "kullanici" to guncelKullanici,
                                            "adresBasligi" to adresBasligi,
                                            "adres" to adress,
                                           // "sehirIlce" to sehirIlce,
                                            "telefonNumarasi" to telefonNumarasi,
                                           // "postaKodu" to postaKodu,
                                            "gorselUrl" to gorselUrl,
                                            "kitapAdi" to kitapAd,
                                            "kitapFiyati" to kitapFiyat,

                                        )
                                        // Yeni belgeyi yeni koleksiyona kaydedin
                                        yeniKoleksiyonRef.add(yeniBelge)//Siparisler adlı koleksiyona ekliyor

                                        val siparislerim=db.collection("Siparis Gecmisi")
                                        siparislerim.get().addOnSuccessListener { querySnapshot->
                                            val siparisVerisi= hashMapOf(
                                                "kullanici" to guncelKullanici,
                                                "gorselUrl" to gorselUrl,
                                                "kitapAdi" to kitapAd,
                                                "kitapFiyati" to kitapFiyat,
                                                "adresBasligi" to adresBasligi,
                                                "adres" to adress,
                                                "tarih" to formattedDateTime,
                                                "telefonNumarasi" to telefonNumarasi,
                                                "toplam" to toplam,
                                                "kargoBilgisi" to "Siparis verildi",
                                                "postId" to postId
                                            )
                                            siparislerim.add(siparisVerisi)
                                            val collectionRef1 = db.collection("Sepet").document(guncelKullanici).collection("Ürünler")
                                            collectionRef1.get()
                                                .addOnSuccessListener { querySnapshot ->
                                                    for (document in querySnapshot.documents) {
                                                        // Her bir belgeyi sil
                                                        document.reference.delete()
                                                            .addOnSuccessListener {
                                                                Log.d("Firestore", "Belge silindi: ${document.id}")
                                                            }
                                                            .addOnFailureListener { e ->
                                                                Log.e("Firestore", "Belge silinirken hata oluştu: ${document.id}", e)
                                                            }
                                                    }
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e("Firestore", "Belgeleri alırken hata oluştu", e)
                                                }
                                        }
                                    }
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.d(ContentValues.TAG, "Error getting documents: ", e)
                            }

                        // Diğer işlemler
                    } else {
                        // Belge bulunamadı veya alınamadı
                        Log.d("Firestore", "Document not found")
                    }
                    // Başarılı olduğunda yapılacak işlemler


                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error getting address", e)
                }

            Toast.makeText(this, "Siparişiniz Tamamlanmıştır!", Toast.LENGTH_SHORT).show()

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        //---------------------------------------------Sözlesmeler ve Bilgilendirmelerin Activity üzerinde gösterimi


        db.collection("Sozlesmeler ve Bilgilendirmeler")
            .document("Cayma Hakkı").get().addOnSuccessListener { snapshot->
                if (snapshot!=null){
                    val veri=snapshot.getString("veri").toString()
                    val caymaHakkiText:TextView=findViewById(R.id.scrollViewCaymaHakkiText)
                    caymaHakkiText.text=veri
                }
            }

        db.collection("Sozlesmeler ve Bilgilendirmeler")
            .document("Ön Bilgilendirme Koşulları").get().addOnSuccessListener { snapshot->
                if (snapshot!=null){
                    val veri=snapshot.getString("veri").toString()
                    val OnBilgilendirmeKosullarıText:TextView=findViewById(R.id.scrollViewOnBilgilendirmeText)
                    OnBilgilendirmeKosullarıText.text=veri
                }
            }

        db.collection("Sozlesmeler ve Bilgilendirmeler")
            .document("Mesafeli Satış Sözleşmesi").get().addOnSuccessListener { snapshot->
                if (snapshot!=null){
                    val veri=snapshot.getString("veri").toString()
                    val MesafeliSatisSozlesmesiText:TextView=findViewById(R.id.scrollViewMesafeliSatisSozlesmesiText)
                    MesafeliSatisSozlesmesiText.text=veri
                }
            }

        //----------------------------------------------------------------------------------------------------------

    }

    private fun updateButtonStatus(button: Button) {
        val checkbox1 = findViewById<CheckBox>(R.id.checkboxKapidaOdeme)
        val checkbox2 = findViewById<CheckBox>(R.id.checkboxSozlesme)

        if (checkbox1.isChecked && checkbox2.isChecked) {
            button.isEnabled = true
        } else {
            button.isEnabled = false
        }
    }
    fun onCheckboxClicked(view: View) {
        val checkbox1 = findViewById<CheckBox>(R.id.checkboxKapidaOdeme)
        val checkbox2 = findViewById<CheckBox>(R.id.checkboxSozlesme)
        val button = findViewById<Button>(R.id.btnOnaylaBitir)

        // Checkbox durumlarına göre butonun durumunu güncelle
        if (checkbox1.isChecked && checkbox2.isChecked) {
            button.isEnabled = true
        } else {
            button.isEnabled = false
        }
    }

}

