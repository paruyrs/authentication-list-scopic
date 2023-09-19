package com.paruyr.scopictask.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.paruyr.scopictask.databinding.ActivityWelcomeBinding
import com.paruyr.scopictask.ui.home.HomeActivity
import com.paruyr.scopictask.ui.landing.LandingActivity
import com.paruyr.scopictask.viewmodel.WelcomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeActivity : AppCompatActivity() {

    private val welcomeViewModel: WelcomeViewModel by viewModel()

    // Define a private nullable variable for ViewBinding
    private var _binding: ActivityWelcomeBinding? = null

    // Create a non-null property for ViewBinding using a custom getter
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomeViewModel.setup()
        // Initialize ViewBinding
        _binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        val navigateToHomeButton = binding.listButton
        setContentView(view)

        navigateToHomeButton.setOnClickListener {
            welcomeViewModel.listButtonClicked()
        }

        lifecycleScope.launch {
            welcomeViewModel.navigation.collect { navigation ->
                when (navigation) {
                    WelcomeViewModel.Navigation.Home -> {
                        // Navigate to Home
                        val intent = Intent(this@WelcomeActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    WelcomeViewModel.Navigation.Landing -> {
                        // Navigate to Landing
                        val intent = Intent(this@WelcomeActivity, LandingActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        welcomeViewModel.initialized()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Set _binding to null when the activity is destroyed to avoid memory leaks
        _binding = null
    }
}