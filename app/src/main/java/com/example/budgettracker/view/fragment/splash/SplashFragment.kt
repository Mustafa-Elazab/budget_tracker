package com.example.budgettracker.view.fragment.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onInit()
    }

    private fun onInit() {

        lifecycleScope.launchWhenResumed {
            delay(1000)
            findNavController().navigate(
                R.id.action_splashFragment_to_homeFragment, null, NavOptions.Builder().setPopUpTo(
                    R.id.splashFragment, true
                ).build()
            )

        }

    }

}