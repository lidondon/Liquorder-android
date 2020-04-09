package com.infolai.liquorder.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.infolai.liquorder.R;
import com.infolai.liquorder.models.Category;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.bnvContainer)
    BottomNavigationView bnvContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initNavigation();
    }

    private void initNavigation() {
        NavController navController = Navigation.findNavController(this, R.id.fContainer);

        NavigationUI.setupWithNavController(bnvContainer, navController);
    }
}
