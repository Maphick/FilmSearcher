package com.makashovadev.filmsearcher.fragments


import android.annotation.SuppressLint
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.transition.Scene
import android.transition.Slide
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.makashovadev.filmsearcher.AnimationHelper
import com.makashovadev.filmsearcher.MainActivity
import com.makashovadev.filmsearcher.R
import com.makashovadev.filmsearcher.data.dto.Film
import com.makashovadev.filmsearcher.data.repository.Repository
import com.makashovadev.filmsearcher.databinding.FragmentHomeBinding
import com.makashovadev.filmsearcher.databinding.MergeHomeScreenContentBinding
import com.makashovadev.filmsearcher.decorator.PaginationLoadingDecoration
import com.makashovadev.filmsearcher.decorator.TopSpacingItemDecoration
import com.makashovadev.filmsearcher.diff_util.updateData
import com.makashovadev.filmsearcher.presentation.FilmListRecyclerAdapter
import com.makashovadev.filmsearcher.touch_helper.SimpleItemTouchHelperCallback
import java.util.Locale


class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null
    private val myIncludeLayoutBinding: MergeHomeScreenContentBinding get() = _myIncludeLayoutBinding!!
    private var _myIncludeLayoutBinding: MergeHomeScreenContentBinding? = null

    private lateinit var homeFragmentRoot: ConstraintLayout

    private val repository = Repository()
    private lateinit var mainRecycler: RecyclerView
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private lateinit var searchView: SearchView
    private lateinit var mAdapter: SimpleCursorAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _myIncludeLayoutBinding = MergeHomeScreenContentBinding.bind(binding.root)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // myIncludeLayoutBinding.button.setOnClickListener{

        //}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _myIncludeLayoutBinding = null
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragmentRoot = binding.homeFragmentRoot
        //находим наш RV
        initRV()
        //RecyclerViewSetScroollListener()
        InitMAdapter()
        initSearchView()
        //Кладем нашу БД в
        downloadAllPages()
        //filmsAdapter.addItems(repository.filmsDataBase)
        //makeAnimation()

        AnimationHelper.performFragmentCircularRevealAnimation(homeFragmentRoot, requireActivity(), 1)



    }

    fun makeAnimation()
    {
        val scene = Scene.getSceneForLayout(homeFragmentRoot, R.layout.merge_home_screen_content, requireContext())
        //Создаем анимацию выезда поля поиска сверху
        //val searchSlide = Slide(Gravity.BOTTOM).addTarget(searchView)
        //Создаем анимацию выезда RV снизу
        val recyclerSlide = Slide(Gravity.TOP).addTarget(mainRecycler)
        //Создаем экземпляр TransitionSet, который объединит все наши анимации
        val customTransition = TransitionSet().apply {
            //Устанавливаем время, за которое будет проходить анимация
            duration = 500
            //Добавляем сами анимации
            addTransition(recyclerSlide)
           // addTransition(searchSlide)
        }
//Также запускаем через TransitionManager, но вторым параметром передаем нашу кастомную анимацию
        TransitionManager.go(scene, customTransition)
    }

    fun InitMAdapter() {
        val from = arrayOf("items")
        val to = intArrayOf(R.id.text1)
        mAdapter = SimpleCursorAdapter(
            getActivity(),
            R.layout.item,
            null,
            from, //   колонки в БД
            to, // поля в БД
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER // флаг, определяющий, как это будет существовать в БД
        )
    }

    fun initSearchView() {
        searchView = myIncludeLayoutBinding.searchView
        searchView.setOnClickListener {
            searchView.isIconified = false
        }
        //Подключаем слушателя изменений введенного текста в поиска
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //Этот метод отрабатывает на каждое изменения текста
            override fun onQueryTextChange(newText: String): Boolean {
                //Если ввод пуст то вставляем в адаптер всю БД
                if (newText.isEmpty()) {
                    filmsAdapter.addItems(repository.filmsDataBase)
                    return true
                }
                //Фильтруем список на поискк подходящих сочетаний
                val result = repository.filmsDataBase.filter {
                    //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                    it.title.toLowerCase(Locale.getDefault())
                        .contains(newText.toLowerCase(Locale.getDefault()))
                }
                //Добавляем в адаптер
                filmsAdapter.addItems(result)
                updateData(result as ArrayList, filmsAdapter)
                return true
            }
        })

    }

    // инициализация Recycler View
    // и применение настроек
    fun initRV() {
        //находим наш RV
        mainRecycler = myIncludeLayoutBinding.mainRecycler
        //main_recycler = homeBinding.homeContent.mainRecycler
        mainRecycler.apply {
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
            //addItemDecoration(paginationItemDecoration)
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
        //downloadFirstPage()
        //downloadAllPages()
    }


    //  из-за обновления адаптера вызывается скроллинг RV
    // что вызывает "подгрузку" данных
    // соответственно в адаптер попадают новые подгруженные элементы
    // что делает поиск не релеавнтным
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
                        updateData(newList, filmsAdapter)
                        isLoading = false
                        /*Handler().postDelayed({
                            // Оповещение RecyclerView об изменении данных с помощью DiffUtil.
                            updateData(newList, filmsAdapter)
                            isLoading = false
                        }, 2000)*/
                    }

                }
            }
        }
       // mainRecycler.setOnScrollListener(scrollListener)
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

    // загрузка первой страницы
    fun downloadAllPages() {
        filmsAdapter.addItems(repository.filmsDataBase)
    }

}


