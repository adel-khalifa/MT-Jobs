package com.example.mtjobs.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mtjobs.R
import com.example.mtjobs.repo.JobsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}