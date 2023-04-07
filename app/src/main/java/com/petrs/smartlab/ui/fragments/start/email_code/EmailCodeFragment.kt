package com.petrs.smartlab.ui.fragments.start.email_code

import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.petrs.smartlab.R
import com.petrs.smartlab.data.ErrorType
import com.petrs.smartlab.databinding.FragmentEmailCodeBinding
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.ui.base.BaseFragment
import com.petrs.smartlab.ui.base.error_dialog.ErrorDialog
import com.petrs.smartlab.ui.base.error_dialog.ErrorDialogParams
import org.koin.androidx.viewmodel.ext.android.viewModel

class EmailCodeFragment : BaseFragment<FragmentEmailCodeBinding, EmailCodeViewModel>(
    FragmentEmailCodeBinding::inflate
) {
    private val args by navArgs<EmailCodeFragmentArgs>()
    private val email by lazy { args.email }

    override val viewModel: EmailCodeViewModel by viewModel()

    override fun initView() {
        viewModel.startTimer()

        binding.apply {
            etCode1.doAfterTextChanged {
                if (!etCode1.text.isNullOrEmpty() && !etCode2.text.isNullOrEmpty() && !etCode3.text.isNullOrEmpty() && !etCode4.text.isNullOrEmpty()) {
                    viewModel.signIn(
                        email,
                        "${etCode1.text}${etCode2.text}${etCode3.text}${etCode4.text}"
                    )
                }
                etCode2.requestFocus()
            }
            etCode2.doAfterTextChanged {
                if (!etCode1.text.isNullOrEmpty() && !etCode2.text.isNullOrEmpty() && !etCode3.text.isNullOrEmpty() && !etCode4.text.isNullOrEmpty()) {
                    viewModel.signIn(
                        email,
                        "${etCode1.text}${etCode2.text}${etCode3.text}${etCode4.text}"
                    )
                }
                etCode3.requestFocus()
            }
            etCode3.doAfterTextChanged {
                if (!etCode1.text.isNullOrEmpty() && !etCode2.text.isNullOrEmpty() && !etCode3.text.isNullOrEmpty() && !etCode4.text.isNullOrEmpty()) {
                    viewModel.signIn(
                        email,
                        "${etCode1.text}${etCode2.text}${etCode3.text}${etCode4.text}"
                    )
                }
                etCode4.requestFocus()
            }
            etCode4.doAfterTextChanged {
                if (!etCode1.text.isNullOrEmpty() && !etCode2.text.isNullOrEmpty() && !etCode3.text.isNullOrEmpty() && !etCode4.text.isNullOrEmpty()) {
                    viewModel.signIn(
                        email,
                        "${etCode1.text}${etCode2.text}${etCode3.text}${etCode4.text}"
                    )
                }
                etCode4.clearFocus()
            }
            tvSendCodeAgain.setOnClickListener {
                viewModel.sendNewCode(email)
                viewModel.startTimer()
                it.isEnabled = false
            }
            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_emailCodeFragment_to_signInFragment)
            }
        }
    }

    override fun observeViewModel() {
        viewModel.apply {
            timer.observe { second ->
                if (second == 0) {
                    binding.tvSendCodeAgain.text = getString(R.string.text_send_code_again)
                    binding.tvSendCodeAgain.isEnabled = true
                } else {
                    binding.tvSendCodeAgain.text =
                        getString(R.string.text_send_code_again_with_time, second)
                }
            }

            token.observe { state ->
                when (state) {
                    is DomainResult.Success -> {
                        saveToken(state.data.token)
                    }
                    is DomainResult.Error -> {
                        val message = if (state.type == ErrorType.NO_INTERNET)
                            getString(R.string.network_error)
                        else
                            state.errors
                        val errorDialog =
                            ErrorDialog.getInstance(ErrorDialogParams(message))
                        errorDialog.show(childFragmentManager, ErrorDialog.TAG)

                        clearFields()
                    }
                    is DomainResult.Loading -> {}
                }
            }

            sendCodeToEmail.observe { state ->
                when (state) {
                    is DomainResult.Success -> {
                        startTimer()
                    }
                    is DomainResult.Error -> {
                        val message = if (state.type == ErrorType.NO_INTERNET)
                            getString(R.string.network_error)
                        else
                            state.errors
                        val errorDialog =
                            ErrorDialog.getInstance(ErrorDialogParams(message))
                        errorDialog.show(childFragmentManager, ErrorDialog.TAG)
                    }
                    is DomainResult.Loading -> {}
                }
            }

            saveToken.observe { domainResult ->
                when (domainResult) {
                    is DomainResult.Success -> {
                        findNavController().navigate(EmailCodeFragmentDirections.actionEmailCodeFragmentToCreatePasswordFragment())
                    }
                    is DomainResult.Error -> {}
                    is DomainResult.Loading -> {}
                }
            }
        }
    }

    private fun clearFields() {
        binding.apply {
            etCode1.setText("")
            etCode2.setText("")
            etCode3.setText("")
            etCode4.setText("")

            etCode1.requestFocus()
        }
    }
}