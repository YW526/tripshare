package com.inhatc.tripshare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.inhatc.tripshare.data.MockData;
import com.inhatc.tripshare.databinding.ActivityLoginBinding;
import com.inhatc.tripshare.util.CaptchaViewHelper;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private CaptchaViewHelper captchaHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        captchaHelper = new CaptchaViewHelper(binding.captchaView.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());
        binding.tvSignup.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
        binding.btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = binding.etEmail.getText() != null
                ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null
                ? binding.etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!captchaHelper.isVerified()) {
            Toast.makeText(this, R.string.captcha_wrong, Toast.LENGTH_SHORT).show();
            captchaHelper.refresh();
            return;
        }
        if (!MockData.login(email, password)) {
            Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
            captchaHelper.refresh();
            return;
        }

        Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
