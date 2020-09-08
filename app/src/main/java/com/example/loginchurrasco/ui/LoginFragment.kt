package com.example.loginchurrasco.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.loginchurrasco.databinding.FragmentLoginBinding
import com.example.loginchurrasco.ui.customs.BaseFragment
import com.example.loginchurrasco.ui.interfaces.INavigationHost
import com.example.loginchurrasco.viewmodel.LoginViewModel
import com.example.loginchurrasco.viewmodel.LoginViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * Primer fragment en mostrarse en la aplicacion, requiere de credenciales para poder iniciar sesión
 * @author Axel Sanchez
 */
class LoginFragment: BaseFragment() {
    override fun onBackPressFragment() = false

    private val viewModelFactory: LoginViewModelFactory by inject()
    private val viewModel: LoginViewModel by lazy { ViewModelProviders.of(requireActivity(), viewModelFactory).get(LoginViewModel::class.java) }

    private var fragmentLoginBinding: FragmentLoginBinding? = null
    private val binding get() = fragmentLoginBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentLoginBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logIn.setOnClickListener {
            GlobalScope.launch {
                viewModel.getToken(binding.user.text.toString(), binding.password.text.toString())
            }
        }

        val tokenObserver = Observer<String?> {
            it?.let {
                Toast.makeText(context, "Login Succesfull", Toast.LENGTH_LONG).show() }
            ?: kotlin.run {
                (activity as INavigationHost).replaceTo(SitiesFragment(), false)
            }
        }
        viewModel.getTokenLiveData().observe(viewLifecycleOwner, tokenObserver)
    }
}