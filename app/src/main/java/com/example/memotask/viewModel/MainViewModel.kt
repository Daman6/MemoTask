package com.example.memotask.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(var context: Application) : AndroidViewModel(context) {

    val _phoneNum = MutableLiveData("")


    fun checkUserPhone():Boolean{
        if (_phoneNum.value.toString().isNotEmpty()) {
            if (_phoneNum.value!!.length > 9) {
                return true
            } else {
                Toast.makeText(context, "Phone No. is less than 10", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Please Enter a Phone No.", Toast.LENGTH_SHORT).show()

        }
        return false
    }


}
