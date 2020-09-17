package com.example.loginchurrasco.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.loginchurrasco.aplication.MyApplication
import com.example.loginchurrasco.data.models.Site
import com.example.loginchurrasco.databinding.FragmentDetailsBinding
import com.example.loginchurrasco.helpers.LocationHelper
import com.example.loginchurrasco.ui.customs.BaseFragment

const val ARG_SITE = "site"

/**
 * Fragment que muestra los datos de los sitios
 * @author Axel Sanchez
 */
class DetailsFragment : BaseFragment() {
    override fun onBackPressFragment() = false

    private var fragmentDetailsBinding: FragmentDetailsBinding? = null
    private val binding get() = fragmentDetailsBinding!!

    var site: Site? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            site = it.getParcelable(ARG_SITE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentDetailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        site?.let { site ->
            binding.nombre.text = site.nombre
            binding.description.text = site.descripcion
            Glide.with(context?.let { context -> context } ?: MyApplication().applicationContext)
                .load(site.url_imagen)
                .into(binding.image)

            var ubicacionStr = site.ubicacion.toString().let { it.substring(1, it.length - 1) }

            var ubicacion = LocationHelper.getUbicacion(ubicacionStr)

            if(ubicacion._lat != "" && ubicacion._long != ""){
                binding.btnUbicacion.visibility = View.VISIBLE
                binding.btnUbicacion.setOnClickListener {
                    println("latitud: ${ubicacion._lat}")
                    println("longitud: ${ubicacion._long}")
                    val intent = Intent(context, MapsActivity::class.java).apply {
                        putExtra("lat", ubicacion._lat)
                        putExtra("lon", ubicacion._long)
                    }
                    startActivity(intent)
                }
            } else{
                binding.btnUbicacion.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentDetailsBinding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(site: Site) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SITE, site)
                }
            }
    }
}