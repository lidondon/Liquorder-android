package com.infolai.liquorder.views.items;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infolai.liquorder.R;
import com.infolai.liquorder.adapters.IOnItemClickListener;
import com.infolai.liquorder.models.Category;
import com.infolai.liquorder.models.Cellarer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryItemView extends RecyclerView.ViewHolder {
    @BindView(R.id.tvCategory)
    TextView tvCategory;

    public CategoryItemView(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setTvCategory(Category category) {
        tvCategory.setText(category.name);
    }
}
