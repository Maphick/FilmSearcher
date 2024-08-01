package com.makashovadev.filmsearcher.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.makashovadev.filmsearcher.MainActivity
import com.makashovadev.filmsearcher.R
import com.makashovadev.filmsearcher.data.dto.Film
import com.makashovadev.filmsearcher.databinding.FragmentDetailsBinding
import com.makashovadev.filmsearcher.databinding.FragmentFavoritesBinding
import com.makashovadev.filmsearcher.decorator.PaginationLoadingDecoration
import com.makashovadev.filmsearcher.decorator.TopSpacingItemDecoration
import com.makashovadev.filmsearcher.presentation.FilmListRecyclerAdapter
import com.makashovadev.filmsearcher.touch_helper.SimpleItemTouchHelperCallback

// фрагмент для отображения избранных фильмов
class FavoritesFragment : Fragment() {
    private lateinit var favoritesBinding: FragmentFavoritesBinding
    private lateinit var favorites_recycler: RecyclerView
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = Init()
        return view
    }


    //  инициализация вьюшек в этом фрагменте
    fun Init(): View? {
        favoritesBinding = FragmentFavoritesBinding.inflate(layoutInflater)
        val view = favoritesBinding.root
        return view
    }

    // инициализация Recycler View
    // и применение настроек
    fun initRV() {
        val favoritesList: List<Film> = emptyList()
        //находим наш RV
        favorites_recycler = favoritesBinding.favoritesRecycler
        favorites_recycler
            .apply {
                filmsAdapter =
                    FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                        override fun click(film: Film) {
                            (requireActivity() as MainActivity).launchDetailsFragment(film)
                        }
                    })
                //Присваиваем адаптер
                adapter = filmsAdapter
                //Присвои layoutmanager
                layoutManager = LinearLayoutManager(requireContext())
                //Применяем декоратор для отступов
                val decorator = TopSpacingItemDecoration(8)
                addItemDecoration(decorator)
            }
        //Кладем нашу БД в RV
        //filmsAdapter.add
        //Кладем нашу БД в RV
        //downloadFirstPage()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRV()
        super.onViewCreated(view, savedInstanceState)
    }

}