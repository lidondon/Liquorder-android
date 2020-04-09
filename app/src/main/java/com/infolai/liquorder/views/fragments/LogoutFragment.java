package com.infolai.liquorder.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.infolai.liquorder.R;
import com.infolai.liquorder.repositories.LoginDataRepository;
import com.infolai.liquorder.views.activities.LoginActivity;
import com.infolai.liquorder.views.activities.PortalActivity;

public class LogoutFragment extends BaseFragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        Context context = getContext();
        LoginDataRepository repository = LoginDataRepository.getInstance();

        repository.removeLoginData(context);
        repository.authData = null;
        context.startActivity(new Intent(context, LoginActivity.class));
        getActivity().finish();

        return root;
    }

}