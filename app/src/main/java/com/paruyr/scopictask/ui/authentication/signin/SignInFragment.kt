package com.paruyr.scopictask.ui.authentication.signin

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
import com.paruyr.scopictask.data.model.signin.SignInField
import com.paruyr.scopictask.databinding.FragmentSignInBinding
import com.paruyr.scopictask.ui.authentication.AuthAlertDialog
import com.paruyr.scopictask.ui.home.HomeActivity
import com.paruyr.scopictask.ui.welcome.WelcomeActivity
import com.paruyr.scopictask.utils.afterTextChanged
import com.paruyr.scopictask.viewmodel.SignInViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy { findNavController() }
    private val signInViewModel: SignInViewModel by viewModel()
    private val args: SignInFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInViewModel.setup()

        with(binding) {
            args.email?.let {
                emailTextField.editText?.setText(it)
            }

            signInButton.setOnClickListener {
                loading.visibility = View.VISIBLE
                signInViewModel.signInClick(
                    email = emailTextField.editText?.text.toString(),
                    password = passwordTextField.editText?.text.toString(),
                )
            }
            signUpButton.setOnClickListener {
                signInViewModel.signUpClick(
                    emailTextField.editText?.text.toString(),
                )
            }

            // disable sign in button by default
            signInButton.isEnabled = false

            emailTextField.editText?.apply {
                onFocusChangeListener =
                    View.OnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            signInViewModel.signInDataChanged(
                                emailTextField.editText?.text.toString(),
                                passwordTextField.editText?.text.toString(),
                                SignInField.EMAIL
                            )
                        } else {
                            // Clear any previous error message when the field gains focus
                            emailTextField.error = null
                        }
                    }

                afterTextChanged {
                    signInViewModel.emailDataChanged(emailTextField.editText?.text.toString())
                }
            }

            passwordTextField.editText?.apply {
                onFocusChangeListener =
                    View.OnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            signInViewModel.signInDataChanged(
                                emailTextField.editText?.text.toString(),
                                passwordTextField.editText?.text.toString(),
                                SignInField.PASSWORD
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
                    signInViewModel.passwordDataChanged(passwordTextField.editText?.text.toString())
                }
            }
        }

        lifecycleScope.launch {
            signInViewModel.navigation.collect { navigation ->
                binding.loading.visibility = View.GONE
                when (navigation) {
                    SignInViewModel.Navigation.Home -> {
                        // Navigate to Home
                        val intent = Intent(requireActivity(), HomeActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }

                    is SignInViewModel.Navigation.SignUp -> {
                        val email = navigation.email
                        // pass email to SignUpFragment in order to prefill email field
                        // Use the generated action to navigate with the argument
                        val action =
                            SignInFragmentDirections.actionSignInFragmentToSignUpFragment(email)
                        navController.navigate(action)
                    }

                    SignInViewModel.Navigation.Welcome -> {
                        // Navigate to Welcome
                        val intent = Intent(requireActivity(), WelcomeActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
        }
        lifecycleScope.launch {
            signInViewModel.showMessage.collect { message ->
                binding.loading.visibility = View.GONE
                AuthAlertDialog.showAlertDialog(
                    context = requireContext(), // Replace with your activity or fragment context
                    title = "Alert",
                    message = message
                )
            }
        }

        lifecycleScope.launch {
            signInViewModel.signInFormState.collect { signInFormState ->
                with(binding) {
                    signInButton.isEnabled = signInFormState.isDataValid

                    when (signInFormState.editedField) {
                        SignInField.EMAIL -> {
                            emailTextField.apply {
                                isErrorEnabled = signInFormState.emailError != null
                                error = signInFormState.emailError?.let { getString(it) } ?: ""
                            }
                        }

                        SignInField.PASSWORD -> {
                            passwordTextField.apply {
                                isErrorEnabled = signInFormState.passwordError != null
                                error = signInFormState.passwordError?.let { getString(it) } ?: ""
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            signInViewModel.invalidData.collect {
                binding.signInButton.isEnabled = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}