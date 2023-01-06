package com.example.memotask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.memotask.R
import com.example.memotask.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel  = ViewModelProvider(this).get(MainViewModel::class.java)

    }
}