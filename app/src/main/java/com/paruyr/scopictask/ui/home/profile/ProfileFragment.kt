package com.paruyr.scopictask.ui.home.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.paruyr.scopictask.R
import com.paruyr.scopictask.databinding.FragmentProfileBinding
import com.paruyr.scopictask.ui.landing.LandingActivity
import com.paruyr.scopictask.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy { findNavController() }
    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        profileViewModel.setup()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            backListButton.setOnClickListener {
                profileViewModel.navigateBackToList()
            }

            logoutButton.setOnClickListener {
                profileViewModel.logoutClicked()
            }
        }

        lifecycleScope.launch {
            profileViewModel.navigation.collect { navigation ->
                when (navigation) {
                    ProfileViewModel.Navigation.List -> {
                        navController.navigate(R.id.action_profileFragment_to_listFragment)
                    }

                    ProfileViewModel.Navigation.Landing -> {
                        // Navigate to Landing
                        val intent = Intent(requireActivity(), LandingActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
        }

        lifecycleScope.launch {
            profileViewModel.welcomeUser.collect { email ->
                binding.emailText.text = email
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}