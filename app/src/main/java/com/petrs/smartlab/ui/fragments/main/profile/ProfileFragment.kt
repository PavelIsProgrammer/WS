package com.petrs.smartlab.ui.fragments.main.profile

import com.petrs.smartlab.databinding.FragmentProfileBinding
import com.petrs.smartlab.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(
    FragmentProfileBinding::inflate
) {
    override val viewModel: ProfileViewModel by viewModel()

    override fun initView() {}

    override fun observeViewModel() {}
}