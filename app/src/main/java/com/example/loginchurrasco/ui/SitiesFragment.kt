package com.example.loginchurrasco.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loginchurrasco.databinding.FragmentSitiesBinding
import com.example.loginchurrasco.ui.customs.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Fragment encargado de mostrar los sitios de interés
 * @author Axel Sanchez
 */
class SitiesFragment: BaseFragment() {
    override fun onBackPressFragment() = false

    private var fragmentSitiesBinding: FragmentSitiesBinding? = null
    private val binding get() = fragmentSitiesBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSitiesBinding = FragmentSitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSitiesBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch {

        }
    }
}