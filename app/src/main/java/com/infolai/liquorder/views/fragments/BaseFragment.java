package com.infolai.liquorder.views.fragments;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.infolai.liquorder.utilities.Util;

import butterknife.Unbinder;

public class BaseFragment extends Fragment {
    protected Unbinder unbinder;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
