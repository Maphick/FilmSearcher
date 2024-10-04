package com.makashovadev.filmsearcher.utils.diff_util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import com.makashovadev.filmsearcher.domain.Film
import com.makashovadev.filmsearcher.view.rv_adapters.FilmListRecyclerAdapter

class FilmDiff(val oldList: List<Film>, val newList: List<Film>) :
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
fun updateData(newList: List<Film>, adapter: FilmListRecyclerAdapter) {
    val oldList: java.util.ArrayList<Film> = adapter.getItems() as java.util.ArrayList<Film>
    val diff = FilmDiff(oldList, newList)
    val diffResult = DiffUtil.calculateDiff(diff)
    oldList.addAll(newList)
    adapter.setItems(oldList)
    diffResult.dispatchUpdatesTo(adapter)
}

