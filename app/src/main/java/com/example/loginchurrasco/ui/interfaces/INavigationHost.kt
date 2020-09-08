package com.example.loginchurrasco.ui.interfaces

/**
 * Interface de navegación
 * @author Axel Sanchez
 */
import androidx.fragment.app.Fragment

interface INavigationHost {
    fun navigateTo(fragment: Fragment, addToBackstack: Boolean)

    fun replaceTo(fragment: Fragment, addToBackstack: Boolean)

    fun finish()
}

