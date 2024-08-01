package com.makashovadev.filmsearcher

import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.makashovadev.filmsearcher.data.dto.Film
import com.makashovadev.filmsearcher.databinding.ActivityMainBinding
import com.makashovadev.filmsearcher.fragments.DetailsFragment
import com.makashovadev.filmsearcher.fragments.FavoritesFragment
import com.makashovadev.filmsearcher.fragments.HomeFragment

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
                // запуск фрагмента с избранным
                R.id.favorites -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(fragment_placeholder.id, FavoritesFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.watch_later -> {
                    Toast.makeText(this, "Посмотреть похже", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.selections -> {
                    Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }


}