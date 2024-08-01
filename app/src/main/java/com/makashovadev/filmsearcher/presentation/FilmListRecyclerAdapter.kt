package com.makashovadev.filmsearcher.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makashovadev.filmsearcher.data.dto.Film
import com.makashovadev.filmsearcher.databinding.FilmItemBinding

//в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса Activity
class FilmListRecyclerAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var filmBinding: FilmItemBinding

    //Здесь у нас хранится список элементов для RV
    private var items = mutableListOf<Film>()

    fun getItems(): List<Film> {
        return items
    }

    fun setItems(newList: List<Film>) {
        items = newList.toMutableList()
    }

    class FilmViewHolder(private val filmBinding: FilmItemBinding) :
        RecyclerView.ViewHolder(filmBinding.root) {
        //В этом методе кладем данные из Film в наши View
        fun onBind(film: Film) {
            //Устанавливаем заголовок
            filmBinding.title.text = film.title
            //Устанавливаем постер
            //Указываем контейнер, в котором будет "жить" наша картинка
            Glide.with(itemView)
                //Загружаем сам ресурс
                .load(film.poster)
                //Центруем изображение
                .centerCrop()
                //Указываем ImageView, куда будем загружать изображение
                .into(filmBinding.poster)
            //filmBinding.poster.setImageResource(film.poster)
            //Устанавливаем описание
            filmBinding.description.text = film.description
        }
    }


    //В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашего фильма
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        filmBinding = FilmItemBinding.inflate(layoutInflater, parent, false)
        return FilmViewHolder(filmBinding)
    }


    //Этот метод нужно переопределить на возврат количества элементов в списке RV
    override fun getItemCount() = items.size


    //В этом методе будет привязка полей из объекта Film к View из film_item.xml
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Проверяем какой у нас ViewHolder
        when (holder) {
            is FilmViewHolder -> {
                //Вызываем метод bind(), который мы создали, и передаем туда объект
                //из нашей базы данных с указанием позиции
                holder.onBind(items[position])
                //Обрабатываем нажатие на весь элемент целиком(можно сделать на отдельный элемент
                //например, картинку) и вызываем метод нашего листенера, который мы получаем из
                //конструктора адаптера
                filmBinding.itemContainer.setOnClickListener {
                    clickListener.click(items[position])
                }
            }
        }
    }

    //Метод для добавления объектов в наш список
    fun addItems(list: List<Film>) {
        //Сначала очищаем(если не реализовать DiffUtils)
        items.clear()
        //Добавляем
        items.addAll(list)
        //Уведомляем RV, что пришел новый список, и ему нужно заново все "привязывать"
        notifyDataSetChanged()
    }


    //Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(film: Film)
    }
}
