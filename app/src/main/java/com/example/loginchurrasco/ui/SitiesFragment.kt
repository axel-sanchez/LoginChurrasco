package com.example.loginchurrasco.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginchurrasco.R
import com.example.loginchurrasco.data.models.Site
import com.example.loginchurrasco.data.models.Sities
import com.example.loginchurrasco.databinding.FragmentSitiesBinding
import com.example.loginchurrasco.ui.adapter.SitiesAdapter
import com.example.loginchurrasco.ui.customs.BaseFragment
import com.example.loginchurrasco.ui.interfaces.INavigationHost
import com.example.loginchurrasco.viewmodel.SitiesViewModel
import com.example.loginchurrasco.viewmodel.SitiesViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

const val ARG_TOKEN = "token"

/**
 * Fragment encargado de mostrar los sitios de interés
 * @author Axel Sanchez
 */
class SitiesFragment : BaseFragment() {
    override fun onBackPressFragment() = false

    private val viewModelFactory: SitiesViewModelFactory by inject()
    private val viewModel: SitiesViewModel by lazy { ViewModelProviders.of(requireActivity(), viewModelFactory).get(SitiesViewModel::class.java) }

    private lateinit var viewAdapter: SitiesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var fragmentSitiesBinding: FragmentSitiesBinding? = null
    private val binding get() = fragmentSitiesBinding!!

    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        token = sharedPref?.getString("preference_token", "")?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSitiesBinding = FragmentSitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSitiesBinding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.menu.findItem(R.id.action_add).setOnMenuItemClickListener {
            (activity as INavigationHost).replaceTo(SiteNewFragment(), true)
            false
        }

        lifecycleScope.launch {
            viewModel.getSities(token)
        }

        val sitiesObserver = Observer<Sities?> {
            binding.loading.cancelAnimation()
            binding.loading.showView(false)
            binding.recyclerview.showView(true)
            setAdapter(it?.let { it.sites } ?: listOf())
        }
        viewModel.getSitiesLiveData().observe(viewLifecycleOwner, sitiesObserver)
    }

    private fun setAdapter(sites: List<Site>) {

        viewAdapter = SitiesAdapter(sites) { itemClick(it) }

        viewManager = GridLayoutManager(this.requireContext(), 2)

        binding.recyclerview.apply {
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

    private fun itemClick(site: Site) {
        (activity as INavigationHost).replaceTo(DetailsFragment.newInstance(site), true)
    }
}