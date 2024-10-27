package com.makashovadev.filmsearcher.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.makashovadev.filmsearcher.R
import com.makashovadev.filmsearcher.data.Entity.ApiConstants
import com.makashovadev.filmsearcher.data.Entity.Film
import com.makashovadev.filmsearcher.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    private lateinit var detailsBinding: FragmentDetailsBinding
    private lateinit var details_toolbar: Toolbar
    private lateinit var details_poster: ImageView
    private lateinit var details_description: TextView
    private lateinit var details_fab_favorites: FloatingActionButton
    private lateinit var details_fab_share: FloatingActionButton
    private lateinit var currentFilm: Film
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = Init()
        return view
    }


    //  инициализация вьюшек в этом фрагменте
    fun Init(): View? {
        detailsBinding = FragmentDetailsBinding.inflate(layoutInflater)
        val view = detailsBinding.root
        // инициализация вьюшек
        InitViews()
        // получение данных о фильме из бандла
        Extras()
        // обработка плавающих кнопок
        InitFABs()
        return view
    }

    fun InitViews() {
        details_toolbar = detailsBinding.detailsToolbar
        details_poster = detailsBinding.detailsPoster
        details_description = detailsBinding.nestedScrollView.detailsDescription
        details_fab_favorites = detailsBinding.detailsFabFavorites
        details_fab_share = detailsBinding.detailsFabShare
    }

    // обработка плавающих кнопок
    fun InitFABs() {
        // ИЗБРАННОЕ
        // устанавливаем иконку на кнопку "Избранное" в зависимости от того, добавлен ли фильм в избранное
        details_fab_favorites.setImageResource(
            if (currentFilm.isInFavorites) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        )
        details_fab_favorites.setOnClickListener {
            if (!currentFilm.isInFavorites) {
                details_fab_favorites.setImageResource(R.drawable.ic_baseline_favorite_24)
                currentFilm.isInFavorites = true
            } else {
                details_fab_favorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                currentFilm.isInFavorites = false
            }
        }
        // ПОДЕЛИТЬСЯ
        //
        details_fab_share.setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this film: \n\n ${currentFilm.title} \n\n ${currentFilm.description}"
            )
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }

    //Получаем наш фильм из переданного бандла
    fun Extras() {
        //val film = intent.extras?.get("film") as Film
        val film = arguments?.get("film") as Film
        //Устанавливаем заголовок
        details_toolbar.title = film.title
        // загрузку картинки, когда мы создаем фрагмент с деталями фильма в DetailsFragment:
        Glide.with(this)
            .load(ApiConstants.IMAGES_URL + "w780" + film.poster)
            .centerCrop()
            .into(details_poster)
        //Устанавливаем описание
        details_description.text = film.description
        currentFilm = film
    }

}