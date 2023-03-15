package com.example.mapapp

import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mapapp.databinding.ActivityMainBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider

class MainActivity : AppCompatActivity(), UserLocationObjectListener {

    lateinit var binding: ActivityMainBinding
    lateinit var locationMapkit: UserLocationLayer

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("2ba9190a-03d8-47a7-a391-4983fe6eecf8")
        MapKitFactory.initialize(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)

        binding.mapView.map.move(CameraPosition(Point(58.496226, 31.208039), 11.0f, 0.0f, 0.0f),
        Animation(Animation.Type.SMOOTH, 0f), null)

        val mapKit = MapKitFactory.getInstance()

        locationMapkit = mapKit.createUserLocationLayer(binding.mapView.mapWindow)
        locationMapkit.isVisible = true
        locationMapkit.setObjectListener(this)
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

    override fun onObjectAdded(usersLocationView: UserLocationView) {
        locationMapkit.setAnchor(
            PointF((binding.mapView.width() * 0.5).toFloat(), (binding.mapView.height() * 0.5).toFloat()),
            PointF((binding.mapView.width() * 0.5).toFloat(), (binding.mapView.height() * 0.83).toFloat())
        )

        usersLocationView.arrow.setIcon(ImageProvider.fromResource(this, R.drawable.cursor))
    }

    override fun onObjectRemoved(p0: UserLocationView) {
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }
}