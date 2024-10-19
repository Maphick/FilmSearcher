package com.makashovadev.filmsearcher.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makashovadev.filmsearcher.utils.AnimationHelper
import com.makashovadev.filmsearcher.MainActivity
import com.makashovadev.filmsearcher.domain.Film
import com.makashovadev.filmsearcher.databinding.FragmentFavoritesBinding
import com.makashovadev.filmsearcher.view.rv_adapters.decorator.TopSpacingItemDecoration
import com.makashovadev.filmsearcher.view.rv_adapters.FilmListRecyclerAdapter

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
        AnimationHelper.performFragmentCircularRevealAnimation(favoritesBinding.root, requireActivity(), 2)
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