package com.example.loginchurrasco.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.loginchurrasco.R
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.hide()

        binding.logIn.setOnClickListener {
            binding.password.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.password.windowToken, 0)

            GlobalScope.launch {
                viewModel.getToken(binding.user.text.toString(), binding.password.text.toString())
            }
        }

        val tokenObserver = Observer<String?> {
            it?.let {
                if(binding.remember.isChecked){
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@Observer
                    with (sharedPref.edit()) {
                        putString(getString(R.string.preference_token), it)
                        commit()
                    }
                }
                (activity as INavigationHost).navigateTo(SitiesFragment(), false)
            }?: kotlin.run {
                Toast.makeText(context, "Datos incorrectos, intente nuevamente", Toast.LENGTH_LONG).show()
            }
        }
        viewModel.getTokenLiveData().observe(viewLifecycleOwner, tokenObserver)
    }
}