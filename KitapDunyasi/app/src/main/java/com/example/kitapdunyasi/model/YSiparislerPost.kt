package com.example.kitapdunyasi.model
import java.io.Serializable
class YSiparislerPost(
    var kullanici: String,
    var imageUrl: String,
    var KitapAdi: String,
    var kitapFiyati: Double,
    var AdresBasligi: String,
    var Adres: String,
    var tarih: String,
    var TelefonNumarasi: String,
    var toplam: Double,
    var kargoBilgisi: String,
    var postId: String
): Serializable {
}
