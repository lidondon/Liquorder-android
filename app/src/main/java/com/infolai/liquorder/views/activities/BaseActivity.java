package com.infolai.liquorder.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.infolai.liquorder.R;
import com.infolai.liquorder.utilities.Util;

import butterknife.Unbinder;

public class BaseActivity extends AppCompatActivity {
    protected Unbinder unbinder;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isTaskRoot()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                DialogInterface.OnClickListener positiveOnClickListener = (dialog, which) -> finish();
                DialogInterface.OnClickListener negativeOnClickListener = (dialog, which) -> dialog.dismiss();

                builder.setTitle(getResources().getString(R.string.wanna_finish_app));
                builder.setPositiveButton(R.string.commit, positiveOnClickListener);
                builder.setNegativeButton(R.string.cancel, negativeOnClickListener);
                builder.create().show();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
    }

    protected void observeIsLoading(LiveData<Boolean> loading, ProgressBar pbLoading, Activity activity) {
        loading.observe(this, isLoading -> {
            if (isLoading) {
                pbLoading.setVisibility(View.VISIBLE);
                Util.blockUserInteraction(activity);
            } else {
                pbLoading.setVisibility(View.GONE);
                Util.unblockUserInteraction(activity);
            }
        });
    }
}
