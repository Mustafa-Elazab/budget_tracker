package com.example.budgettracker.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.budgettracker.R
import com.example.budgettracker.databinding.ActivityMainBinding
import com.example.budgettracker.storage.StorageViewModel
import com.example.budgettracker.utils.hideBottomNav
import com.example.budgettracker.utils.hideSystemUI
import com.example.budgettracker.utils.showBottomNav
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    lateinit var binding: ActivityMainBinding
    private val viewModel: StorageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {


       observeThemeMode()
        // checkSelectedTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavigationView()

    }

    private fun initBottomNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.findNavController()
        navController.addOnDestinationChangedListener(this)
        binding.bottomNav.setupWithNavController(navController)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        when (destination.id) {
            R.id.homeFragment, R.id.incomeFragment, R.id.expenseFragment, R.id.settingFragment
            -> showBottomNav()
            else -> hideBottomNav()
        }
    }


    private fun observeThemeMode() {
        lifecycleScope.launchWhenStarted {
            viewModel.getUIMode.collect {
                val mode = when (it) {
                    true -> AppCompatDelegate.MODE_NIGHT_YES
                    false -> AppCompatDelegate.MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}