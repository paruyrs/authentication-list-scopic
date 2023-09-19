package com.paruyr.scopictask.ui.home.list

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.paruyr.scopictask.databinding.DialogFragmentInputBinding
import com.paruyr.scopictask.utils.Constants
import com.paruyr.scopictask.utils.Constants.ITEM_TEXT_MAX_LENGTH
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class InputDialogFragment : DialogFragment() {

    private val viewModel: InputViewModel by viewModel()
    private var _binding: DialogFragmentInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textInputLayout.editText!!.filters =
                arrayOf<InputFilter>(InputFilter.LengthFilter(ITEM_TEXT_MAX_LENGTH))
            textInputLayout.counterMaxLength = ITEM_TEXT_MAX_LENGTH
            textInputLayout.editText!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.updateCharCount(s.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            addButton.setOnClickListener {
                val userInput = textInputLayout.editText!!.text.toString()
                viewModel.addItem(userInput)
            }

            cancelButton.setOnClickListener {
                dismiss()
            }

            lifecycleScope.launch {
                viewModel.warningMessage.collect { warning ->
                    textInputLayout.helperText = warning
                }
            }

            lifecycleScope.launch {
                viewModel.input.collect { userInput ->
                    setFragmentResult(
                        Constants.DIALOG_ITEM_REQUEST_KEY,
                        bundleOf(Constants.DIALOG_ADDED_ITEM_KEY to userInput)
                    )
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}