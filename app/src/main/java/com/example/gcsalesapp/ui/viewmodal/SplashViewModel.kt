package com.example.gcsalesapp.ui.viewmodal

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gcsalesapp.repository.PrefrenceRepository
import com.example.gcsalesapp.repository.ValidateRepository
import kotlinx.coroutines.launch

class SplashViewModel @ViewModelInject constructor(
    private val prefrenceRepository: PrefrenceRepository
) : ViewModel() {

    private var isLogin: Boolean? = null


    public fun getLoginStatus(): Boolean {
        return isLogin ?: false
    }


    public fun checkLoginStatus() {
        viewModelScope.launch {
            isLogin = prefrenceRepository.isLogin()
        }
    }

}