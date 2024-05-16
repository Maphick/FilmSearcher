package com.makashovadev.filmsearcher

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.makashovadev.filmsearcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // создание кнопок меню
        initMenuButtons()
    }


    // создание кнопок меню
    fun initMenuButtons() {
        //  Меню
        val btn_menu = binding.buttonMenu
        btn_menu.setOnClickListener {
            Toast.makeText(this, R.string.menu, Toast.LENGTH_LONG).show()
        }
        // Избранное
        val btn_favorites = binding.buttonFavorites
        btn_favorites.setOnClickListener {
            Toast.makeText(this, R.string.favorites, Toast.LENGTH_LONG).show()
        }
        // Посмотреть позже
        val btn_watch_late = binding.buttonWatchLate
        btn_watch_late.setOnClickListener {
            Toast.makeText(this, R.string.watch_later, Toast.LENGTH_LONG).show()
        }
        // Подборки
        val btn_collections = binding.buttonCollections
        btn_collections.setOnClickListener {
            Toast.makeText(this, R.string.collections, Toast.LENGTH_LONG).show()
        }
        // Настройки
        val btn_settings = binding.buttonSettings
        btn_settings.setOnClickListener {
            Toast.makeText(this, R.string.settings, Toast.LENGTH_LONG).show()
        }
    }
}