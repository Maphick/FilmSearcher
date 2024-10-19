package com.makashovadev.filmsearcher.touch_helper


import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.makashovadev.filmsearcher.domain.Film
import com.makashovadev.filmsearcher.utils.diff_util.updateData
import com.makashovadev.filmsearcher.view.rv_adapters.FilmListRecyclerAdapter
import java.util.Collections


class SimpleItemTouchHelperCallback(val adapter: FilmListRecyclerAdapter) :
    ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        //Drag & drop поддерживается
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        //Swipe поддерживается
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        //Настраиваем флаги для drag & drop и swipe жестов
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {
        val items = adapter.getItems()
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        //Меняем элементы местами с помощью метода swap
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }
        //Сообщаем об изменениях адаптеру
        //Or DiffUtil
        adapter.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        //Удаляем элемент из списка после жеста swipe
        val newList = arrayListOf<Film>()
        newList.addAll(adapter.getItems())
        newList.removeAt(viewHolder.adapterPosition)
        //DiffUtil
        updateData(newList, adapter)
        //adapter.notifyItemRemoved(viewHolder.adapterPosition);
    }

}