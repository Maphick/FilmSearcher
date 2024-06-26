package com.makashovadev.filmsearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.makashovadev.filmsearcher.data.dto.Film
import com.makashovadev.filmsearcher.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    private lateinit var detailsBinding: FragmentDetailsBinding
    private lateinit var details_toolbar: Toolbar
    private lateinit var details_poster: ImageView
    private lateinit var details_description: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = Init()
        return view
    }


    //  инициализация вьюшек в этом активити
    fun Init(): View? {
        detailsBinding = FragmentDetailsBinding.inflate(layoutInflater)
        val view = detailsBinding.root
        details_toolbar = detailsBinding.detailsToolbar
        details_poster = detailsBinding.detailsPoster
        details_description = detailsBinding.nestedScrollView.detailsDescription
        Extras()
        return view
    }


    //Получаем наш фильм из переданного бандла
    fun Extras() {
        //val film = intent.extras?.get("film") as Film
        val film = arguments?.get("film") as Film
        //Устанавливаем заголовок
        details_toolbar.title = film.title
        //Устанавливаем картинку
        details_poster.setImageResource(film.poster)
        //Устанавливаем описание
        details_description.text = film.description
    }

}