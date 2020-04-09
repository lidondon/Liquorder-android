package com.infolai.liquorder.views.items;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.infolai.liquorder.R;
import com.infolai.liquorder.adapters.IOnItemClickListener;
import com.infolai.liquorder.models.BaseItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartItemView extends BaseItemView {
    @BindView(R.id.clRoot)
    ConstraintLayout clRoot;
    @BindView(R.id.tvQuantity)
    TextView tvQuantity;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPrice)
    TextView tvPrice;


    public CartItemView(@NonNull View itemView, IOnItemClickListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);
    }

    public void setItem(BaseItem item) {
        tvQuantity.setText(String.format(context.getString(R.string.menu_item_quantity), item.quantity));
        tvName.setText(item.liquorName);
        tvPrice.setText(String.format(context.getString(R.string.price), item.price));
        clRoot.setOnClickListener(v -> onItemClickListener.onItemClick(view, item));
    }
}
