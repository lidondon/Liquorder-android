package com.infolai.liquorder.views.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.infolai.liquorder.R;
import com.infolai.liquorder.adapters.CellarersAdapter;
import com.infolai.liquorder.adapters.IOnItemClickListener;
import com.infolai.liquorder.constants.IPassingKeys;
import com.infolai.liquorder.decoration.DefaultItemDecoration;
import com.infolai.liquorder.models.Cellarer;
import com.infolai.liquorder.viewmodels.fragments.CreateOrderFVM;
import com.infolai.liquorder.views.activities.MenuActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateOrderFragment extends BaseFragment {
    @BindView(R.id.rvCellarers)
    RecyclerView rvCellarers;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;

    private CreateOrderFVM viewModel;
    private CellarersAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_order, container, false);

        unbinder = ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(this).get(CreateOrderFVM.class);
        viewModel.clearCart();
        IOnItemClickListener<Cellarer> listener = (view, item) -> viewModel.selectMenu(view, item.id);
        adapter = new CellarersAdapter(viewModel.getCellarers().getValue(), listener);
        initRvCellarers();
        observeData();

        return root;
    }

    private void observeData() {
        observeIsLoading(viewModel.isLoading(), pbLoading, getActivity());
        observeCellarers();
        observeSelectedMenuId();
    }

    private void observeCellarers() {
        viewModel.getCellarers().observe(this, cellarers -> {
            adapter.notifyDataSetChanged();
        });
    }

    private void observeSelectedMenuId() {
        viewModel.getSelectedMenuId().observe(this, id -> {
            Intent intent = new Intent(getContext(), MenuActivity.class);
            Cellarer selectedCellarer = viewModel.getSelectedCellarer();
            View itemView = viewModel.getSelectedItem().getValue();
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity()
                , itemView, ViewCompat.getTransitionName(itemView));

            intent.putExtra(IPassingKeys.MENU_ID, id);
            intent.putExtra(IPassingKeys.CELLARER_ID, selectedCellarer.id);
            intent.putExtra(IPassingKeys.CELLARER_NAME, selectedCellarer.name);
            startActivity(intent, options.toBundle());
        });
    }

    private void initRvCellarers() {
        Resources resources = getContext().getResources();
        int hSpace = resources.getDimensionPixelSize(R.dimen.default_item_space_h);
        int vSpace = resources.getDimensionPixelSize(R.dimen.default_item_space_v);

        rvCellarers.setAdapter(adapter);
        rvCellarers.addItemDecoration(new DefaultItemDecoration(hSpace, vSpace));
    }
}