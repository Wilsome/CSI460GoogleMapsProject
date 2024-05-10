package com.example.googlemaps;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap; // Declare GoogleMap as a member variable
    private LatLng markerLocation; // Store marker location as a member variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Customize the map's appearance
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker at a specific location
        markerLocation = new LatLng(37.7749, -122.4194); // San Francisco coordinates
        MarkerOptions markerOptions = new MarkerOptions()
                .position(markerLocation)
                .title("Marker in San Francisco");
        googleMap.addMarker(markerOptions);

        // Move the camera to center around the marker
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 10));
    }

    public void submitZip(View view) {
        EditText editZip = findViewById(R.id.zipCodeInput);
        String zipCode = editZip.getText().toString();

        // Use Geocoding to get the coordinates for the ZIP code
        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipCode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                // Update the marker location
                markerLocation = new LatLng(latitude, longitude);

                // Move the map to center around the new marker location
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 10));

                // Clear existing markers and add a new marker at the updated location
                googleMap.clear();
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(markerLocation)
                        .title("Marker");
                googleMap.addMarker(markerOptions);
            } else {
                // Handle case where no address is found for the entered ZIP code
                Toast.makeText(MainActivity.this, "Invalid ZIP code", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}