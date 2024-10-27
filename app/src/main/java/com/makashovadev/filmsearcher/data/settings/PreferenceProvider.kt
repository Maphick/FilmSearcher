package com.makashovadev.filmsearcher.data.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceProvider(context: Context) {
    //Нам нужен контекст приложения
    private val appContext = context.applicationContext
    //Создаем экземпляр SharedPreferences
    private val preference: SharedPreferences = appContext.getSharedPreferences(
        "settings",
        Context.MODE_PRIVATE
    )

    init {
        //Логика для первого запуска приложения, чтобы положить наши настройки,
        //Сюда потом можно добавить и другие настройки
        if(preference.getBoolean(KEY_FIRST_LAUNCH, false)) {
            preference.edit { putString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY) }
            preference.edit { putBoolean(KEY_FIRST_LAUNCH, false) }
        }
    }

    //Category prefs
    //Сохраняем категорию
    fun saveDefaultCategory(category: String) {
        preference.edit { putString(KEY_DEFAULT_CATEGORY, category) }
    }
    //Забираем категорию
    fun getDefaultCategory(): String {
        var str = preference.getString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY) ?: DEFAULT_CATEGORY
        return str
    }


    //Сохраняем время последнего обновления
    fun saveLastUpdateTime(time: Long) {
        preference.edit { putLong(LAST_DOWNLOAD_TIME_KEY, time) }
    }

    // Забираем время последнего обновления
    fun getLastUpdateTime(): Long {
        var str = preference.getLong(LAST_DOWNLOAD_TIME_KEY, DEFAULT_DOWNLOAD_TIME) ?: DEFAULT_DOWNLOAD_TIME
        return str
    }





    //Ключи для наших настроек, по ним мы их будем получать
    companion object {
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_DEFAULT_CATEGORY = "default_category"
        private const val DEFAULT_CATEGORY = "popular"
        // ключ для хранения последней загрузки данных в шаред преференис
        private const val LAST_DOWNLOAD_TIME_KEY = "last_download_time"
        private const val DEFAULT_DOWNLOAD_TIME = 0L

    }
}