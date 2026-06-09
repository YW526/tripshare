package com.inhatc.tripshare;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.inhatc.tripshare.adapter.ReviewAdapter;
import com.inhatc.tripshare.adapter.ScheduleAdapter;
import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.databinding.ActivityItineraryDetailBinding;
import com.inhatc.tripshare.model.Review;
import com.inhatc.tripshare.model.Trip;

import java.util.List;

public class ItineraryDetailActivity extends BaseActivity {

    public static final String EXTRA_TRIP_ID = "trip_id";

    private ActivityItineraryDetailBinding binding;
    private Trip trip;
    private ScheduleAdapter scheduleAdapter;
    private ReviewAdapter reviewAdapter;
    private int tripId;
    private boolean isFollowing;
    private final TripRepository repository = TripRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItineraryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tripId = getIntent().getIntExtra(EXTRA_TRIP_ID, 1);

        scheduleAdapter = new ScheduleAdapter();
        binding.rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSchedule.setAdapter(scheduleAdapter);

        reviewAdapter = new ReviewAdapter();
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReviews.setAdapter(reviewAdapter);

        setupTabs();
        setupActions();
        loadTrip();
    }

    private void loadTrip() {
        repository.getTrip(tripId, new ApiCallback<Trip>() {
            @Override
            public void onSuccess(Trip data) {
                trip = data;
                bindTrip();
                loadReviews();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void bindTrip() {
        binding.imgCover.setImageResource(trip.getImageRes());
        binding.tvTitle.setText(trip.getTitle());
        binding.tvAuthor.setText(trip.getAuthor());
        binding.tvLocation.setText(trip.getLocation());
        binding.tvDates.setText(trip.getDateRange());
        binding.tvDescription.setText(trip.getDescription());
        updateReviewCount();
        updateReviewTabTitle(trip.getComments());

        scheduleAdapter.setSchedule(trip.getSchedule());

        binding.tvInfo.setText("여행지: " + trip.getLocation() + "\n"
                + "기간: " + trip.getDateRange() + "\n\n"
                + trip.getDescription());

        isFollowing = false;
        updateFollowButtonStyle();
    }

    private void setupActions() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnShare.setOnClickListener(v ->
                Toast.makeText(this, "공유 기능 준비 중", Toast.LENGTH_SHORT).show());
        binding.btnMore.setOnClickListener(v ->
                Toast.makeText(this, "더보기 준비 중", Toast.LENGTH_SHORT).show());
        binding.btnFollow.setOnClickListener(v -> toggleFollow());
        binding.btnSave.setOnClickListener(v -> saveTrip());
        binding.btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra(MapActivity.EXTRA_TRIP_ID, trip.getId());
            startActivity(intent);
        });
        binding.btnSubmitReview.setOnClickListener(v -> submitReview());
    }

    private void toggleFollow() {
        if (trip == null || trip.getAuthorUserId() == 0) {
            return;
        }
        if (isFollowing) {
            isFollowing = false;
            updateFollowButtonStyle();
            return;
        }

        repository.followUser(trip.getAuthorUserId(), new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                isFollowing = true;
                updateFollowButtonStyle();
                Toast.makeText(ItineraryDetailActivity.this, R.string.follow_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveTrip() {
        if (trip == null) {
            return;
        }
        repository.saveTrip(trip.getId(), new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.saved_itinerary, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFollowButtonStyle() {
        if (isFollowing) {
            binding.btnFollow.setText(R.string.following);
            binding.btnFollow.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.transparent)));
            binding.btnFollow.setStrokeColor(
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.purple_primary)));
            binding.btnFollow.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.stroke_width));
            binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.purple_primary));
        } else {
            binding.btnFollow.setText(R.string.follow);
            binding.btnFollow.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.purple_primary)));
            binding.btnFollow.setStrokeWidth(0);
            binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    private void setupTabs() {
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

        repository.addReview(tripId, content, rating, new ApiCallback<Review>() {
            @Override
            public void onSuccess(Review data) {
                binding.etReview.getText().clear();
                binding.ratingInput.setRating(5);
                loadReviews();
                Toast.makeText(ItineraryDetailActivity.this, R.string.review_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadReviews() {
        repository.getReviews(tripId, new ApiCallback<List<Review>>() {
            @Override
            public void onSuccess(List<Review> data) {
                reviewAdapter.setReviews(data);
                binding.tvReviewEmpty.setVisibility(
                        reviewAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                binding.tvComments.setText(String.valueOf(data.size()));
                updateReviewTabTitle(data.size());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateReviewCount() {
        if (trip != null) {
            binding.tvComments.setText(String.valueOf(trip.getComments()));
        }
    }

    private void updateReviewTabTitle(int count) {
        TabLayout.Tab reviewTab = binding.tabLayout.getTabAt(2);
        if (reviewTab != null) {
            reviewTab.setText(getString(R.string.tab_reviews, count));
        }
    }
}
