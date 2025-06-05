package dev.pseudo.testpokeapi.presentation.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import dev.pseudo.testpokeapi.R
import dev.pseudo.testpokeapi.databinding.ActivityMainBinding
import dev.pseudo.testpokeapi.presentation.viewmodel.MainViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val APP_EXIT_DELAY = 2000L
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private var lastBackPressedTime: Long = 0

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        initNavController()

        setupOnBackPressedDispatcher()

        configureWindowStyle()

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.loadPokemon()
    }

    private fun configureWindowStyle() {
        Glide.with(this)
            .load(R.drawable.bg_screen)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(10, 3)))
            .into(binding.backgroundImage)

        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            applyWindowInsets(insets)
            insets
        }
    }

    private fun applyWindowInsets(insets: WindowInsetsCompat) {
        val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        binding.navHostFragment.setPadding(
            systemBarsInsets.left,
            systemBarsInsets.top,
            systemBarsInsets.right,
            systemBarsInsets.bottom,
        )
    }

    private fun setupOnBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        })
    }

    private fun handleBackPress() {
        when (navController.currentDestination?.id) {
            R.id.navigation_pokemon_list -> {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastBackPressedTime < APP_EXIT_DELAY) {
                    finishAffinity()
                } else {
                    lastBackPressedTime = currentTime
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.app_exit_confirmation_message),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
            else -> {
                navController.navigateUp()
            }
        }
    }


    private fun initNavController() {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let { fragment ->
            navController = fragment.findNavController()
        }
    }
}