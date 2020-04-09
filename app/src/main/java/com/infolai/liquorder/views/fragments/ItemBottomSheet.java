package com.infolai.liquorder.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.infolai.liquorder.R;
import com.infolai.liquorder.models.BaseItem;
import com.infolai.liquorder.models.MenuItem;
import com.infolai.liquorder.models.OrderItem;
import com.infolai.liquorder.repositories.CartRepository;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemBottomSheet extends BottomSheetDialogFragment {
    public enum From { MENU, CART }
    private static final int MAX = 100;

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvCapacity)
    TextView tvCapacity;
    @BindView(R.id.tvBottling)
    TextView tvBottling;
    @BindView(R.id.tvQuantity)
    TextView tvQuantity;
    @BindView(R.id.ivMinus)
    ImageView ivMinus;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.btnCommit)
    Button btnCommit;

    private int min;
    private BaseItem item;
    private View.OnClickListener parentListener;
    private CartRepository cartRepository;
    private From from;

    private View.OnClickListener btnCommitOnClickListener = view -> {
        item.quantity = Integer.valueOf(tvQuantity.getText().toString());
        if (from == From.MENU) {
            item.quantity = cartRepository.addMenuItem((MenuItem) item);
        } else {
            cartRepository.updateOrderItem(item);
        }
        parentListener.onClick(view);
        dismiss();
    };

    public ItemBottomSheet(From f, BaseItem i, View.OnClickListener listener) {
        item = i;
        parentListener = listener;
        from = f;
        min = (from == From.MENU) ? 1 : 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_menu_item, container, false);

        ButterKnife.bind(this, view);
        cartRepository = CartRepository.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setValues();
        setIvAdd(0);
        setIvMinus(0);
        btnCommit.setOnClickListener(btnCommitOnClickListener);
    }

    private void setValues() {
        String strQuantity = "1";

        if (from == From.CART) {
            strQuantity = String.valueOf(item.quantity);
            btnCommit.setText(R.string.update);
        }
        tvName.setText(item.liquorName);
        tvCapacity.setText(String.format(getContext().getString(R.string.capacity), item.liquorCapacity));
        tvBottling.setText(item.liquorBottling);
        tvQuantity.setText(strQuantity);
    }

    private void setIvAdd(int currentQ) {
        final int quantity = (currentQ == 0) ? Integer.valueOf(tvQuantity.getText().toString()) : currentQ;
        View.OnClickListener listener = view -> {
            int currentQuantity = quantity + 1;

            tvQuantity.setText(String.valueOf(currentQuantity));
            calculateSubtitle(currentQuantity);
            setIvAdd(currentQuantity);
            setIvMinus(currentQuantity);
        };

        if (quantity == MAX) {
            ivAdd.setOnClickListener(null);
            ivAdd.setImageResource(R.drawable.add_disabled);
        } else {
            ivAdd.setOnClickListener(listener);
            ivAdd.setImageResource(R.drawable.add);
        }
    }

    private void setIvMinus(int currentQ) {
        final int quantity = (currentQ == 0) ? Integer.valueOf(tvQuantity.getText().toString()) : currentQ;
        View.OnClickListener listener = view -> {
            int currentQuantity = quantity - 1;

            tvQuantity.setText(String.valueOf(currentQuantity));
            calculateSubtitle(currentQuantity);
            setIvAdd(currentQuantity);
            setIvMinus(currentQuantity);
        };

        if (quantity == min) {
            ivMinus.setOnClickListener(null);
            ivMinus.setImageResource(R.drawable.minus_disabled);
        } else {
            ivMinus.setOnClickListener(listener);
            ivMinus.setImageResource(R.drawable.minus);
        }
    }

    private void calculateSubtitle(int amount) {
//        int subtitle = amount * item.price;
//
//        tvSubtitle.setText(String.format(getString(R.string.subtitle), subtitle));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentListener = null;
    }


}
