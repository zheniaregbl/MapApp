package com.example.mapapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.children
import com.example.mapapp.databinding.ActivityMainBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), UserLocationObjectListener, CameraListener, InputListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentLocation: UserLocationLayer
    private lateinit var inAnimation: android.view.animation.Animation
    private lateinit var outAnimation: android.view.animation.Animation

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("2ba9190a-03d8-47a7-a391-4983fe6eecf8")
        MapKitFactory.initialize(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inAnimation = loadAnimation(this, R.anim.alpha_in)
        outAnimation = loadAnimation(this, R.anim.alpha_out)

        hideSystemUI()
        initToggleButton()

        super.onCreate(savedInstanceState)

        binding.mapView.map.move(CameraPosition(Point(58.4, 31.2), 11.0f, 0.0f, 0.0f),
        Animation(Animation.Type.SMOOTH, 0f), null)

        binding.mapView.map.addInputListener(this)

        val style: String = "[" +
                "        {" +
                "            \"types\": \"point\"," +
                "            \"tags\": {" +
                "                \"all\": [" +
                "                    \"poi\"" +
                "                ]" +
                "            }," +
                "            \"stylers\": {" +
                "                \"visibility\": \"off\"" +
                "            }" +
                "        }" +
                "    ]"

        binding.mapView.map.setMapStyle(style)
        binding.mapView.map.addCameraListener(this)

        val mapKit = MapKitFactory.getInstance()

        currentLocation = mapKit.createUserLocationLayer(binding.mapView.mapWindow)
        currentLocation.isVisible = true
        currentLocation.setObjectListener(this)

        binding.imageFilter.setOnClickListener {
            if(binding.layoutFilter.visibility == View.VISIBLE){
                binding.layoutFilter.startAnimation(outAnimation)
                binding.layoutFilter.visibility = View.GONE
                binding.cardViewFilter.setCardBackgroundColor(Color.TRANSPARENT)
            }
            else{
                binding.cardViewFilter.setCardBackgroundColor(Color.parseColor("#1B1B1B"))
                binding.layoutFilter.visibility = View.VISIBLE
                binding.layoutFilter.startAnimation(inAnimation)
            }
        }
    }

    override fun onMapTap(map: Map, point: Point) {
        TODO("Nothing")
    }

    override fun onMapLongTap(p0: Map, point: Point) {
        Log.d("tag123", "${(point.latitude * 1000.0).roundToInt() / 1000.0} " +
                "${(point.longitude * 1000.0).roundToInt()  / 1000.0}")
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

    override fun onResume() {
        super.onResume()

        binding.navView.selectedItemId = R.id.map
    }

    override fun onObjectAdded(usersLocationView: UserLocationView) {
        currentLocation.setAnchor(
            PointF((binding.mapView.width() * 0.5).toFloat(), (binding.mapView.height() * 0.5).toFloat()),
            PointF((binding.mapView.width() * 0.5).toFloat(), (binding.mapView.height() * 0.83).toFloat())
        )

        usersLocationView.arrow.setIcon(ImageProvider.fromResource(this, R.drawable.current_location))

        val pinIcon: CompositeIcon = usersLocationView.pin.useCompositeIcon()

        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(this, R.drawable.search_user_loc),
            IconStyle().setAnchor(PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(1f)
        )

        usersLocationView.accuracyCircle.fillColor = Color.TRANSPARENT
    }

    override fun onObjectRemoved(p0: UserLocationView) {
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        finish: Boolean
    ) {
        if (finish) {
            currentLocation.resetAnchor()
        }
    }

    private fun initToggleButton() = with(binding){
        tgBtAll.setOnCheckedChangeListener { _, _ ->
            if (tgBtAll.isChecked){
                tgBtMonument.isChecked = false
                tgBtRel.isChecked = false
                tgBtPark.isChecked = false
                tgBtHotel.isChecked = false
                tgBtCafe.isChecked = false
            }
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}