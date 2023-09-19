package com.paruyr.scopictask.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.paruyr.scopictask.R
import com.paruyr.scopictask.databinding.ActivityHomeBinding
import com.paruyr.scopictask.ui.landing.LandingActivity
import com.paruyr.scopictask.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    private var _binding: ActivityHomeBinding? = null
    private lateinit var navController: NavController
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navController = findNavController(R.id.nav_host_fragment)
        homeViewModel.setup()

        lifecycleScope.launch {
            homeViewModel.navigation.collect { navigation ->
                when (navigation) {
                    HomeViewModel.Navigation.Landing -> {
                        // Navigate to Landing
                        val intent = Intent(this@HomeActivity, LandingActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}