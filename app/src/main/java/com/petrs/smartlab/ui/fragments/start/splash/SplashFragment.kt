package com.petrs.smartlab.ui.fragments.start.splash

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.petrs.smartlab.R
import com.petrs.smartlab.databinding.FragmentSplashBinding
import com.petrs.smartlab.domain.DomainResult
import com.petrs.smartlab.ui.activities.main.MainActivity
import com.petrs.smartlab.ui.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>(
    FragmentSplashBinding::inflate
) {

    override val viewModel: SplashViewModel by viewModel()

    override fun initView() {
        lifecycleScope.launch {
            delay(2000)
            viewModel.checkOnboardingStatus()
        }
    }

    override fun observeViewModel() {
        viewModel.apply {
            onboardingStatus.observe { result ->
                when (result) {
                    is DomainResult.Error -> {}
                    is DomainResult.Loading -> {}
                    is DomainResult.Success -> {
                        if (!result.data) {
                            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnboardingFragment())
                        } else {
                            checkToken()
                        }
                    }
                }
            }
            token.observe { domainResult ->
                when (domainResult) {
                    is DomainResult.Success -> {
                        if (domainResult.data.isNotEmpty()) {
                            checkPassword()
                        } else {
                            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                        }
                    }
                    is DomainResult.Error -> {}
                    is DomainResult.Loading -> {}
                }
            }
            password.observe { domainResult ->
                when (domainResult) {
                    is DomainResult.Success -> {
                        if (domainResult.data.isNotEmpty()) {
                            startMainActivity()
                        } else {
                            findNavController().navigate(R.id.action_splashFragment_to_createPasswordFragment)
                        }
                    }
                    is DomainResult.Error -> {}
                    is DomainResult.Loading -> {}
                }
            }
        }
    }

    private fun startMainActivity() {
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
    }
}