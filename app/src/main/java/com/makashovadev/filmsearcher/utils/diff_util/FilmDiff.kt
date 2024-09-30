package com.makashovadev.filmsearcher.utils.diff_util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.makashovadev.filmsearcher.domain.Film
import com.makashovadev.filmsearcher.view.rv_adapters.FilmListRecyclerAdapter

class FilmDiff(val oldList: ArrayList<Film>, val newList: ArrayList<Film>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
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