package com.inhatc.tripshare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.inhatc.tripshare.R;
import com.inhatc.tripshare.WelcomeActivity;
import com.inhatc.tripshare.adapter.MenuAdapter;
import com.inhatc.tripshare.data.MockData;
import com.inhatc.tripshare.databinding.FragmentProfileBinding;
import com.inhatc.tripshare.model.UserProfile;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserProfile user = MockData.getCurrentUser();
        binding.tvNickname.setText(user.getNickname());
        binding.tvBio.setText(user.getBio());
        binding.tvCreatedCount.setText(String.valueOf(user.getCreatedCount()));
        binding.tvSavedCount.setText(String.valueOf(user.getSavedCount()));
        binding.tvLikesCount.setText(String.valueOf(user.getLikesReceived()));

        String[] menuItems = {
                getString(R.string.edit_profile),
                getString(R.string.change_password),
                getString(R.string.notification_settings),
                getString(R.string.customer_center),
                getString(R.string.logout)
        };

        MenuAdapter menuAdapter = new MenuAdapter(menuItems, menu -> {
            if (getString(R.string.logout).equals(menu)) {
                Intent intent = new Intent(requireContext(), WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), menu + " - 준비 중", Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvMenu.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMenu.setAdapter(menuAdapter);

        binding.btnSettings.setOnClickListener(v ->
                Toast.makeText(requireContext(), "설정 - 준비 중", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
