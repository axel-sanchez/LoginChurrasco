package com.example.loginchurrasco.ui

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.example.loginchurrasco.data.TableSite
import com.example.loginchurrasco.data.models.Detalle
import com.example.loginchurrasco.data.models.Site
import com.example.loginchurrasco.data.repository.GenericRepository
import com.example.loginchurrasco.databinding.FragmentSiteNewBinding
import com.example.loginchurrasco.ui.customs.BaseFragment
import com.example.loginchurrasco.ui.interfaces.INavigationHost
import com.example.loginchurrasco.viewmodel.SiteNewViewModel
import com.example.loginchurrasco.viewmodel.SiteNewViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

private const val CODE_CAMERA = 10
private const val REQUEST_CAMERA = 20
private const val MY_PERMISSIONS_REQUEST_LOCATION = 5254

@RequiresApi(Build.VERSION_CODES.N)
/** Fragment para agregar un site
 * @author Axel Sanchez
 */
class SiteNewFragment : BaseFragment() {

    private lateinit var locationManager: LocationManager
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var nombre = ""
    private var description = ""
    private var lastId = 0

    private var imagenBase64 = ""

    private val repository: GenericRepository by inject()

    private var fragmentSiteNewBinding: FragmentSiteNewBinding? = null
    private val binding get() = fragmentSiteNewBinding!!

    private val viewModelFactory: SiteNewViewModelFactory by inject()
    private val viewModel: SiteNewViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(SiteNewViewModel::class.java)
    }

    private lateinit var token: String

    private val locationListener: LocationListener = object : LocationListener {
        @SuppressLint("SetTextI18n")
        override fun onLocationChanged(location: Location) {
            latitude = location.latitude
            longitude = location.longitude
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSiteNewBinding = FragmentSiteNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        toggleUpdates()

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        token = sharedPref?.getString("preference_token", "") ?: ""

        binding.cardCamara.setOnClickListener {
            if (validatePermission()) takePicture()
        }

        binding.btnRegister.setOnClickListener {
            nombre = binding.name.text.toString()
            description = binding.descripcion.text.toString()
            if (nombre.isNotEmpty() && description.isNotEmpty() && imagenBase64.isNotEmpty() && latitude != 0.0 && longitude != 0.0) {

                lastId = repository.getSite(null, null, TableSite.Columns.COLUMN_NAME_ID).last().id

                val obj = JSONObject()
                obj.put("id", lastId + 1)
                obj.put("nombre", nombre)
                obj.put("ubicacion", JSONObject().apply {
                    put("_long", latitude)
                    put("_lat", longitude)
                })
                obj.put("descripcion", description)
                obj.put("image", imagenBase64)

                lifecycleScope.launch {
                    viewModel.createSite(token, obj)
                }
            } else Toast.makeText(
                context,
                "Asegurese de completar todos los campos",
                Toast.LENGTH_SHORT
            ).show()
        }

        val myObserver = Observer<String?> {
            repository.insert(
                Site(
                    lastId + 1,
                    description,
                    Detalle("", "", ""),
                    it,
                    nombre,
                    nombre,
                    null,
                    it
                ), latitude.toString(), longitude.toString()
            )
            (activity as INavigationHost).finish()
        }
        viewModel.getUrlLiveData().observe(viewLifecycleOwner, myObserver)
    }

    private fun toggleUpdates() {
        if (!checkLocation()) return

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                10.0f,
                locationListener
            )
        }
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun showAlert() {
        val dialog: androidx.appcompat.app.AlertDialog.Builder =
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
        dialog.setTitle("Enable Location")
            .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación usa esta app")
            .setPositiveButton("Configuración de ubicación") { _, _ ->
                var myIntent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancelar") { _, _ -> }
        dialog.show()
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun validatePermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true

        if (activity?.checkSelfPermission(CAMERA) === PackageManager.PERMISSION_GRANTED &&
            activity?.checkSelfPermission(WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        if (shouldShowRequestPermissionRationale(CAMERA) ||
            shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)
        ) {
            openDialogPermission()
        } else {
            requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE, CAMERA), REQUEST_CAMERA)
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CAMERA -> {
                if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    takePicture()
                }
            }

            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            10.0f,
                            locationListener
                        )
                        return
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    private fun openDialogPermission() {
        val dialogo = AlertDialog.Builder(context)
        dialogo.setTitle("Permisos Desactivados")
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App")

        dialogo.setPositiveButton("Aceptar") { _, _ ->
            requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE, CAMERA), REQUEST_CAMERA)
        }
        dialogo.show()
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, CODE_CAMERA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_CAMERA && resultCode == AppCompatActivity.RESULT_OK) {
            binding.cardCamara.visibility = View.GONE
            val extras = data?.extras
            val imgBitmap = extras!!["data"] as Bitmap?

            val imageScaled = Bitmap.createScaledBitmap(imgBitmap!!, 200, 400, false)

            binding.image.setImageBitmap(imageScaled)

            var byte = bitmapToMap(imgBitmap)
            saveImage(byte)

            imagenBase64 = byteToBase(byte)

        }
    }

    private fun bitmapToMap(bitmap: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun byteToBase(byte: ByteArray): String {
        return Base64.encodeToString(byte, Base64.DEFAULT)
    }

    private fun saveImage(byte: ByteArray) {
        try {
            val outputStream: FileOutputStream =
                requireContext().openFileOutput(
                    "loginChurrasco_${System.currentTimeMillis()}",
                    Context.MODE_PRIVATE
                )
            outputStream.write(byte)
            outputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e2: IOException) {
            e2.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSiteNewBinding = null
    }

    override fun onBackPressFragment() = false
}