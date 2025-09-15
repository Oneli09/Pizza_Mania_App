package com.example.pizzamania;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class OrderTrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView textViewStatus;
    private Marker driverMarker;
    private Polyline routeLine;
    private int progress = 0; // 👈 ADDED: Declare progress

    // Simulated locations
    private static final LatLng COLOMBO_BRANCH = new LatLng(6.9271, 79.8612);
    private static final LatLng USER_LOCATION = new LatLng(6.9000, 79.8800);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        textViewStatus = findViewById(R.id.textViewStatus);

        // Setup map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startTracking();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add branch marker
        mMap.addMarker(new MarkerOptions()
                .position(COLOMBO_BRANCH)
                .title("🍕 Pizza Mania - Colombo")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Add user marker
        mMap.addMarker(new MarkerOptions()
                .position(USER_LOCATION)
                .title("🏠 Your Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        // Draw route line
        routeLine = mMap.addPolyline(new PolylineOptions()
                .add(COLOMBO_BRANCH)
                .add(USER_LOCATION)
                .color(Color.RED)  // 👈 FIXED: Color imported
                .width(5));

        // Add driver marker
        driverMarker = mMap.addMarker(new MarkerOptions()
                .position(COLOMBO_BRANCH)
                .title("🛵 Driver")
                .icon(BitmapDescriptorFactory.fromBitmap(createEmojiBitmap("🛵"))));

        // Move camera to show both points
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(COLOMBO_BRANCH, 13f));
    }

    private void startTracking() {
        final Handler handler = new Handler(); // 👈 FIXED: Handler imported
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progress < 100) {
                    progress += 10;
                    updateStatusText();
                    animateDriverMarker();
                    handler.postDelayed(this, 2000);
                }
            }
        }, 2000);
    }

    private void updateStatusText() {
        if (progress <= 30) {
            textViewStatus.setText("🚚 Preparing your order...");
        } else if (progress <= 60) {
            textViewStatus.setText("🛵 Driver on the way...");
        } else if (progress <= 90) {
            textViewStatus.setText("📍 2 mins away!");
        } else {
            textViewStatus.setText("✅ Order Delivered!");
        }
    }

    private void animateDriverMarker() {
        if (mMap == null || driverMarker == null) return;

        double lat = COLOMBO_BRANCH.latitude + (USER_LOCATION.latitude - COLOMBO_BRANCH.latitude) * (progress / 100.0);
        double lng = COLOMBO_BRANCH.longitude + (USER_LOCATION.longitude - COLOMBO_BRANCH.longitude) * (progress / 100.0);
        LatLng newPosition = new LatLng(lat, lng);
        driverMarker.setPosition(newPosition);
    }

    private Bitmap createEmojiBitmap(String emoji) {
        TextView textView = new TextView(this);
        textView.setText(emoji);
        textView.setAllCaps(false);
        textView.setTextSize(12); // Small emoji
        textView.setPadding(10, 10, 10, 10);
        textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(textView.getMeasuredWidth(), textView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        textView.draw(canvas);
        return bitmap;
    }
}