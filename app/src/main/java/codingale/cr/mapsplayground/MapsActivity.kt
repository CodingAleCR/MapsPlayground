package codingale.cr.mapsplayground

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var bestLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setupMap()
    }

    private fun setupMap() {
        mMap.clear()

        // Add a marker in UPV and move the camera

        mMap.addMarker(
            MarkerOptions().position(Companion.UPV).title("Marker in UPV").icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
            )
        )

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            bestLocation = getMyLocation()

            bestLocation?.let {
                val myLocation = LatLng(it.latitude, it.longitude)
                mMap.addMarker(
                    MarkerOptions().position(myLocation).title("My Location").icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
                    )
                )
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(myLocation, 15f)
                )
            }
        } else {
            //Ask permission
            askPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                "Queremos rastearte. Muhahahaha!",
                RC_ASK_LOCATION_PERMISSION
            )
        }
    }

    private fun askPermission(permission: String, justification: String, requestCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            AlertDialog.Builder(this)
                .setTitle("Solicitud de permiso")
                .setMessage(justification)
                .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                    ActivityCompat.requestPermissions(this, arrayOf(permission, Manifest.permission.RECEIVE_SMS), requestCode)
                }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == RC_ASK_LOCATION_PERMISSION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap()
            } else {
                Toast.makeText(this, "Sin permisos de ubicación no puedo encontrarte.", Toast.LENGTH_LONG).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @SuppressLint("MissingPermission")
    fun getMyLocation() : Location? {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var myLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (myLocation == null) {
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            val provider = manager.getBestProvider(criteria, true)
            myLocation = manager.getLastKnownLocation(provider)
        }
        return myLocation
    }

    companion object {
        private const val RC_ASK_LOCATION_PERMISSION = 1
        private val UPV = LatLng(39.481106, -0.340987)
    }
}
