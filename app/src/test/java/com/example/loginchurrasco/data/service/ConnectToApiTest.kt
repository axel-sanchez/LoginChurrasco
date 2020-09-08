package com.example.loginchurrasco.data.service

import org.junit.Test
import org.junit.Assert.*
import org.junit.internal.runners.JUnit38ClassRunner
import org.junit.runner.RunWith
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

@RunWith(JUnit38ClassRunner::class)
class ConnectToApiTest: KoinComponent {

    private val api: ConnectToApi by inject()

    @Test suspend fun getAuth(){
        assertNotNull(api.getAuth("login@churrasco.cc", "l0g1n2020"))
    }
}