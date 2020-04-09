package com.infolai.liquorder.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.infolai.liquorder.R;
import com.infolai.liquorder.utilities.Util;
import com.infolai.liquorder.viewmodels.activities.LoginAVM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.loading)
    ProgressBar pbLoading;

    private LoginAVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(LoginAVM.class);
        observeIsLoading(viewModel.isLoading(), pbLoading, this);
        observeErrorMessage();
        observeAuthData();
        btnLogin.setOnClickListener(v -> {
            viewModel.login(etEmail.getText().toString(), etPassword.getText().toString());
        });
    }

    private void observeErrorMessage() {
        viewModel.getApiError().observe(this, data -> {
            if (data != null) {
                Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeAuthData() {
        viewModel.getAuthData().observe(this, data -> {
            if (data != null) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
