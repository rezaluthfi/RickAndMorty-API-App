package com.example.rickandmortyapiapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortyapiapp.databinding.ActivityMainBinding
import com.example.rickandmortyapiapp.model.Characters
import com.example.rickandmortyapiapp.network.ApiClient
import com.example.rickandmortyapiapp.network.CharactersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val client = ApiClient.getInstance()
    private lateinit var adapter: CharactersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up RecyclerView and Adapter
        binding.rvMorty.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = CharactersAdapter(listOf()) // Adapter dimulai dengan daftar kosong
        binding.rvMorty.adapter = adapter

        // Fetch data from API
        fetchData()
    }

    private fun fetchData() {
        val response = client.getAllCharacters()
        response.enqueue(object : Callback<CharactersResponse> {
            override fun onResponse(call: Call<CharactersResponse>, response: Response<CharactersResponse>) {
                val characterList = response.body()?.results
                if (characterList != null) {
                    // Update data in adapter
                    adapter.updateCharacters(characterList)
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

