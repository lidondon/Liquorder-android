package com.infolai.liquorder.views.items;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.infolai.liquorder.R;
import com.infolai.liquorder.adapters.IOnItemClickListener;
import com.infolai.liquorder.models.FavoriteItem;
import com.infolai.liquorder.models.LiquorItem;
import com.infolai.liquorder.models.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiquorItemView extends BaseItemView {
    @BindView(R.id.clRoot)
    ConstraintLayout clRoot;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvCapacity)
    TextView tvCapacity;
    @BindView(R.id.tvBottling)
    TextView tvBottling;
    @BindView(R.id.cbx)
    CheckBox cbx;

    private IOnItemClickListener onItemClickListener;

    public LiquorItemView(@NonNull View itemView, IOnItemClickListener listener) {
        super(itemView, null);
        ButterKnife.bind(this, itemView);
        onItemClickListener = listener;
    }

    public void setLiquorItem(LiquorItem item) {

        tvName.setText(item.name);
        tvCapacity.setText(String.format(context.getString(R.string.capacity), item.capacity));
        tvBottling.setText(item.bottling);
        cbx.setChecked(item.favoriteId != -1);
        clRoot.setOnClickListener(v -> onItemClickListener.onItemClick(view, item));
    }
}
