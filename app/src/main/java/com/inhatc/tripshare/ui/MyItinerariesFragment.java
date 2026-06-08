package com.inhatc.tripshare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.inhatc.tripshare.CreateItineraryActivity;
import com.inhatc.tripshare.ItineraryDetailActivity;
import com.inhatc.tripshare.R;
import com.inhatc.tripshare.adapter.TripAdapter;
import com.inhatc.tripshare.data.MockData;
import com.inhatc.tripshare.databinding.FragmentMyItinerariesBinding;
import com.inhatc.tripshare.model.Trip;

public class MyItinerariesFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private FragmentMyItinerariesBinding binding;
    private TripAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMyItinerariesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new TripAdapter(this);
        binding.rvTrips.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTrips.setAdapter(adapter);
        adapter.setTrips(MockData.getMyCreatedTrips());

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    adapter.setTrips(MockData.getMyCreatedTrips());
                } else {
                    adapter.setTrips(MockData.getSavedTrips());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        binding.fabCreate.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), CreateItineraryActivity.class)));
    }

    @Override
    public void onTripClick(Trip trip) {
        Intent intent = new Intent(requireContext(), ItineraryDetailActivity.class);
        intent.putExtra(ItineraryDetailActivity.EXTRA_TRIP_ID, trip.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
