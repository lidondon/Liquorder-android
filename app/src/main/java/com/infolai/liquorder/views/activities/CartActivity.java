package com.infolai.liquorder.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.infolai.liquorder.R;
import com.infolai.liquorder.adapters.CartAdapter;
import com.infolai.liquorder.adapters.IOnItemClickListener;
import com.infolai.liquorder.constants.IPassingKeys;
import com.infolai.liquorder.enums.Status;
import com.infolai.liquorder.models.BaseItem;
import com.infolai.liquorder.models.Order;
import com.infolai.liquorder.utilities.Util;
import com.infolai.liquorder.viewmodels.activities.CartAVM;
import com.infolai.liquorder.views.fragments.ItemBottomSheet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends BaseActivity {
    @BindView(R.id.clRoot)
    ConstraintLayout clRoot;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTotalAmount)
    TextView tvTotalAmount;
    @BindView(R.id.rvItems)
    RecyclerView rvItems;
    @BindView(R.id.llButtons)
    LinearLayout llButtons;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.fabMenu)
    FloatingActionButton fabMenu;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;

    private Order order;
    private String cellarerName;
    private CartAVM viewModel;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        unbinder = ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(CartAVM.class);
        getExtraValues();
        initToolbar();
        initRvItems();
        setTvTotalAmount();
        initButtons();
        observeDatas();
    }

    private void getExtraValues() {
        Intent intent = getIntent();
        int cellarerId = intent.getIntExtra(IPassingKeys.CELLARER_ID, -1);

        cellarerName = intent.getStringExtra(IPassingKeys.CELLARER_NAME);
        viewModel.setCellarerId(cellarerId);
        order = viewModel.getOrder();
    }

    private void observeDatas() {
        observeIsLoading(viewModel.isLoading(), pbLoading, this);
        observerIsDone();
        observeMenuId();
        observeItems();
    }



    private void observerIsDone() {
        viewModel.isDone().observe(this, isDone -> {
            if (isDone) {
                Toast.makeText(this, R.string.order_process_done, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void observeMenuId() {
        viewModel.getMenuId().observe(this, id -> {
            if (id != -1) {
                Intent intent = new Intent(this, MenuActivity.class);

                intent.putExtra(IPassingKeys.MENU_ID, id);
                intent.putExtra(IPassingKeys.CELLARER_ID, order.cellarerId);
                intent.putExtra(IPassingKeys.CELLARER_NAME, order.cellarerName);
                startActivity(intent);
            }
        });
    }

    private void observeItems() {
        viewModel.getItems().observe(this, items -> {
            adapter.notifyDataSetChanged();
        });
    }

    private void initToolbar() {
        if (order != null) {
            toolbar.setTitle(order.formNumber);
            toolbar.setSubtitle(order.cellarerName);
        } else {
            toolbar.setSubtitle(cellarerName);
        }
        toolbar.setNavigationOnClickListener(view -> {
            if (order != null) {
                Util.clearCartConfirm(this);
            } else {
                onBackPressed();
            }
        });
    }

    private void initRvItems() {
        List<BaseItem> items = viewModel.getItems().getValue();
        IOnItemClickListener<BaseItem> listener = (view, item) -> {
            if (order == null || Status.valueOf(order.orderStatus) == Status.SAVE) {
                ItemBottomSheet buttonSheet = new ItemBottomSheet(ItemBottomSheet.From.CART, item, v -> {
                    if (item.quantity == 0) items.remove(item);
                    adapter.notifyDataSetChanged();
                    setTvTotalAmount();
                });

                buttonSheet.show(getSupportFragmentManager(), MenuActivity.class.toString());
            }
        };

        adapter = new CartAdapter(items, listener);
        rvItems.setAdapter(adapter);
    }

    private void setTvTotalAmount() {
        int amount = viewModel.getTotalAmount();

        tvTotalAmount.setText(String.format(getString(R.string.price), amount));
    }

    private void initButtons() {
        fabMenu.hide();
        if (order == null) {
            btnSave.setOnClickListener(v -> viewModel.createOrder(false));
            btnSubmit.setOnClickListener(v -> viewModel.createOrder(true));
        } else if (Status.valueOf(order.orderStatus) != Status.SAVE) {
            llButtons.setVisibility(View.GONE);
        } else {
            btnSave.setOnClickListener(v -> viewModel.updateOrder(false));
            btnSubmit.setOnClickListener(v -> viewModel.updateOrder(true));
            fabMenu.setOnClickListener(v -> viewModel.getMenuFromApi(order.cellarerId));
            fabMenu.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refreshItems();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && order == null) Util.clearCartConfirm(this);

        return super.onKeyDown(keyCode, event);
    }
}
