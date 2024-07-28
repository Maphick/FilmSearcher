package com.makashovadev.filmsearcher.diff_util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.makashovadev.filmsearcher.data.dto.Film
import com.makashovadev.filmsearcher.presentation.FilmListRecyclerAdapter

class FilmDiff(val oldList: ArrayList<Film>, val newList: ArrayList<Film>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    //Элементы одинаковые
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val a = oldList[oldItemPosition].hashCode()
        val b = newList[newItemPosition].hashCode()
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    //Содержимое одинаковое
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return ((oldProduct.title == newProduct.title) &&
                (oldProduct.description == newProduct.description) &&
                (oldProduct.poster == newProduct.poster))
    }
}

// Оповещение RecyclerView об изменении данных с помощью DiffUtil.
@SuppressLint("NotifyDataSetChanged")
fun updateData(newList: java.util.ArrayList<Film>, adapter: FilmListRecyclerAdapter) {
    val oldList: java.util.ArrayList<Film> = adapter.getItems() as java.util.ArrayList<Film>
    val diff = FilmDiff(oldList, newList)
    val diffResult = DiffUtil.calculateDiff(diff)
    adapter.setItems(newList)
    diffResult.dispatchUpdatesTo(adapter)
}