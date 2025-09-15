package com.example.pizzamania;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class BranchesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Button buttonFindNearest;

    // Branch locations
    private static final LatLng COLOMBO_BRANCH = new LatLng(6.9271, 79.8612); // Colombo
    private static final LatLng GALLE_BRANCH = new LatLng(6.0287, 80.2210);   // Galle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);

        buttonFindNearest = findViewById(R.id.buttonFindNearest);

        // Obtain the SupportMapFragment and get notified when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set click listener for "Find Nearest" button
        buttonFindNearest.setOnClickListener(v -> {
            findNearestBranch();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add Colombo Branch Marker
        Marker colomboMarker = mMap.addMarker(new MarkerOptions()
                .position(COLOMBO_BRANCH)
                .title("🍕 Pizza Mania - Colombo")
                .snippet("Address: Colombo City\nPhone: +94 11 234 5678\nHours: 10AM - 10PM"));
        colomboMarker.setTag("Colombo");

        // Add Galle Branch Marker
        Marker galleMarker = mMap.addMarker(new MarkerOptions()
                .position(GALLE_BRANCH)
                .title("🍕 Pizza Mania - Galle")
                .snippet("Address: Galle Fort\nPhone: +94 91 234 5678\nHours: 11AM - 9PM"));
        galleMarker.setTag("Galle");

        // Move camera to center of both branches
        LatLng center = new LatLng(6.4779, 80.0411);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 8f));
    }

    private void findNearestBranch() {
        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Get last known location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        // Calculate distance to Colombo
                        float[] colomboDistance = new float[1];
                        Location.distanceBetween(
                                userLocation.latitude, userLocation.longitude,
                                COLOMBO_BRANCH.latitude, COLOMBO_BRANCH.longitude,
                                colomboDistance
                        );

                        // Calculate distance to Galle
                        float[] galleDistance = new float[1];
                        Location.distanceBetween(
                                userLocation.latitude, userLocation.longitude,
                                GALLE_BRANCH.latitude, GALLE_BRANCH.longitude,
                                galleDistance
                        );

                        // Determine nearest branch
                        String nearestBranch;
                        if (colomboDistance[0] < galleDistance[0]) {
                            nearestBranch = "Colombo";
                        } else {
                            nearestBranch = "Galle";
                        }

                        // Show result
                        Toast.makeText(this,
                                "Nearest Branch: " + nearestBranch + "\nDistance: " +
                                        String.format("%.1f km", Math.min(colomboDistance[0], galleDistance[0]) / 1000),
                                Toast.LENGTH_LONG).show();

                        // Zoom to user's location
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12f));
                    } else {
                        Toast.makeText(this, "Unable to get location. Try again.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Location failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findNearestBranch(); // Retry after permission granted
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}