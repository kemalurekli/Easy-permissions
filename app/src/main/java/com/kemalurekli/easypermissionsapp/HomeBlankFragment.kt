package com.kemalurekli.easypermissionsapp

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.kemalurekli.easypermissionsapp.databinding.FragmentHomeBlankBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class HomeBlankFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentHomeBlankBinding? = null
    private val binding get() = _binding!!
    private val PERMISSION_LOCATION_REQUEST_CODE = 1
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBlankBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewVisibility()
        if (hasLocationPermission()){
            getUserLocation()
        }
    }
    // If user deny permissions 2 times other dialog message is shown.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    //this function return true or false depends on permissions
    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    // Get permission from user.
    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application cannot work without Location Permission.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
    // if user denied permission.
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(),perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission()
        }
    }
    // if user granted permission.
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permission Granted!", Toast.LENGTH_SHORT).show()
        setViewVisibility()
        println("ilk izin verildi.")
        getUserLocation()
    }
    private fun setViewVisibility() {
        if (hasLocationPermission()) {
            binding.tvAllowed.visibility = View.VISIBLE
            binding.btnGoToLocationFragment.visibility = View.GONE
            binding.btnGetPer.visibility = View.GONE
            binding.tvGetyourlocation.visibility = View.GONE

        } else {
            binding.tvAllowed.visibility = View.GONE
            binding.btnGetPer.visibility = View.VISIBLE
            binding.btnGoToLocationFragment.visibility = View.GONE
            binding.tvGetyourlocation.visibility = View.GONE
        }
        binding.btnGetPer.setOnClickListener {
            requestLocationPermission()
        }
    }
    private fun getUserLocation() {
        locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        binding.tvGetyourlocation.visibility = View.VISIBLE
        binding.btnGoToLocationFragment.visibility = View.GONE
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (location.provider.toString()=="gps"){
                    binding.btnGoToLocationFragment.visibility = View.VISIBLE
                    binding.tvGetyourlocation.visibility = View.GONE
                }
                binding.btnGoToLocationFragment.setOnClickListener {
                    Navigation.findNavController(it).navigate(HomeBlankFragmentDirections.actionHomeBlankFragmentToMapsFragment("${location.longitude.toString()}","${location.latitude.toString()}"))
                }
            }
            // it's for just disable some crashed.
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                super.onStatusChanged(provider, status, extras)
            }
        }
        //this condition is just for fixing permission error from android studio.
        if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10f,locationListener)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}