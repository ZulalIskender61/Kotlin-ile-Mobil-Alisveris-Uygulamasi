package com.example.kitapdunyasi.fragmentler

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kitapdunyasi.R
import com.example.kitapdunyasi.adapter.AnaSayfaCokSatanlarAdapter
import com.example.kitapdunyasi.adapter.AnaSayfaYeniCikanlarAdapter
import com.example.kitapdunyasi.adapter.AnaSayfaİlgiGorenlerAdapter
import com.example.kitapdunyasi.model.Post
import com.example.kitapdunyasi.model.Post2
import com.example.kitapdunyasi.model.Post3
import com.google.firebase.firestore.FirebaseFirestore

class AnaSayfa : Fragment() {

    private lateinit var recyclerViewAdapter1: AnaSayfaCokSatanlarAdapter
    private lateinit var recyclerViewAdapter2: AnaSayfaİlgiGorenlerAdapter
    private lateinit var recyclerViewAdapter3: AnaSayfaYeniCikanlarAdapter
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    private lateinit var database: FirebaseFirestore
    private var listeAnasayfa1 = ArrayList<Post>()
    private var listeAnasayfa2 = ArrayList<Post2>()
    private var listeAnasayfa3 = ArrayList<Post3>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseFirestore.getInstance()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ana_sayfa, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView1 = view.findViewById(R.id.recyclerView1)
        recyclerView1.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAdapter1 = AnaSayfaCokSatanlarAdapter(listeAnasayfa1)
        recyclerView1.adapter = recyclerViewAdapter1
        recyclerView1.addItemDecoration(LeftSpacingDecoration(10))//recyclerView deki itemlerin arasındaki mesafeyi atarlamak için

        recyclerView2 = view.findViewById(R.id.recyclerView2)
        recyclerView2.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAdapter2 = AnaSayfaİlgiGorenlerAdapter(listeAnasayfa2)
        recyclerView2.adapter = recyclerViewAdapter2
        recyclerView2.addItemDecoration(LeftSpacingDecoration(10))//recyclerView deki itemlerin arasındaki mesafeyi atarlamak için

        recyclerView3 = view.findViewById(R.id.recyclerView3)
        recyclerView3.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAdapter3 = AnaSayfaYeniCikanlarAdapter(listeAnasayfa3)
        recyclerView3.adapter = recyclerViewAdapter3
        recyclerView3.addItemDecoration(LeftSpacingDecoration(10))//recyclerView deki itemlerin arasındaki mesafeyi atarlamak için

        //Bu fonksiyonlar firebase veritabanındaki verileri çekmek için kullanılmıştır .
        verileriAl_IG()
        verileriAl_CS()
        verileriAl_YC()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bottom_nav, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.anaSayfa -> {

                val action = AnaSayfaDirections.actionAnaSayfa3Self()
                Navigation.findNavController(context as Activity, R.id.fragmentContainerView)
                    .navigate(action)
            }
            R.id.kategori -> {
                val action = AnaSayfaDirections.actionAnaSayfa3ToKategori3()
                Navigation.findNavController(context as Activity, R.id.fragmentContainerView)
                    .navigate(action)
            }
            R.id.sepet -> {
                val action = AnaSayfaDirections.actionAnaSayfa3ToSepet2()
                Navigation.findNavController(context as Activity, R.id.fragmentContainerView)
                    .navigate(action)
            }
            R.id.profil -> {
                val action = AnaSayfaDirections.actionAnaSayfa3ToProfil2()
                Navigation.findNavController(context as Activity, R.id.fragmentContainerView)
                    .navigate(action)
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView1.adapter = null // RecyclerView adapter'ını null yaparak referansları temizle
        recyclerView2.adapter = null // RecyclerView adapter'ını null yaparak referansları temizle
        recyclerView3.adapter = null // RecyclerView adapter'ını null yaparak referansları temizle
        listeAnasayfa1.clear() // ArrayList'i temizle
        listeAnasayfa2.clear() // ArrayList'i temizle
        listeAnasayfa3.clear() // ArrayList'i temizle


    }

