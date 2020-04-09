package com.infolai.liquorder.views.activities;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.infolai.liquorder.R;
import com.infolai.liquorder.viewmodels.activities.PortalAVM;

public class PortalActivity extends BaseActivity {
    private PortalAVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);
        viewModel = ViewModelProviders.of(this).get(PortalAVM.class);
        observeHasLoginData();
        observeIsLoginSuccess();
        observeConnectionFailMsg();
    }

    private void observeHasLoginData() {
        viewModel.getHasLoginData().observe(this, data -> {
            if (!data) {
                gotoLogin();
            }
        });
    }

    private void observeIsLoginSuccess() {
        viewModel.isLoginSuccess().observe(this, data -> {
            if (data) {
                goToMain();
            } else {
                gotoLogin();
            }
        });
    }

    private void observeConnectionFailMsg() {
        viewModel.getConnectionError().observe(this, data -> {
            if (!data.isEmpty()) {
                Log.e(PortalActivity.class.toString(), data);
                Toast.makeText(PortalActivity.this, data, Toast.LENGTH_LONG).show();
                //todo should have a error page
            }
        });
    }

    private void gotoLogin() {
        PortalActivity.this.startActivity(new Intent(PortalActivity.this, LoginActivity.class));
        finish();
    }

    public void goToMain() {
        PortalActivity.this.startActivity(new Intent(PortalActivity.this, MainActivity.class));
        finish();
    }
}
