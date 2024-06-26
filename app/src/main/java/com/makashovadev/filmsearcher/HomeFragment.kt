package com.makashovadev.filmsearcher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.makashovadev.filmsearcher.data.dto.Film
import com.makashovadev.filmsearcher.data.repository.Repository
import com.makashovadev.filmsearcher.databinding.FragmentHomeBinding
import com.makashovadev.filmsearcher.decorator.PaginationLoadingDecoration
import com.makashovadev.filmsearcher.decorator.TopSpacingItemDecoration
import com.makashovadev.filmsearcher.diff_util.updateData
import com.makashovadev.filmsearcher.presentation.FilmListRecyclerAdapter
import com.makashovadev.filmsearcher.touch_helper.SimpleItemTouchHelperCallback

class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    private val repository = Repository()
    private lateinit var container: LinearLayout
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var imageView4: ImageView
    private lateinit var imageView5: ImageView
    private lateinit var main_recycler: RecyclerView
    private lateinit var filmsAdapter: FilmListRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = Init()
        return view
    }


    fun Init(): View? {
        homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        val view = homeBinding.root
        initImages()
        initRV()
        RecyclerViewSetScroollListener()
        setUpperPosterAnimator()
        return view
    }


    // инициализация всех компонентов
    fun initImages() {
        container = homeBinding.container
        imageView1 = homeBinding.imageView1
        imageView2 = homeBinding.imageView2
        imageView3 = homeBinding.imageView3
        imageView4 = homeBinding.imageView4
        imageView5 = homeBinding.imageView5
    }

    // инициализация Recycler View
    // и применение настроек
    fun initRV() {
        //находим наш RV
        main_recycler = homeBinding.mainRecycler
        main_recycler.apply {
            //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
            //оставим его пока пустым, он нам понадобится во второй части задания

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

}


