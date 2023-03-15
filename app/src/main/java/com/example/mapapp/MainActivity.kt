package com.example.mapapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mapapp.databinding.ActivityMainBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("2ba9190a-03d8-47a7-a391-4983fe6eecf8")
        MapKitFactory.initialize(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        binding.mapView.map.move(CameraPosition(Point(58.0, 31.2), 11.0f, 0.0f, 0.0f),
        Animation(Animation.Type.SMOOTH, 300f), null)
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
}