package com.infolai.liquorder.views.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.infolai.liquorder.R;
import com.infolai.liquorder.adapters.IOnItemClickListener;
import com.infolai.liquorder.models.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuItemView extends BaseItemView {
    @BindView(R.id.clRoot)
    ConstraintLayout clRoot;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tvQuantity)
    TextView tvQuantity;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvCapacity)
    TextView tvCapacity;
    @BindView(R.id.tvBottling)
    TextView tvBottling;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.ivFavorite)
    ImageView ivFavorite;

    public MenuItemView(@NonNull View itemView, IOnItemClickListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);
    }

    public void setMenuItem(MenuItem item) {
        int favoriteVisibility = item.isConsumerFav ? View.VISIBLE : View.GONE;

        tvName.setText(item.liquorName);
        tvCapacity.setText(String.format(context.getString(R.string.capacity), item.liquorCapacity));
        tvBottling.setText(item.liquorBottling);
        tvPrice.setText(String.format(context.getString(R.string.price), item.price));
        ivFavorite.setVisibility(favoriteVisibility);
        if (item.quantity > 0) {
            vLine.setVisibility(View.VISIBLE);
            tvQuantity.setVisibility(View.VISIBLE);
            tvQuantity.setText(String.format(context.getString(R.string.menu_item_quantity), item.quantity));
        } else {
            vLine.setVisibility(View.INVISIBLE);
            tvQuantity.setVisibility(View.GONE);
        }
        clRoot.setOnClickListener(v -> onItemClickListener.onItemClick(view, item));
    }
}
