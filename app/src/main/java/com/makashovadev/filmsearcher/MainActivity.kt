package com.makashovadev.filmsearcher

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.makashovadev.filmsearcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var container: LinearLayout
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var imageView4: ImageView
    private lateinit var imageView5: ImageView

    // верхнее меню
    private lateinit var topAppBar: MaterialToolbar

    // нижнее меню
    private lateinit var bottom_navigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // инициализация всех компонентов
        init()
        setUpperPosterAnimator()
    }

    // инициализация всех компонентов
    fun init() {
        initTopAppBar()
        initBottomNavigation()
        container = binding.container
        imageView1 = binding.imageView1
        imageView2 = binding.imageView2
        imageView3 = binding.imageView3
        imageView4 = binding.imageView4
        imageView5 = binding.imageView5
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

    // анисация нажатия на один из верхних постеров
    fun setUpperPosterAnimator() {
        imageView1.setOnClickListener {
            objectAnimatorTranslationAnim(imageView1)
        }
        imageView2.setOnClickListener {
            objectAnimatorScaleAnim(imageView2)
        }
        imageView3.setOnClickListener {
            ViewPropertyAnimation(imageView3)
        }
        imageView4.setOnClickListener {
            ViewPropertyAnimation(imageView4)
        }
        imageView5.setOnClickListener {
            ViewPropertyAnimation(imageView5)
        }
    }

}