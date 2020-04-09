package com.infolai.liquorder.views.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.infolai.liquorder.R;
import com.infolai.liquorder.adapters.IOnItemClickListener;
import com.infolai.liquorder.adapters.MenuAdapter;
import com.infolai.liquorder.constants.IPassingKeys;
import com.infolai.liquorder.models.Category;
import com.infolai.liquorder.models.MenuItem;
import com.infolai.liquorder.utilities.Util;
import com.infolai.liquorder.viewmodels.activities.MenuAVM;
import com.infolai.liquorder.views.fragments.ItemBottomSheet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.parentToolbar)
    CollapsingToolbarLayout parentToolbar;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;
    @BindView(R.id.tabCategories)
    TabLayout tabCategories;
    @BindView(R.id.rvItems)
    RecyclerView rvItems;
    @BindView(R.id.fabCart)
    FloatingActionButton fabCart;

    private int menuId;
    private int cellarerId;
    private String cellarerName;
    private MenuAVM viewModel;
    private MenuAdapter adapter;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.activity_menu);
        unbinder = ButterKnife.bind(this);
        menuId = intent.getIntExtra(IPassingKeys.MENU_ID, -1);
        cellarerId = intent.getIntExtra(IPassingKeys.CELLARER_ID, -1);
        cellarerName = intent.getStringExtra(IPassingKeys.CELLARER_NAME);
        if (menuId != -1) {
            init();
        } else {
            Toast.makeText(this, R.string.menu_id_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void init() {
        viewModel = ViewModelProviders.of(this).get(MenuAVM.class);
        viewModel.setMenuId(menuId);
        initToolbar();
        initTabCategories();
        initRvItems();
        initFabCart();
        observeData();
    }

    private void observeData() {
        observeIsLoading(viewModel.isLoading(), pbLoading, this);
        observeCategories();
        observeItems();
        observeGetItemsDone();
    }

    private void observeCategories() {
        viewModel.getCategories().observe(this, categories -> {
            tabCategories.removeAllTabs();
            tabCategories.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
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

    private void initToolbar() {
        parentToolbar.setTitle(cellarerName);
        toolbar.setNavigationOnClickListener(view -> {
            if (viewModel.getCartOrder() == null) {
                Util.clearCartConfirm(this);
            } else {
                onBackPressed();
            }
        });
    }

    private void initTabCategories() {
        manager = ((LinearLayoutManager) rvItems.getLayoutManager());
        tabCategories.setTabMode(TabLayout.MODE_SCROLLABLE);
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
        IOnItemClickListener<MenuItem> listener = (view, item) -> {
            ItemBottomSheet buttonSheet = new ItemBottomSheet(ItemBottomSheet.From.MENU, item, v -> {
                adapter.notifyDataSetChanged();
                if (viewModel.getCartOrder() == null) fabCart.show();
            });

            buttonSheet.show(getSupportFragmentManager(), MenuActivity.class.toString());
        };

        adapter = new MenuAdapter(viewModel.getItems().getValue(), listener);
        rvItems.setAdapter(adapter);
    }

    private void initFabCart() {
        fabCart.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, CartActivity.class);
            String transitionName = getString(R.string.transition_cart);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fabCart, transitionName);

            intent.putExtra(IPassingKeys.CELLARER_ID, cellarerId);
            intent.putExtra(IPassingKeys.CELLARER_NAME, cellarerName);
            startActivity(intent, options.toBundle());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetItemQuantities();
        if (!viewModel.isLoading().getValue()) pbLoading.setVisibility(View.INVISIBLE);
        if (viewModel.isCartEmpty()) fabCart.hide();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && viewModel.getCartOrder() == null) Util.clearCartConfirm(this);

        return super.onKeyDown(keyCode, event);
    }


    private void resetItemQuantities() {
        viewModel.resetItemQuantities();
        adapter.notifyDataSetChanged();
    }
}
