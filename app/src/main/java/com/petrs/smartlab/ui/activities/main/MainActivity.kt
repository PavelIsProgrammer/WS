package com.petrs.smartlab.ui.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.petrs.smartlab.R
import com.petrs.smartlab.databinding.ActivityMainBinding
import com.petrs.smartlab.ui.base.BaseActivity
import com.petrs.smartlab.utils.navigation.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>(
    ActivityMainBinding::inflate
) {
    override val viewModel: MainActivityViewModel by viewModel()

    override fun initView() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun observeViewModel() {
        viewModel.apply {

        }
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.analyzes,
            R.navigation.results,
            R.navigation.support,
            R.navigation.profile
        )

        binding.bottomNav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment_activity_main,
            intent = intent,
            this
        )
    }

    fun showBottomBar() {
        binding.bottomNav.isVisible = true
    }

    fun hideBottomBar() {
        binding.bottomNav.isVisible = false
    }
}