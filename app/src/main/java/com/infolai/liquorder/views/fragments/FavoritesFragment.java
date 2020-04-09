package com.infolai.liquorder.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.infolai.liquorder.R;
import com.infolai.liquorder.adapters.IOnItemClickListener;
import com.infolai.liquorder.adapters.LiquorAdapter;
import com.infolai.liquorder.adapters.MenuAdapter;
import com.infolai.liquorder.models.Category;
import com.infolai.liquorder.models.LiquorItem;
import com.infolai.liquorder.models.MenuItem;
import com.infolai.liquorder.viewmodels.fragments.FavoritesFVM;
import com.infolai.liquorder.views.activities.MenuActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesFragment extends BaseFragment {
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;
    @BindView(R.id.tabCategories)
    TabLayout tabCategories;
    @BindView(R.id.rvItems)
    RecyclerView rvItems;

    private FavoritesFVM viewModel;
    private LiquorAdapter adapter;
    private LinearLayoutManager manager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);

        unbinder = ButterKnife.bind(this, root);
        viewModel = ViewModelProviders.of(this).get(FavoritesFVM.class);
        init();

        return root;
    }

    private void init() {
        initTabCategories();
        initRvItems();
        observeData();
    }

    private void observeData() {
        observeIsLoading(viewModel.isLoading(), pbLoading, getActivity());
        observeCategories();
        observeItems();
        observeGetItemsDone();
        observeSuccessItem();
        observeFailItem();
    }

    private void observeCategories() {
        viewModel.getCategories().observe(this, categories -> {
            tabCategories.removeAllTabs();
            for (Category c : categories) {
                tabCategories.addTab(tabCategories.newTab().setText(c.name));
            }
        });
    }

    private void observeGetItemsDone() {
        viewModel.isGetItemsCompleted().observe(this, isDone -> {
            if (isDone) {
                rvItems.setOnScrollChangeListener((view, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    int firstPosition = manager.findFirstVisibleItemPosition();
                    int position = viewModel.getCategoryPositionByItemPosition(firstPosition);

                    tabCategories.setScrollPosition(position, 0, false);
                });
            }
        });
    }

    private void observeItems() {
        viewModel.getItems().observe(this, items -> {
            adapter.notifyDataSetChanged();
        });
    }

    private void observeSuccessItem() {
        viewModel.getSuccessItem().observe(this, item -> {
            if (item != null) adapter.notifyDataSetChanged();
        });
    }

    private void observeFailItem() {
        viewModel.getFailItem().observe(this, item -> {
            Context context = getContext();

            adapter.notifyDataSetChanged();
            Toast.makeText(context, String.format(context.getString(R.string.favorite_process_fail), item.name), Toast.LENGTH_SHORT).show();
        });
    }

    private void initTabCategories() {
        manager = ((LinearLayoutManager) rvItems.getLayoutManager());
        tabCategories.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabCategories.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tabCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                int[] categoryIndices = viewModel.getCategoryIndices();

                manager.scrollToPositionWithOffset(categoryIndices[tabPosition], 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initRvItems()  {
        IOnItemClickListener<LiquorItem> listener = (view, item) -> {
            if (item.favoriteId != -1) {
                viewModel.removeFavorite(item);
            } else {
                viewModel.addFavorite(item);
            }
        };

        adapter = new LiquorAdapter(viewModel.getItems().getValue(), listener);
        rvItems.setAdapter(adapter);
    }
}