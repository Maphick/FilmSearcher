package com.makashovadev.filmsearcher.view.fragments

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.makashovadev.filmsearcher.BuildConfig
import com.makashovadev.filmsearcher.R
import com.makashovadev.filmsearcher.data.Entity.ApiConstants
import com.makashovadev.filmsearcher.data.Entity.Film
import com.makashovadev.filmsearcher.databinding.FragmentDetailsBinding
import com.makashovadev.filmsearcher.viewmodel.DetailsFragmentViewModel
import com.makashovadev.filmsearcher.viewmodel.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {
    private lateinit var detailsBinding: FragmentDetailsBinding
    private lateinit var details_toolbar: Toolbar
    private lateinit var details_poster: ImageView
    private lateinit var details_description: TextView
    private lateinit var details_fab_favorites: FloatingActionButton
    private lateinit var details_fab_share: FloatingActionButton
    private lateinit var details_fab_download: FloatingActionButton

    private lateinit var progressBar: ProgressBar

    private lateinit var currentFilm: Film

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DetailsFragmentViewModel::class.java)
    }

    // скоуп для нашей Корутины с загрузкой постера:
    private val scope = CoroutineScope(Dispatchers.IO)


    // загрузка постера
    private fun performAsyncLoadOfPoster() {
        //Проверяем есть ли разрешение
        if (!checkPermission()) {
            //Если нет, то запрашиваем и выходим из метода
            requestPermission()
            return
        }
        //Создаем родительский скоуп с диспатчером Main потока, так как будем взаимодействовать с UI
        MainScope().launch {
            //Включаем Прогресс-бар
            progressBar.isVisible = true
            //Создаем через async, так как нам нужен результат от работы, то есть Bitmap
            val job = scope.async {
                viewModel.loadWallpaper(ApiConstants.IMAGES_URL + "original" + currentFilm.poster)
            }
            //Сохраняем в галерею, как только файл загрузится

            job.await()?.let {
                saveToGallery(it)
                //Выводим снекбар с кнопкой перейти в галерею
                Snackbar.make(
                    detailsBinding.root,
                    R.string.downloaded_to_gallery,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.open) {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.type = "image/*"
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
            }

            //Отключаем Прогресс-бар
            progressBar.isVisible = false
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = Init()
        return view
    }


    //  инициализация вьюшек в этом фрагменте
    fun Init(): View? {
        detailsBinding = FragmentDetailsBinding.inflate(layoutInflater)
        val view = detailsBinding.root
        // инициализация вьюшек
        InitViews()
        // получение данных о фильме из бандла
        Extras()
        // обработка плавающих кнопок
        InitFABs()



        return view
    }

    fun InitViews() {
        details_toolbar = detailsBinding.detailsToolbar
        details_poster = detailsBinding.detailsPoster
        details_description = detailsBinding.nestedScrollView.detailsDescription
        details_fab_favorites = detailsBinding.detailsFabFavorites
        details_fab_share = detailsBinding.detailsFabShare
        details_fab_download = detailsBinding.detailsFabDownloadWp
        progressBar = detailsBinding.progressBar
    }

    // обработка плавающих кнопок
    fun InitFABs() {
        // ИЗБРАННОЕ
        // устанавливаем иконку на кнопку "Избранное" в зависимости от того, добавлен ли фильм в избранное
        details_fab_favorites.setImageResource(
            if (currentFilm.isInFavorites) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        )
        details_fab_favorites.setOnClickListener {
            if (!currentFilm.isInFavorites) {
                details_fab_favorites.setImageResource(R.drawable.ic_baseline_favorite_24)
                currentFilm.isInFavorites = true
            } else {
                details_fab_favorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                currentFilm.isInFavorites = false
            }
        }
        // ПОДЕЛИТЬСЯ
        //
        details_fab_share.setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this film: \n\n ${currentFilm.title} \n\n ${currentFilm.description}"
            )
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
        // ЗАГРУЗКА КАРТИНКИ
        //
        // подписываемся  на  ошибку получения данных с сервера
        viewModel.errorEvent.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }
        details_fab_download.setOnClickListener {
            performAsyncLoadOfPoster()
        }
    }

    //Получаем наш фильм из переданного бандла
    fun Extras() {
        //val film = intent.extras?.get("film") as Film
        val film = arguments?.get("film") as Film
        //Устанавливаем заголовок
        details_toolbar.title = film.title
        // загрузку картинки, когда мы создаем фрагмент с деталями фильма в DetailsFragment:
        Glide.with(this)
            .load(ApiConstants.IMAGES_URL + "w780" + film.poster)
            .centerCrop()
            .into(details_poster)
        //Устанавливаем описание
        details_description.text = film.description
        currentFilm = film
    }

    //Узнаем, было ли получено разрешение ранее
    private fun checkPermission(): Boolean {
        // для новых версий андройд
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager()
        }
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    //Запрашиваем разрешение
    private fun requestPermission() {
        //  для андройд > 11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                startActivity(intent)
            } catch (ex: Exception) {
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }


            /*ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE),
                1
            )*/
        }
        else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    // логика сохранения в галерею
    private fun saveToGallery(bitmap: Bitmap) {
        //Проверяем версию системы
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //Создаем объект для передачи данных
            val contentValues = ContentValues().apply {
                //Составляем информацию для файла (имя, тип, дата создания, куда сохранять и т.д.)
                put(
                    MediaStore.Images.Media.TITLE,
                    currentFilm.title.handleSingleQuote())
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    currentFilm.title.handleSingleQuote()
                )
                put(
                    MediaStore.Images.Media.MIME_TYPE,
                    "image/jpeg"
                )
                put(
                    MediaStore.Images.Media.DATE_ADDED,
                    System.currentTimeMillis() / 1000
                )
                put(
                    MediaStore.Images.Media.DATE_TAKEN,
                    System.currentTimeMillis()
                )
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    "Pictures/FilmsSearchApp"
                )
            }
            //Получаем ссылку на объект Content resolver, который помогает передавать информацию из
            // приложения вовне
            val contentResolver = requireActivity().contentResolver
            val uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            //Открываем канал для записи на диск
            val outputStream = contentResolver.openOutputStream(uri!!)
            //Передаем нашу картинку, может сделать компрессию
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            //Закрываем поток
            outputStream?.close()
        } else {
            //То же, но для более старых версий ОС
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                bitmap,
                currentFilm.title.handleSingleQuote(),
                currentFilm.description.handleSingleQuote()
            )
        }
    }

    // кастомный метод для того, чтобы убирать кавычки. Так как кавычки в названии фильма или
    // описании влияют на составление пути и не лучшим образом, поэтому мы их убираем вот так:
    private fun String.handleSingleQuote(): String {
       return this.replace("'", "")
    }



}