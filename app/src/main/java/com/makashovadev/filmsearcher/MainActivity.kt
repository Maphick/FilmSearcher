package com.makashovadev.filmsearcher

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.makashovadev.filmsearcher.data.dto.Film
import com.makashovadev.filmsearcher.data.repository.Repository
import com.makashovadev.filmsearcher.databinding.ActivityMainBinding
import com.makashovadev.filmsearcher.decorator.PaginationLoadingDecoration
import com.makashovadev.filmsearcher.decorator.TopSpacingItemDecoration
import com.makashovadev.filmsearcher.diff_util.FilmDiff
import com.makashovadev.filmsearcher.diff_util.updateData
import com.makashovadev.filmsearcher.presentation.FilmListRecyclerAdapter
import com.makashovadev.filmsearcher.touch_helper.SimpleItemTouchHelperCallback

class MainActivity : AppCompatActivity() {
    //private val repository = Repository()
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragment_placeholder: FrameLayout

    // верхнее меню
    private lateinit var topAppBar: MaterialToolbar

    // нижнее меню
    private lateinit var bottom_navigation: BottomNavigationView

    // при нажатии кнопки  "назад"

    /*override fun onBackPressed() {
        super.onBackPressed()
        //BuildAlertDialog()
    }*/


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        fragment_placeholder = binding.fragmentPlaceholder
        initTopAppBar()
        // инициализация нижнего меню
        initBottomNavigation()
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
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null)
            .commit()
    }


    // инициализация верхнего меню
    fun initTopAppBar() {
        topAppBar = binding.topAppBar
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }


    // инициализация нижнего меню
    fun initBottomNavigation() {
        bottom_navigation = binding.bottomNavigation
        bottom_navigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.favorites -> {
                    Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
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