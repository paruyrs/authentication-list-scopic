package com.paruyr.scopictask.ui.authentication.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.paruyr.scopictask.data.model.signup.SignUpField
import com.paruyr.scopictask.databinding.FragmentSignUpBinding
import com.paruyr.scopictask.ui.authentication.AuthAlertDialog
import com.paruyr.scopictask.ui.home.HomeActivity
import com.paruyr.scopictask.ui.welcome.WelcomeActivity
import com.paruyr.scopictask.utils.afterTextChanged
import com.paruyr.scopictask.viewmodel.SignUpViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy { findNavController() }
    private val signUpViewModel: SignUpViewModel by viewModel()
    private val args: SignUpFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpViewModel.setup()

        with(binding) {
            args.email?.let {
                emailTextField.editText?.setText(it)
            }

            signUpButton.setOnClickListener {
                loading.visibility = View.VISIBLE
                signUpViewModel.signUpClick(
                    email = emailTextField.editText?.text.toString(),
                    password = passwordTextField.editText?.text.toString(),
                )
            }
            signInButton.setOnClickListener {
                signUpViewModel.signInClick(
                    emailTextField.editText?.text.toString(),
                )
            }

            // disable sign up button by default
            signUpButton.isEnabled = false

            emailTextField.editText?.apply {
                onFocusChangeListener =
                    View.OnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            signUpViewModel.signUpDataChanged(
                                emailTextField.editText?.text.toString(),
                                passwordTextField.editText?.text.toString(),
                                SignUpField.EMAIL
                            )
                        } else {
                            // Clear any previous error message when the field gains focus
                            emailTextField.error = null
                        }
                    }

                afterTextChanged {
                    signUpViewModel.emailDataChanged(emailTextField.editText?.text.toString())
                }
            }

            passwordTextField.editText?.apply {
                onFocusChangeListener =
                    View.OnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            signUpViewModel.signUpDataChanged(
                                emailTextField.editText?.text.toString(),
                                passwordTextField.editText?.text.toString(),
                                SignUpField.PASSWORD
                            )
                        } else {
                            // Clear any previous error message when the field gains focus
                            passwordTextField.error = null
                        }
                    }

                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // Clear focus here from editText
                        passwordTextField.editText?.clearFocus()
                    }
                    false
                }

                afterTextChanged {
                    signUpViewModel.passwordDataChanged(passwordTextField.editText?.text.toString())
                }
            }
        }

        lifecycleScope.launch {
            signUpViewModel.navigation.collect { navigation ->
                binding.loading.visibility = View.GONE
                when (navigation) {
                    SignUpViewModel.Navigation.Home -> {
                        // Navigate to Home
                        val intent = Intent(requireActivity(), HomeActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }

                    is SignUpViewModel.Navigation.SignIn -> {
                        val email = navigation.email
                        // pass email to SignInFragment in order to prefill email field
                        // Use the generated action to navigate with the argument
                        val action =
                            SignUpFragmentDirections.actionSignUpFragmentToSignInFragment(email)
                        navController.navigate(action)
                    }

                    SignUpViewModel.Navigation.Welcome -> {
                        // Navigate to Welcome
                        val intent = Intent(requireActivity(), WelcomeActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
        }
        lifecycleScope.launch {
            signUpViewModel.showMessage.collect { message ->
                binding.loading.visibility = View.GONE
                AuthAlertDialog.showAlertDialog(
                    context = requireContext(), // Replace with your activity or fragment context
                    title = "Alert",
                    message = message
                )
            }
        }

        lifecycleScope.launch {
            signUpViewModel.signUpFormState.collect { signUpFormState ->
                with(binding) {
                    signUpButton.isEnabled = signUpFormState.isDataValid

                    when (signUpFormState.editedField) {
                        SignUpField.EMAIL -> {
                            emailTextField.apply {
                                isErrorEnabled = signUpFormState.emailError != null
                                error = signUpFormState.emailError?.let { getString(it) } ?: ""
                            }
                        }

                        SignUpField.PASSWORD -> {
                            passwordTextField.apply {
                                isErrorEnabled = signUpFormState.passwordError != null
                                error = signUpFormState.passwordError?.let { getString(it) } ?: ""
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            signUpViewModel.invalidData.collect {
                binding.signUpButton.isEnabled = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}