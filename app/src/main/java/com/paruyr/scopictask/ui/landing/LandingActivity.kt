package com.paruyr.scopictask.ui.landing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.paruyr.scopictask.R
import com.paruyr.scopictask.data.model.landing.LandingScreenNavigation
import com.paruyr.scopictask.ui.authentication.AuthenticationActivity
import com.paruyr.scopictask.ui.home.HomeActivity
import com.paruyr.scopictask.ui.welcome.WelcomeActivity
import com.paruyr.scopictask.viewmodel.LandingViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LandingActivity : AppCompatActivity() {

    private val landingViewModel: LandingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        lifecycleScope.launch {
            landingViewModel.navigation.collect { navigation ->
                when (navigation) {
                    LandingScreenNavigation.Authentication -> {
                        // Navigate to Authentication
                        val intent =
                            Intent(this@LandingActivity, AuthenticationActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    LandingScreenNavigation.Welcome -> {
                        // Navigate to Welcome
                        val intent = Intent(this@LandingActivity, WelcomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    LandingScreenNavigation.Home -> {
                        // Navigate to Home
                        val intent = Intent(this@LandingActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        landingViewModel.setup()
    }
}