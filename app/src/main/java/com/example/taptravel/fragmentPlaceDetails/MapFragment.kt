import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.taptravel.R
import com.mapmyindia.sdk.maps.MapView
import com.mapmyindia.sdk.maps.MapmyIndia
import com.mapmyindia.sdk.maps.MapmyIndiaMap
import com.mapmyindia.sdk.maps.OnMapReadyCallback
import com.mapmyindia.sdk.maps.camera.CameraPosition
import com.mapmyindia.sdk.maps.geometry.LatLng
import com.mmi.services.account.MapmyIndiaAccountManager

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var mapmyIndiaMap: MapmyIndiaMap
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapmyIndiaAccountManager.getInstance().apply {
            restAPIKey = "4a2a685c0f4da25a61de6c0f2867e5cf"
            mapSDKKey = "4a2a685c0f4da25a61de6c0f2867e5cf"
            atlasClientId = "96dHZVzsAuvo7tQSSt-un9MlXvbmHMsK6pgoi3C0rl1Jlv9_KHfR21CsmoAA5Q0Ps0Okc2naHJttTKp7cs4OIA=="
            atlasClientSecret = "lrFxI-iSEg9QcAmbJHRAFkCueOdPXsumQ8IgEnOorFwSeMSeinX4fDz_bQ21PxxI1tUUJVVGDCf-0EeEIzTZeK5iIRPVPnbs"
        }
        MapmyIndia.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_maps, container, false)

        // Retrieve arguments (latitude and longitude)
        arguments?.let {
            latitude = it.getDouble("latitude", 0.0)
            longitude = it.getDouble("longitude", 0.0)
        }

        // Initialize map view
        mapView = rootView.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return rootView
    }
    override fun onMapReady(map: MapmyIndiaMap) {
        mapmyIndiaMap = map

        // Move camera to the specified location
        val location = LatLng(latitude, longitude)
        mapmyIndiaMap.cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(14.0)
            .build()
        mapmyIndiaMap.addMarker(
            com.mapmyindia.sdk.maps.annotations.MarkerOptions()
                .position(location)
                .title("Selected Location")
                .snippet("Latitude: $latitude, Longitude: $longitude")
        )
    }
    override fun onMapError(errorCode: Int, errorMessage: String?) {
        val errorText = "Map Error: $errorMessage (Code: $errorCode)"
        Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}

