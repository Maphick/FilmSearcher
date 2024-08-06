package com.makashovadev.filmsearcher

import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.makashovadev.filmsearcher.data.dto.Film
import com.makashovadev.filmsearcher.databinding.ActivityMainBinding
import com.makashovadev.filmsearcher.fragments.DetailsFragment
import com.makashovadev.filmsearcher.fragments.FavoritesFragment
import com.makashovadev.filmsearcher.fragments.HomeFragment
import com.makashovadev.filmsearcher.fragments.SelectionsFragment
import com.makashovadev.filmsearcher.fragments.WatchLaterFragment

class MainActivity : AppCompatActivity() {
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

    // при нажатии кнопки  "назад"
    override fun onBackPressed() {
       // if (supportFragmentManager.)


        // если это последний экран
        if (supportFragmentManager.backStackEntryCount == 1) {
            // то вызывается алерт диалог
            BuildAlertDialog()
        } else {
            super.onBackPressed()
        }
    }



    // всплывающее меню о том, что пользователь сейчас покинет приложение
    fun BuildAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("Вы хотите выйти?")
            .setIcon(R.drawable.baseline_menu_24)
            .setPositiveButton("Да") { _, _ ->
                finish()
            }
            .setNegativeButton("Нет") { _, _ ->

            }
            .setNeutralButton("Не знаю") { _, _ ->
                Toast.makeText(this, "Решайся", Toast.LENGTH_SHORT).show()
            }
            .setMessage("Нам не хотелось бы, чтобы вы уходили")
            .setView(EditText(this))
            .show()
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
            .replace(R.id.fragment_placeholder, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

}