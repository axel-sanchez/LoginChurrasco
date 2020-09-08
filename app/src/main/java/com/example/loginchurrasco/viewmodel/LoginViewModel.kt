package com.example.loginchurrasco.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.loginchurrasco.domain.LoginUseCase
import java.lang.Exception

/**
 * View model de [LoginFragment]
 * @author Axel Sanchez
 */
class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val listData = MutableLiveData<String?>()

    private fun setListData(token: String?) {
        listData.postValue(token)
    }

    suspend fun getToken(email: String, password: String) {
        setListData(loginUseCase.getToken(email, password))
    }

    fun getTokenLiveData(): LiveData<String?> {
        return listData
    }
}