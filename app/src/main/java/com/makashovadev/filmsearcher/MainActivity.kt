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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
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
    private val repository = Repository()
    private lateinit var binding: ActivityMainBinding
    private lateinit var container: LinearLayout
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var imageView4: ImageView
    private lateinit var imageView5: ImageView

    //
    private lateinit var main_recycler: RecyclerView
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

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
        initRV()
        RecyclerViewSetScroollListener()
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

    // инициализация Recycler View
    // и применение настроек
    fun initRV() {
        //находим наш RV
        main_recycler = binding.mainRecycler
        main_recycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            //оставим его пока пустым, он нам понадобится во второй части задания
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        //Создаем бандл и кладем туда объект с данными фильма
                        val bundle = Bundle()
                        //Первым параметром указывается ключ, по которому потом будем искать, вторым сам
                        //передаваемый объект
                        bundle.putParcelable("film", film)
                        //Запускаем наше активити
                        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                        //Прикрепляем бандл к интенту
                        intent.putExtras(bundle)
                        //Запускаем активити через интент
                        startActivity(intent)
                    }
                })
            //Присваиваем адаптер
            adapter = filmsAdapter
            //Присвои layoutmanager
            layoutManager = LinearLayoutManager(this@MainActivity)

            //Применяем декоратор для отступов
            val topSpacingDecorator = TopSpacingItemDecoration(8)
            addItemDecoration(topSpacingDecorator)


            // применяем декоратор для пагинации
            val paginationItemDecoration = PaginationLoadingDecoration()
            addItemDecoration(paginationItemDecoration)

            //  Этот класс сам определит на каком элементе остановилось пролистывание и доведет до
            //  ближайшего элемента, чтобы его было полностью видно.
            val linearSnapHelper = LinearSnapHelper()
            linearSnapHelper.attachToRecyclerView(this)

            // swipe + drag&drop
            val callback = SimpleItemTouchHelperCallback(this.adapter as FilmListRecyclerAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(this)
        }


        //Кладем нашу БД в RV
        downloadFirstPage()
    }

    fun RecyclerViewSetScroollListener() {
        var isLoading = false
        val scrollListener = object : RecyclerView.OnScrollListener() {
            @Override
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as RecyclerView.LayoutManager
                //смотрим сколько элементов на экране
                val visibleItemCount: Int = layoutManager.childCount
                //сколько всего элементов
                val totalItemCount: Int = layoutManager.itemCount

                //какая позиция первого элемента
                val firstVisibleItems =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                //проверяем, грузим мы что-то или нет
                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItems >= totalItemCount) {
                        //ставим флаг, что мы попросили еще элементы
                        isLoading = true
                        //Вызывает загрузку данных в RecyclerView
                        val newList = downloadAnyPage()
                        // задержка для демонстрации загрузки
                        Handler().postDelayed({
                            // Оповещение RecyclerView об изменении данных с помощью DiffUtil.
                            updateData(newList, filmsAdapter)
                            isLoading = false
                        }, 2000)
                    }

                }
            }
        }
        main_recycler.setOnScrollListener(scrollListener)
    }


    // загрузка первой страницы
    fun downloadFirstPage() {
        // add first page
        val addedList = repository.firstPage()
        val newList = arrayListOf<Film>()
        newList.addAll(filmsAdapter.getItems())
        newList.addAll(addedList)
        updateData(newList, filmsAdapter)
    }

    // загрузка страницы
    fun downloadAnyPage(): ArrayList<Film> {
        // add not first page
        val addedList = repository.nextPage()
        val newList = arrayListOf<Film>()
        newList.addAll(filmsAdapter.getItems())
        newList.addAll(addedList)
        return newList
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