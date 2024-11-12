package com.example.rickandmortyapiapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapiapp.databinding.ActivityMainBinding
import com.example.rickandmortyapiapp.model.Characters
import com.example.rickandmortyapiapp.network.ApiClient
import com.example.rickandmortyapiapp.model.CharactersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // API client dan adapter untuk RecyclerView
    private val client = ApiClient.getInstance()
    private lateinit var adapter: CharactersAdapter
    private var charactersList: List<Characters> = listOf() // Daftar lengkap karakter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup RecyclerView dan Adapter
        binding.rvMorty.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = CharactersAdapter(listOf()) // Mulai dengan data kosong
        binding.rvMorty.adapter = adapter

        // Setup fungsionalitas pencarian
        setupSearch()
        // Ambil data karakter dari API
        fetchData()
    }

    // Fungsi untuk setup pencarian berdasarkan inputan user
    private fun setupSearch() {
        binding.svSearch.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // Tidak perlu aksi ketika submit
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter karakter berdasarkan query
                val filteredList = if (!newText.isNullOrEmpty()) {
                    charactersList.filter { it.name.contains(newText, ignoreCase = true) }
                } else {
                    charactersList // Tampilkan semua karakter jika tidak ada input
                }

                // Update RecyclerView dengan data yang difilter
                adapter.updateCharacters(filteredList)

                // Tampilkan/hide TextView "Data Tidak Ditemukan"
                if (filteredList.isEmpty()) {
                    binding.tvDataNotFound.visibility = View.VISIBLE
                } else {
                    binding.tvDataNotFound.visibility = View.GONE
                }

                return true
            }
        })
    }

    // Fungsi untuk mengambil data karakter dari API
    private fun fetchData() {
        val response = client.getAllCharacters()
        response.enqueue(object : Callback<CharactersResponse> {
            override fun onResponse(call: Call<CharactersResponse>, response: Response<CharactersResponse>) {
                val characterList = response.body()?.results
                if (characterList != null) {
                    charactersList = characterList // Simpan daftar karakter untuk pencarian
                    adapter.updateCharacters(charactersList) // Tampilkan semua karakter di awal
                } else {
                    Toast.makeText(this@MainActivity, "Opss! Tidak ada data yang tersedia", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CharactersResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Koneksi Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
