package com.makashovadev.filmsearcher

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.makashovadev.filmsearcher.data.dto.Film
import com.makashovadev.filmsearcher.databinding.ActivityDetailsBinding
import com.makashovadev.filmsearcher.databinding.ActivityMainBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var detailsBinding: ActivityDetailsBinding
    private lateinit var details_toolbar: Toolbar
    private lateinit var details_poster: ImageView
    private lateinit var details_description: TextView
    private lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Init()
        Extras()
        initBottomNavigation()
    }

    //  инициализация вьюшек в этом активити
    fun Init() {
        detailsBinding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = detailsBinding.root
        setContentView(view)
        details_toolbar = detailsBinding.detailsToolbar
        details_poster = detailsBinding.detailsPoster
        details_description = detailsBinding.detailsDescription
    }

    //Получаем наш фильм из переданного бандла
    fun Extras() {
        val film = intent.extras?.get("film") as Film
        //Устанавливаем заголовок
        details_toolbar.title = film.title
        //Устанавливаем картинку
        details_poster.setImageResource(film.poster)
        //Устанавливаем описание
        details_description.text = film.description
    }


    // инициализация нижнего меню
    fun initBottomNavigation() {
        bottom_navigation = detailsBinding.bottomNavigation
        val snackbarFavourite = Snackbar.make(bottom_navigation, "Избранное", Snackbar.LENGTH_SHORT)
        snackbarFavourite.setAnchorView(bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.favorites -> {
                    Snackbar.make(bottom_navigation, "Избранное", Snackbar.LENGTH_SHORT)
                        .setAnchorView(bottom_navigation).show()
                    true
                }

                R.id.watch_later -> {
                    Snackbar.make(bottom_navigation, "Посмотреть похже", Snackbar.LENGTH_SHORT)
                        .setAnchorView(bottom_navigation).show()
                    true
                }

                R.id.selections -> {
                    Snackbar.make(bottom_navigation, "Подборки", Snackbar.LENGTH_SHORT)
                        .setAnchorView(bottom_navigation).show()
                    true
                }

                else -> false
            }
        }
    }

}