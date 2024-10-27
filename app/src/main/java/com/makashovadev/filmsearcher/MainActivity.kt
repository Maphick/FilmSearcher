package com.makashovadev.filmsearcher

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.makashovadev.filmsearcher.data.Entity.Film
import com.makashovadev.filmsearcher.databinding.ActivityMainBinding
import com.makashovadev.filmsearcher.view.fragments.DetailsFragment
import com.makashovadev.filmsearcher.view.fragments.FavoritesFragment
import com.makashovadev.filmsearcher.view.fragments.HomeFragment
import com.makashovadev.filmsearcher.view.fragments.SelectionsFragment
import com.makashovadev.filmsearcher.view.fragments.SettingsFragment
import com.makashovadev.filmsearcher.view.fragments.WatchLaterFragment
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel

class MainActivity : AppCompatActivity() {

    //val mainActivityViewModel : HomeFragmentViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    // фрагмент
    private lateinit var fragment_placeholder: FrameLayout
    // верхнее меню
    private lateinit var topAppBar: MaterialToolbar
    // нижнее меню
    private lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Init()
    }


    fun Init()
    {

        // инициализация вепхнего меню
        //initTopAppBar()
        // инициализация нижнего меню
        initBottomNavigation()
        // инициализация фрагмента
        fragment_placeholder = binding.fragmentPlaceholder
        //Запускаем фрагмент при старте
        supportFragmentManager
            .beginTransaction()
            .add(fragment_placeholder.id, HomeFragment())
            .addToBackStack(null)
            .commit()
    }


    fun launchDetailsFragment(film: Film) {
        //Создаем "посылку"
        val bundle = Bundle()
        //Кладем наш фильм в "посылку"
        bundle.putParcelable("film", film)
        //Кладем фрагмент с деталями в перменную
        val fragment = DetailsFragment()
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.arguments = bundle

        //Запускаем фрагмент
        supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(fragment_placeholder.id, fragment)
            .addToBackStack(null)
            .commit()
    }

    // инициализация нижнего меню
    fun initBottomNavigation() {
        bottom_navigation = binding.bottomNavigation
        bottom_navigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.home -> {
                    val tag = "home"
                    val fragment = checkFragmentExistence(tag)
                    //В первом параметре, если фрагмент не найден и метод вернул null, то с помощью
                    //элвиса мы вызываем создание нового фрагмента
                    changeFragment( fragment?: HomeFragment(), tag)
                    true
                    }
                // запуск фрагмента с избранным
                R.id.favorites -> {
                    val tag = "favorites"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: FavoritesFragment(), tag)
                    true
                }
                R.id.watch_later -> {
                    val tag = "watch_later"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: WatchLaterFragment(), tag)
                    true
                }
                R.id.selections -> {
                    val tag = "selections"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: SelectionsFragment(), tag)
                    true
                }
                R.id.settings -> {
                    val tag = "settings"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: SettingsFragment(), tag)
                    true
                }
                else -> false
            }
        }
    }

    //Ищем фрагмент по тегу, если он есть то возвращаем его, если нет, то null
    private fun checkFragmentExistence(tag: String): Fragment? = supportFragmentManager.findFragmentByTag(tag)

    // Сам запуск фрагмента
    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(fragment_placeholder.id, fragment, tag)
            .addToBackStack(null)
            .commit()
    }
    // .replace(  R.id.fragment_placeholder, fragment, tag)
    // binding.fragmentPlaceholder.id
}