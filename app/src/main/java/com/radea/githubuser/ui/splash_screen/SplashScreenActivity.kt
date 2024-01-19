package com.radea.githubuser.ui.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.radea.githubuser.R
import com.radea.githubuser.databinding.ActivitySplashScreenBinding
import com.radea.githubuser.ui.home.MainViewModelFactory
import com.radea.githubuser.ui.home.MainActivity
import com.radea.githubuser.ui.home.MainViewModel
import com.radea.githubuser.utils.SettingPreferences
import com.radea.githubuser.utils.dataStore

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.dark))
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, MainViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                setStatusBarColor(ContextCompat.getColor(this, R.color.dark))
                binding.ivLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.github_light))
                binding.tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.splashLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark))
            } else {
                setStatusBarColor(ContextCompat.getColor(this, R.color.white))
                binding.ivLogo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.github_dark))
                binding.tvTitle.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.splashLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, DELAY_SCREEN)
    }

    private fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    companion object {
        const val DELAY_SCREEN = 5000L
    }
}