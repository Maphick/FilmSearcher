package com.makashovadev.filmsearcher.view.rv_viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makashovadev.filmsearcher.databinding.FilmItemBinding
import com.makashovadev.filmsearcher.domain.Film

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
        //Устанавливаем рэйтинг
        filmBinding.ratingDonut.setProgress((film.rating * 10).toInt())
    }
}