    private fun verileriAl_IG() {
        database.collection("İlgi Görenler").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle the exception
            } else {
                if (snapshot != null && !snapshot.isEmpty) {
                    val documents = snapshot.documents
                    listeAnasayfa1.clear()
                    for (document in documents) {
                        val kitabinAlani = document.getString("Alan Adı") ?: ""
                        val kitapAdi = document.getString("Kitabın adı") ?: ""
                        val gorselUrl = document.getString("imageUrl") ?: ""
                        val kitabinYazari = document.getString("Kitabın yazarı") ?: ""
                        val kitabinSayfaSayisi = document.getString("Kitabın sayfa sayısı") ?: ""
                        val kitabinFiyati = document.getDouble("Kitabın fiyatı")
                        val parsedKitabinFiyati:Double = kitabinFiyati ?: 0.0

                        val alinanVeri1 = Post(
                            kitabinAlani,
                            gorselUrl,
                            kitapAdi,
                            kitabinYazari,
                            kitabinSayfaSayisi,
                            parsedKitabinFiyati
                        )

                        listeAnasayfa1.add(alinanVeri1)
                    }
                    recyclerViewAdapter1.notifyDataSetChanged()
                }
            }
        }
    }

    private fun verileriAl_CS() {
        database.collection("Çok Satanlar").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle the exception
            } else {
                if (snapshot != null && !snapshot.isEmpty) {
                    val documents = snapshot.documents
                    listeAnasayfa2.clear()
                    for (document in documents) {
                        val kitabinAlani = document.getString("Alan Adı") ?: ""
                        val kitapAdi = document.getString("Kitabın adı") ?: ""
                        val kitabinYazari = document.getString("Kitabın yazarı") ?: ""
                        val kitabinSayfaSayisi = document.getString("Kitabın sayfa sayısı") ?: ""
                        val gorselUrl = document.getString("imageUrl") ?: ""
                        val kitabinFiyati = document["Kitabın fiyatı"] as? Double
                        val parsedKitabinFiyati: Double = kitabinFiyati ?: 0.0


                        val alinanVeri2 = Post2(
                            kitabinAlani,
                            gorselUrl,
                            kitapAdi,
                            kitabinYazari,
                            kitabinSayfaSayisi,
                            parsedKitabinFiyati
                        )

                        listeAnasayfa2.add(alinanVeri2)
                    }
                    recyclerViewAdapter2.notifyDataSetChanged()
                }
            }
        }
    }

    private fun verileriAl_YC() {
        database.collection("Yeni Çıkanlar").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle the exception
            } else {
                if (snapshot != null && !snapshot.isEmpty) {
                    val documents = snapshot.documents
                    listeAnasayfa3.clear()
                    for (document in documents) {
                        val kitabinAlani = document.getString("Alan Adı") ?: ""
                        val kitapAdi = document.getString("Kitabın adı") ?: ""
                        val kitabinYazari = document.getString("Kitabın yazarı") ?: ""
                        val kitabinSayfaSayisi = document.getString("Kitabın sayfa sayısı") ?: ""
                        val gorselUrl = document.getString("imageUrl") ?: ""
                        val kitabinFiyati = document.getDouble("Kitabın fiyatı")
                        val parsedKitabinFiyati:Double = kitabinFiyati ?: 0.0

                        val alinanVeri3 = Post3(
                            kitabinAlani,
                            gorselUrl,
                            kitapAdi,
                            kitabinYazari,
                            kitabinSayfaSayisi,
                            parsedKitabinFiyati

                        )

                        listeAnasayfa3.add(alinanVeri3)
                    }
                    recyclerViewAdapter3.notifyDataSetChanged()
                }
            }
        }
    }

    //itemlerin sol taraflarına boşluk koyulmak için oluşturulmuştur.
    class LeftSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.left = spacing
        }
    }
}


