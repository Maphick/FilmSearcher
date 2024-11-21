package com.makashovadev.filmsearcher.view.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.makashovadev.filmsearcher.App
import com.makashovadev.filmsearcher.MainActivity
import com.makashovadev.filmsearcher.R
import com.makashovadev.filmsearcher.databinding.FragmentHomeBinding
import com.makashovadev.filmsearcher.databinding.MergeHomeScreenContentBinding
import com.makashovadev.filmsearcher.data.Entity.Film
import com.makashovadev.filmsearcher.di.AppComponent
import com.makashovadev.filmsearcher.utils.AnimationHelper
import com.makashovadev.filmsearcher.utils.diff_util.updateData
import com.makashovadev.filmsearcher.view.rv_adapters.FilmListRecyclerAdapter
import com.makashovadev.filmsearcher.view.rv_adapters.decorator.PaginationLoadingDecoration
import com.makashovadev.filmsearcher.view.rv_adapters.decorator.TopSpacingItemDecoration
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class HomeFragment : Fragment() {

    // Внедрение ViewModel в наш Fragment
    // В вашем Fragment мы можем внедрить ViewModelFactory через Dagger, а затем использовать ее с
    // ViewModelProvider для получения ViewModel.
    @Inject
    lateinit var viewModelFactory: HomeFragmentViewModelFactory
    lateinit var viewModel: HomeFragmentViewModel

    private val binding: FragmentHomeBinding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null

    private val myIncludeLayoutBinding: MergeHomeScreenContentBinding get() = _myIncludeLayoutBinding!!
    private var _myIncludeLayoutBinding: MergeHomeScreenContentBinding? = null

    private lateinit var homeFragmentRoot: ConstraintLayout

    private lateinit var progress_bar: ProgressBar

    private lateinit var mainRecycler: RecyclerView
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private lateinit var searchView: SearchView
    private lateinit var pullToRefresh: SwipeRefreshLayout
    private lateinit var mAdapter: SimpleCursorAdapter


    // Создадим переменную, куда будем класть нашу БД из ViewModel, чтобы у нас не сломался поиск
    private var filmsDataBase = listOf<Film>()
        //Используем backing field
        set(value) {
            //Если придет такое же значение, то мы выходим из метода
            if (field == value) return
            //Если пришло другое значение, то кладем его в переменную
            field = value
            updateData(field, filmsAdapter)
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Внедрение зависимостей
        (activity?.applicationContext as App).dagger.inject(this)
        // Использование ViewModelProvider с внедренной фабрикой.
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _myIncludeLayoutBinding = MergeHomeScreenContentBinding.bind(binding.root)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _myIncludeLayoutBinding = null
    }


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.root, requireActivity(), 1)
        homeFragmentRoot = binding.homeFragmentRoot
        //находим наш RV
        initRV()
        InitMAdapter()
        RecyclerViewSetScroollListener()
        initSearchView()
        initPullToRefresh()
        progress_bar = myIncludeLayoutBinding.progressBar
        //Кладем нашу БД в RV
        // Это должно быть сделано в фоновом потоке.
        viewModel.filmsListData
            // Обеспечивает выполнение запроса к базе данных в фоновом режиме
            .subscribeOn(Schedulers.io())
            // Обновление пользовательского интерфейса происходит в основном потоке
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                // Обновляем RecyclerView в основном потоке полученными данными
                filmsAdapter.addItems(list)
                filmsDataBase = list
            }, { error ->
                // Обработать ошибку, если таковая возникнет во время извлечения данных
                Log.e("HomeFragment", "Error fetching films", error)
            })
        // На UI мы будем получать данные о том, нужно нам показывать Прогресс бар или нет, вот так:
        viewModel.showProgressBar
            // Обеспечивает выполнение запроса к базе данных в фоновом режиме
            .subscribeOn(Schedulers.io())
            // Обновление пользовательского интерфейса происходит в основном потоке
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                progress_bar.isVisible = it
            }

    }

    private fun initPullToRefresh() {
        pullToRefresh = myIncludeLayoutBinding.pullToRefresh
        //Вешаем слушатель, чтобы вызвался pull to refresh
        pullToRefresh.setOnRefreshListener {
            //Делаем новый запрос фильмов на сервер или в БД
            viewModel.loadMovies(viewModel.currentPage)
            //Убираем крутящееся колечко
            pullToRefresh.isRefreshing = false
        }
    }

    fun InitMAdapter() {
        val from = arrayOf("items")
        val to = intArrayOf(R.id.text1)
        mAdapter = SimpleCursorAdapter(
            getActivity(), R.layout.item, null, from, //   колонки в БД
            to, // поля в БД4
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
                    updateData(filmsDataBase, filmsAdapter)
                    return true
                }
                //Фильтруем список на поискк подходящих сочетаний
                val result = filmsDataBase.filter {
                    //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
                    it.title.toLowerCase(Locale.getDefault())
                        .contains(newText.toLowerCase(Locale.getDefault()))
                }
                //Добавляем в адаптер
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
            addItemDecoration(paginationItemDecoration)
            //  Этот класс сам определит на каком элементе остановилось пролистывание и доведет до
            //  ближайшего элемента, чтобы его было полностью видно.
            val linearSnapHelper = LinearSnapHelper()
            linearSnapHelper.attachToRecyclerView(this)
            // swipe + drag&drop
            /*val callback = SimpleItemTouchHelperCallback(this.adapter as FilmListRecyclerAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(this)
             */
        }
    }


    // слушатель скролла для реализации подгрузки контента
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
                    if (visibleItemCount + firstVisibleItems + 5 >= totalItemCount) {
                        //ставим флаг, что мы попросили еще элементы
                        isLoading = true
                        //Вызывает загрузку данных в RecyclerView
                        viewModel.currentPage += 1
                        if (viewModel.currentPage <= HomeFragmentViewModel.TOTAL_PAGES) {
                            // загрузка следующей страницы
                            downloadAnyPage(viewModel.currentPage)
                            //isLoading = false
                            // задержка для демонстрации загрузки
                            //  если разблокировать - можно увидеть прогресс бар =)
                            Handler().postDelayed({
                                // Оповещение RecyclerView об изменении данных с помощью DiffUtil.
                                isLoading = false
                            }, 500)
                        }
                    }
                }
            }
        }
        mainRecycler.setOnScrollListener(scrollListener)
    }


    // загрузка страницы с номером page
    fun downloadAnyPage(page: Int) {
        viewModel.getFilms(page)
    }

    // Чтобы у нас Корутины не продолжали работать, когда наш фрагмент будет уничтожен, в
    // переопределенном методе onStop мы наш скоуп отменяем:
    override fun onStop() {
        super.onStop()
    }

}