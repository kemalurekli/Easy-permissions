package com.kemalurekli.easypermissionsapp

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.properties.Delegates

class MapsFragment : Fragment() {




    var lat : Double = 0.0
    var lon : Double = 0.0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        arguments?.let {
            lat = MapsFragmentArgs.fromBundle(it).userLat.toDouble()
            lon = MapsFragmentArgs.fromBundle(it).userLong.toDouble()

        }


    }

    private val callback = OnMapReadyCallback { googleMap ->

        var userLocation = LatLng(lat, lon)
        googleMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15f))
    }
}