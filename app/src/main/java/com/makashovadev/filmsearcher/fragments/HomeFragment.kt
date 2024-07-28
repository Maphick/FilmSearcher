package com.makashovadev.filmsearcher.fragments


import android.annotation.SuppressLint
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
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
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.makashovadev.filmsearcher.MainActivity
import com.makashovadev.filmsearcher.R
import com.makashovadev.filmsearcher.data.dto.Film
import com.makashovadev.filmsearcher.data.repository.Repository
import com.makashovadev.filmsearcher.databinding.FragmentHomeBinding
import com.makashovadev.filmsearcher.decorator.PaginationLoadingDecoration
import com.makashovadev.filmsearcher.decorator.TopSpacingItemDecoration
import com.makashovadev.filmsearcher.diff_util.updateData
import com.makashovadev.filmsearcher.presentation.FilmListRecyclerAdapter
import com.makashovadev.filmsearcher.touch_helper.SimpleItemTouchHelperCallback
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    private val repository = Repository()
    private lateinit var main_recycler: RecyclerView
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private lateinit var searchView: SearchView
    private lateinit var mAdapter: SimpleCursorAdapter

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
        initRV()
        //RecyclerViewSetScroollListener()
        InitMAdapter()
        initSearchView()
        return view
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
        searchView = homeBinding.searchView
        searchView.setOnClickListener {
            searchView.isIconified = false
        }
        //Подключаем слушателя изменений введенного текста в поиска
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
            override fun onQueryTextSubmit(query: String?): Boolean {
                setToAdapter(query)
                return true
            }

            //Этот метод отрабатывает на каждое изменения текста
            override fun onQueryTextChange(newText: String): Boolean {
                pupulateAdapter(newText)
                //Если ввод пуст то вставляем в адаптер всю БД
                ifFieldEmpty(newText)
                return true
            }
        })
        searchView.suggestionsAdapter = mAdapter
        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            // когда выбираем предложение
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            //  когда нажимаем по предложению
            @SuppressLint("Range")
            override fun onSuggestionClick(position: Int): Boolean {
                //  получить айтем адаптера по его позиции
                val cursor: Cursor = mAdapter.getItem(position) as Cursor
                // получаем строку из БД в колонке "items"
                val txt: String = cursor.getString(cursor.getColumnIndex("items"))
                // устанавливаем текст в searchView
                //searchView.setQuery(txt, true)
                searchView.clearFocus()
                cursor.close()
                searchView.setQuery(txt, true)
                //setToAdapter(txt)
                return true
            }
        })
    }

    fun setToAdapter(newText: String?): Boolean {
        //Если ввод пуст то вставляем в адаптер всю БД
        ifFieldEmpty(newText)
        //Фильтруем список на поиск подходящих сочетаний
        val result = repository.filmsDataBase.filter {
            //Чтобы все работало правильно, нужно и запрос, и имя фильма приводить к нижнему регистру
            it.title.toLowerCase(Locale.getDefault())
                .contains(newText?.toLowerCase(Locale.getDefault()) ?: "")
        }
        //Добавляем в адаптер
        filmsAdapter.addItems(result)
        return true
    }

    fun ifFieldEmpty(newText: String?): Boolean {
        //Если ввод пуст то вставляем в адаптер всю БД
        if (newText != null) {
            if (newText.isEmpty()) {
                filmsAdapter.addItems(repository.filmsDataBase)
                return true
            }
        }
        return false
    }

    //   ф-я берет текст из поля ввода, сравнивает с БД и наполняет адаптер
    private fun pupulateAdapter(query: String) {
        val c = MatrixCursor(arrayOf(BaseColumns._ID, "items"))
        for (i in repository.filmsDataBase.indices) {
            if (repository.filmsDataBase[i].title.toLowerCase().contains(query.toLowerCase())) {
                c.addRow(arrayOf(i, repository.filmsDataBase[i].title))
            }
        }
        mAdapter.changeCursor(c)
    }

    // инициализация Recycler View
    // и применение настроек
    fun initRV() {
        //находим наш RV
        main_recycler = homeBinding.mainRecycler
        //main_recycler = homeBinding.homeContent.mainRecycler
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
        downloadFirstPage()
    }


    //  из-за обновления адаптера вызывается скроллинг RV
    // что вызывает "подгрузку" данных
    // соответственно в адаптер попадают новые подгруженные элементы
    // что делает поиск не релеавнтным
    /*
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
        main_recycler.setOnScrollListener(scrollListener)
    }
    */

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


