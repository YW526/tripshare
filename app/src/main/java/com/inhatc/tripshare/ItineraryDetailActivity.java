package com.inhatc.tripshare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.inhatc.tripshare.adapter.ReviewAdapter;
import com.inhatc.tripshare.adapter.ScheduleAdapter;
import com.inhatc.tripshare.data.MockData;
import com.inhatc.tripshare.databinding.ActivityItineraryDetailBinding;
import com.inhatc.tripshare.model.Trip;

public class ItineraryDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TRIP_ID = "trip_id";

    private ActivityItineraryDetailBinding binding;
    private Trip trip;
    private ScheduleAdapter scheduleAdapter;
    private ReviewAdapter reviewAdapter;
    private int tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItineraryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tripId = getIntent().getIntExtra(EXTRA_TRIP_ID, 1);
        loadTrip();

        setupViews();
        setupTabs();
        setupReviews();
    }

    private void loadTrip() {
        trip = MockData.getTripById(tripId);
    }

    private void setupViews() {
        binding.imgCover.setImageResource(trip.getImageRes());
        binding.tvTitle.setText(trip.getTitle());
        binding.tvAuthor.setText(trip.getAuthor());
        binding.tvLocation.setText(trip.getLocation());
        binding.tvDates.setText(trip.getDateRange());
        binding.tvDescription.setText(trip.getDescription());
        updateReviewCount();

        scheduleAdapter = new ScheduleAdapter();
        binding.rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSchedule.setAdapter(scheduleAdapter);
        scheduleAdapter.setSchedule(trip.getSchedule());

        binding.tvInfo.setText("여행지: " + trip.getLocation() + "\n"
                + "기간: " + trip.getDateRange() + "\n\n"
                + trip.getDescription());

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnShare.setOnClickListener(v ->
                Toast.makeText(this, "공유 기능 준비 중", Toast.LENGTH_SHORT).show());
        binding.btnMore.setOnClickListener(v ->
                Toast.makeText(this, "더보기 준비 중", Toast.LENGTH_SHORT).show());
        binding.btnFollow.setOnClickListener(v ->
                Toast.makeText(this, "팔로우 완료", Toast.LENGTH_SHORT).show());
        binding.btnSave.setOnClickListener(v ->
                Toast.makeText(this, R.string.saved_itinerary, Toast.LENGTH_SHORT).show());
        binding.btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra(MapActivity.EXTRA_TRIP_ID, trip.getId());
            startActivity(intent);
        });
    }

    private void setupTabs() {
        updateReviewTabTitle();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                binding.rvSchedule.setVisibility(pos == 0 ? View.VISIBLE : View.GONE);
                binding.tvInfo.setVisibility(pos == 1 ? View.VISIBLE : View.GONE);
                binding.layoutReviews.setVisibility(pos == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupReviews() {
        reviewAdapter = new ReviewAdapter();
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReviews.setAdapter(reviewAdapter);
        refreshReviews();

        binding.btnSubmitReview.setOnClickListener(v -> submitReview());
    }

    private void submitReview() {
        String content = binding.etReview.getText() != null
                ? binding.etReview.getText().toString().trim() : "";
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, R.string.review_hint, Toast.LENGTH_SHORT).show();
            return;
        }

        int rating = Math.round(binding.ratingInput.getRating());
        if (rating < 1) {
            rating = 1;
        }

        MockData.addReview(tripId, MockData.getCurrentUser().getNickname(), content, rating);
        binding.etReview.getText().clear();
        binding.ratingInput.setRating(5);
        refreshReviews();
        updateReviewCount();
        updateReviewTabTitle();

        Toast.makeText(this, R.string.review_success, Toast.LENGTH_SHORT).show();
    }

    private void refreshReviews() {
        reviewAdapter.setReviews(MockData.getReviews(tripId));
        binding.tvReviewEmpty.setVisibility(
                reviewAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void updateReviewCount() {
        binding.tvComments.setText(String.valueOf(MockData.getReviewCount(tripId)));
    }

    private void updateReviewTabTitle() {
        TabLayout.Tab reviewTab = binding.tabLayout.getTabAt(2);
        if (reviewTab != null) {
            reviewTab.setText(getString(R.string.tab_reviews, MockData.getReviewCount(tripId)));
        }
    }
}
