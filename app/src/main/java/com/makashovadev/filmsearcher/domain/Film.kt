package com.makashovadev.filmsearcher.domain

import android.os.Parcel
import android.os.Parcelable

// класс Film, который мы кладем в интерактор
//@Parcelize
data class Film(
    //var id: Int = 0,
    val title: String,
    val poster: String, //У нас будет приходить ссылка на картинку, так что теперь это String
    val description: String,
    var rating: Double = 0.0, //Приходит не целое число с API
    var isInFavorites: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        //parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(poster)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Film> {
        override fun createFromParcel(parcel: Parcel): Film {
            return Film(parcel)
        }

        override fun newArray(size: Int): Array<Film?> {
            return arrayOfNulls(size)
        }
    }
}