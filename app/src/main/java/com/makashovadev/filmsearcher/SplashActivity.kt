package com.makashovadev.filmsearcher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.makashovadev.filmsearcher.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var splashscreen_icon_view: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed(
            {
                // запуск главного активити с задержкой после сплеша
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                // чтобы не возвращаться на сплеш скрин
                finish()
            },
            3000
        )
        binding = ActivitySplashBinding.inflate(layoutInflater)
        splashscreen_icon_view = binding.splashscreenIconView
        setContentView(binding.root)
        startAnimation()
    }

    // запуск векторной анимации с имитацией загрузки
    fun startAnimation() {
        val arrowBackToCloseDrawable =
            AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_blink)
        splashscreen_icon_view.setImageDrawable(arrowBackToCloseDrawable)
        if (arrowBackToCloseDrawable != null) {
            arrowBackToCloseDrawable.start()
        }
    }

}