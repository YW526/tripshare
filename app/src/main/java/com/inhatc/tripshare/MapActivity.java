package com.inhatc.tripshare;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inhatc.tripshare.data.MockData;
import com.inhatc.tripshare.databinding.ActivityMapBinding;
import com.inhatc.tripshare.model.ScheduleItem;
import com.inhatc.tripshare.model.Trip;
import com.inhatc.tripshare.util.MarkerUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    public static final String EXTRA_TRIP_ID = "trip_id";

    private ActivityMapBinding binding;
    private GoogleMap googleMap;
    private Trip trip;
    private final Map<Marker, ScheduleItem> markerData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int tripId = getIntent().getIntExtra(EXTRA_TRIP_ID, 1);
        trip = MockData.getTripById(tripId);

        binding.btnBack.setOnClickListener(v -> finish());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMarkerClickListener(this);

        List<ScheduleItem> schedule = trip.getSchedule();
        LatLng center = null;

        for (int i = 0; i < schedule.size(); i++) {
            ScheduleItem item = schedule.get(i);
            LatLng position = new LatLng(item.getLatitude(), item.getLongitude());
            if (center == null) {
                center = position;
            }

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(item.getTitle())
                    .icon(MarkerUtils.createNumberedMarker(this, i + 1)));
            if (marker != null) {
                markerData.put(marker, item);
            }
        }

        if (center != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 10f));
        }

        if (!schedule.isEmpty()) {
            showSpotInfo(schedule.get(0));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ScheduleItem item = markerData.get(marker);
        if (item != null) {
            showSpotInfo(item);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12f));
        }
        return true;
    }

    private void showSpotInfo(ScheduleItem item) {
        binding.cardSpotInfo.setVisibility(View.VISIBLE);
        binding.tvSpotTitle.setText(
                getString(R.string.day_format, item.getDay()) + " - " + item.getTitle());
        binding.tvSpotTime.setText(item.getTime());
        binding.tvSpotDesc.setText(item.getDescription());
        binding.imgSpot.setBackgroundResource(trip.getImageRes());
    }
}
