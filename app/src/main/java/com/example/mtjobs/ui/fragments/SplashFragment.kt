package com.example.mtjobs.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.NonNull
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.mtjobs.R
import com.example.mtjobs.databinding.FragmentSplashBinding
import com.example.mtjobs.utils.Constants.SPLASH_DISPLAY_LENGTH

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private lateinit var navController: NavController
    private lateinit var binding :FragmentSplashBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)
        navController = findNavController()

        binding.splashBtnStart.setOnClickListener { _ ->
            navController.let {
                it.popBackStack(R.id.splashFragment, true)
                it.navigate(R.id.listFragment)
            }
        }
    }
}