package com.inhatc.tripshare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.inhatc.tripshare.adapter.ScheduleInputAdapter;
import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.SessionManager;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.api.dto.ScheduleItemRequest;
import com.inhatc.tripshare.api.dto.TripCreateRequest;
import com.inhatc.tripshare.databinding.ActivityCreateItineraryBinding;
import com.inhatc.tripshare.model.ScheduleInput;
import com.inhatc.tripshare.model.Trip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateItineraryActivity extends BaseActivity {

    private ActivityCreateItineraryBinding binding;
    private ScheduleInputAdapter scheduleAdapter;
    private final TripRepository repository = TripRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateItineraryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        scheduleAdapter = new ScheduleInputAdapter(() -> { });
        binding.rvScheduleInput.setLayoutManager(new LinearLayoutManager(this));
        binding.rvScheduleInput.setAdapter(scheduleAdapter);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> saveItinerary());
        binding.btnAddSchedule.setOnClickListener(v -> scheduleAdapter.addItem());

        binding.etStartDate.setOnClickListener(v -> showDatePicker(binding.etStartDate));
        binding.etEndDate.setOnClickListener(v -> showDatePicker(binding.etEndDate));
    }

    private void showDatePicker(com.google.android.material.textfield.TextInputEditText target) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format("%d.%02d.%02d", year, month + 1, dayOfMonth);
            target.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveItinerary() {
        String title = getText(binding.etTitle);
        String destination = getText(binding.etDestination);
        String startDate = getText(binding.etStartDate);
        String endDate = getText(binding.etEndDate);
        String intro = getText(binding.etIntro);

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(destination)
                || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        List<ScheduleItemRequest> scheduleItems = buildScheduleItems();
        if (scheduleItems.isEmpty()) {
            Toast.makeText(this, R.string.schedule_required, Toast.LENGTH_SHORT).show();
            return;
        }

        String description = TextUtils.isEmpty(intro) ? title + " 여행 일정입니다." : intro;
        TripCreateRequest request = new TripCreateRequest(
                SessionManager.getUserId(),
                title,
                destination,
                startDate,
                endDate,
                description,
                scheduleItems);

        binding.btnSave.setEnabled(false);
        repository.createTrip(request, new ApiCallback<Trip>() {
            @Override
            public void onSuccess(Trip trip) {
                binding.btnSave.setEnabled(true);
                Toast.makeText(CreateItineraryActivity.this, R.string.itinerary_registered, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateItineraryActivity.this, ItineraryDetailActivity.class);
                intent.putExtra(ItineraryDetailActivity.EXTRA_TRIP_ID, trip.getId());
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                binding.btnSave.setEnabled(true);
                Toast.makeText(CreateItineraryActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<ScheduleItemRequest> buildScheduleItems() {
        List<ScheduleItemRequest> result = new ArrayList<>();
        for (ScheduleInput input : scheduleAdapter.getItems()) {
            if (TextUtils.isEmpty(input.getPlace())) {
                continue;
            }
            String desc = TextUtils.isEmpty(input.getDescription())
                    ? input.getPlace() + " 방문" : input.getDescription();
            result.add(new ScheduleItemRequest(
                    input.getDay(),
                    input.getTime(),
                    input.getPlace(),
                    desc,
                    0, 0));
        }
        return result;
    }

    private String getText(com.google.android.material.textfield.TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
