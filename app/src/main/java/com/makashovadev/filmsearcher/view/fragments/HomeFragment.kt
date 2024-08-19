package com.makashovadev.filmsearcher.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.makashovadev.filmsearcher.utils.AnimationHelper
import com.makashovadev.filmsearcher.MainActivity
import com.makashovadev.filmsearcher.R
import com.makashovadev.filmsearcher.domain.Film
import com.makashovadev.filmsearcher.data.repository.MainRepository
import com.makashovadev.filmsearcher.databinding.FragmentHomeBinding
import com.makashovadev.filmsearcher.databinding.MergeHomeScreenContentBinding
import com.makashovadev.filmsearcher.view.rv_adapters.decorator.PaginationLoadingDecoration
import com.makashovadev.filmsearcher.view.rv_adapters.decorator.TopSpacingItemDecoration
import com.makashovadev.filmsearcher.utils.diff_util.updateData
import com.makashovadev.filmsearcher.view.rv_adapters.FilmListRecyclerAdapter
import com.makashovadev.filmsearcher.touch_helper.SimpleItemTouchHelperCallback
import com.makashovadev.filmsearcher.viewmodel.HomeFragmentViewModel
import java.util.Locale


class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null
    private val myIncludeLayoutBinding: MergeHomeScreenContentBinding get() = _myIncludeLayoutBinding!!
    private var _myIncludeLayoutBinding: MergeHomeScreenContentBinding? = null

    private lateinit var homeFragmentRoot: ConstraintLayout

    private val repository = MainRepository()
    private lateinit var mainRecycler: RecyclerView
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private lateinit var searchView: SearchView
    private lateinit var mAdapter: SimpleCursorAdapter

    // Инициализируем (лениво) ViewModel
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }

    // Создадим переменную, куда будем класть нашу БД из ViewModel, чтобы у нас не сломался поиск
    private var filmsDataBase = listOf<Film>()
        //Используем backing field
        set(value) {
            //Если придет такое же значение, то мы выходим из метода
            if (field == value) return
            //Если пришло другое значение, то кладем его в переменную
            field = value
            //Обновляем RV адаптер
            filmsAdapter.addItems(field)
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        _myIncludeLayoutBinding = MergeHomeScreenContentBinding.bind(binding.root)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.root, requireActivity(), 1)
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragmentRoot = binding.homeFragmentRoot
        //находим наш RV
        initRV()
        InitMAdapter()
        initSearchView()
        // подпишемся на изменения этой View Model
        viewModel.filmsListLiveData.observe(viewLifecycleOwner, Observer<List<Film>> {
            filmsDataBase = it
        })
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
    }

}


