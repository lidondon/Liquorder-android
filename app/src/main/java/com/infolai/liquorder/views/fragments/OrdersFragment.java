package com.infolai.liquorder.views.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.infolai.liquorder.R;
import com.infolai.liquorder.adapters.IOnItemClickListener;
import com.infolai.liquorder.adapters.OrdersAdapter;
import com.infolai.liquorder.models.Cellarer;
import com.infolai.liquorder.models.MenuItem;
import com.infolai.liquorder.models.Order;
import com.infolai.liquorder.viewmodels.fragments.OrdersFVM;
import com.infolai.liquorder.views.activities.CartActivity;
import com.infolai.liquorder.views.activities.MenuActivity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersFragment extends BaseFragment {
    @BindView(R.id.tvStartDate)
    TextView tvStartDate;
    @BindView(R.id.tvEndDate)
    TextView tvEndDate;
    @BindView(R.id.ivStartDate)
    ImageView ivStartDate;
    @BindView(R.id.ivEndDate)
    ImageView ivEndDate;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;
    @BindView(R.id.tabCategories)
    TabLayout tabCellarers;
    @BindView(R.id.rvItems)
    RecyclerView rvOrders;

    private OrdersFVM viewModel;
    private OrdersAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        unbinder = ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(this).get(OrdersFVM.class);
        setDefaultDateRange();
        initTabCellarers();
        initRvOrders();
        observeDatas();

        return root;
    }

    private void setDefaultDateRange() {
        String[] range = viewModel.getDefaultDateRange();

        tvStartDate.setText(range[0]);
        tvEndDate.setText(range[1]);
        ivStartDate.setOnClickListener(view -> showCalendar(tvStartDate, tvStartDate.getText().toString()));
        ivEndDate.setOnClickListener(view -> showCalendar(tvEndDate, tvEndDate.getText().toString()));
    }

    private void showCalendar(TextView tvDate, String strDate) {
        String[] date = strDate.split("-");

        if (date.length == 3) {
            DatePickerDialog.OnDateSetListener listener = (datePicker, year, month, day) -> {
                tvDate.setText(String.format(getString(R.string.date), year, month + 1, day));
                viewModel.getOrdersFromApi(tvStartDate.getText().toString(), tvEndDate.getText().toString());
            };

            new DatePickerDialog(getContext(), listener, Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2])).show();
        }
    }

    private void observeDatas() {
        observeIsLoading(viewModel.isLoading(), pbLoading, getActivity());
        observeCellarers();
        observeOrders();
        observeIsGetOrderItemsDone();
    }

    private void observeOrders() {
        viewModel.getOrders().observe(this, orders -> adapter.notifyDataSetChanged());
    }

    private void observeCellarers() {
        viewModel.getCellarers().observe(this, cellarers -> {
            tabCellarers.removeAllTabs();
            tabCellarers.addTab(tabCellarers.newTab().setText(R.string.unlimited).setTag(R.string.unlimited));
            tabCellarers.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
            for (Cellarer c : cellarers) {
                tabCellarers.addTab(tabCellarers.newTab().setText(c.name).setTag(c.id));
            }
        });
    }

    private void observeIsGetOrderItemsDone() {
        viewModel.isGetOrderItemsDone().observe(this, isDone -> {
            if (isDone) startActivity(new Intent(getContext(), CartActivity.class));
        });
    }

    private void initTabCellarers() {
        tabCellarers.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabCellarers.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewModel.filterCellarer((int) tab.getTag());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initRvOrders() {
        IOnItemClickListener<Order> listener = (view, item) -> viewModel.setOrderIn2Cart(item);

        adapter = new OrdersAdapter(viewModel.getOrders().getValue(), listener);
        rvOrders.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getOrdersFromApi(tvStartDate.getText().toString(), tvEndDate.getText().toString());
        tabCellarers.setScrollPosition(0, 0, false);
    }
}