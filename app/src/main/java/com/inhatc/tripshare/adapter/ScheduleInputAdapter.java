package com.inhatc.tripshare.adapter;

import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.inhatc.tripshare.R;
import com.inhatc.tripshare.model.ScheduleInput;

import java.util.ArrayList;
import java.util.List;

public class ScheduleInputAdapter extends RecyclerView.Adapter<ScheduleInputAdapter.InputViewHolder> {

    public interface OnScheduleChangedListener {
        void onScheduleChanged();
    }

    private final List<ScheduleInput> items = new ArrayList<>();
    private final OnScheduleChangedListener listener;

    public ScheduleInputAdapter(OnScheduleChangedListener listener) {
        this.listener = listener;
        items.add(new ScheduleInput());
    }

    public List<ScheduleInput> getItems() {
        return new ArrayList<>(items);
    }

    public void addItem() {
        items.add(new ScheduleInput());
        notifyItemInserted(items.size() - 1);
        listener.onScheduleChanged();
    }

    public void removeItem(int position) {
        if (items.size() <= 1) {
            return;
        }
        items.remove(position);
        notifyItemRemoved(position);
        listener.onScheduleChanged();
    }

    @NonNull
    @Override
    public InputViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule_input, parent, false);
        return new InputViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InputViewHolder holder, int position) {
        holder.bind(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class InputViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText etDay;
        private final TextInputEditText etTime;
        private final TextInputEditText etPlace;
        private final TextInputEditText etDescription;
        private final ImageButton btnRemove;

        InputViewHolder(@NonNull View itemView) {
            super(itemView);
            etDay = itemView.findViewById(R.id.et_day);
            etTime = itemView.findViewById(R.id.et_time);
            etPlace = itemView.findViewById(R.id.et_place);
            etDescription = itemView.findViewById(R.id.et_description);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

        void bind(ScheduleInput input, int position) {
            etDay.setText(String.valueOf(input.getDay()));
            etTime.setText(input.getTime());
            etPlace.setText(input.getPlace());
            etDescription.setText(input.getDescription());

            etDay.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    updateDay(input);
                }
            });
            etPlace.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    input.setPlace(getText(etPlace));
                }
            });
            etDescription.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    input.setDescription(getText(etDescription));
                }
            });

            etTime.setOnClickListener(v -> {
                String[] parts = input.getTime().split(":");
                int hour = parts.length > 0 ? parseInt(parts[0], 9) : 9;
                int minute = parts.length > 1 ? parseInt(parts[1], 0) : 0;
                new TimePickerDialog(itemView.getContext(), (view, h, m) -> {
                    String time = String.format("%02d:%02d", h, m);
                    input.setTime(time);
                    etTime.setText(time);
                }, hour, minute, true).show();
            });

            btnRemove.setOnClickListener(v -> removeItem(position));
        }

        private void updateDay(ScheduleInput input) {
            input.setDay(parseInt(getText(etDay), 1));
        }

        private String getText(TextInputEditText editText) {
            return editText.getText() != null ? editText.getText().toString().trim() : "";
        }

        private int parseInt(String value, int fallback) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return fallback;
            }
        }
    }
}
