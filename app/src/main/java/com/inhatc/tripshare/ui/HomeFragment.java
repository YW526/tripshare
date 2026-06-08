package com.inhatc.tripshare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.inhatc.tripshare.ItineraryDetailActivity;
import com.inhatc.tripshare.R;
import com.inhatc.tripshare.adapter.TripAdapter;
import com.inhatc.tripshare.data.MockData;
import com.inhatc.tripshare.databinding.FragmentHomeBinding;
import com.inhatc.tripshare.model.Trip;

public class HomeFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private FragmentHomeBinding binding;
    private TripAdapter popularAdapter;
    private TripAdapter latestAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        popularAdapter = new TripAdapter(this);
        latestAdapter = new TripAdapter(this);

        binding.rvPopular.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPopular.setAdapter(popularAdapter);

        binding.rvLatest.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvLatest.setAdapter(latestAdapter);

        popularAdapter.setTrips(MockData.getPopularTrips());
        latestAdapter.setTrips(MockData.getLatestTrips());

        binding.btnNotifications.setOnClickListener(v ->
                Toast.makeText(requireContext(), R.string.notifications_coming, Toast.LENGTH_SHORT).show());

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                latestAdapter.setTrips(MockData.searchTrips(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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
