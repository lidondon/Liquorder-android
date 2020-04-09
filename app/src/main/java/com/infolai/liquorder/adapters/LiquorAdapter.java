package com.infolai.liquorder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infolai.liquorder.R;
import com.infolai.liquorder.models.FavoriteItem;
import com.infolai.liquorder.models.LiquorItem;
import com.infolai.liquorder.models.MenuItem;
import com.infolai.liquorder.views.items.CategoryItemView;
import com.infolai.liquorder.views.items.LiquorItemView;

import java.util.List;

public class LiquorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CATEGORY = 0;
    private static final int ITEM = 1;

    private List<LiquorItem> items;
    private IOnItemClickListener onItemClickListener;

    public LiquorAdapter(List<LiquorItem> iList, IOnItemClickListener listener) {
        items = iList;
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liquor, parent, false);
        RecyclerView.ViewHolder result = new LiquorItemView(view, onItemClickListener);

        if (viewType == CATEGORY) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
            result = new CategoryItemView(view);
        }

        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        LiquorItem item = items.get(position);

        if (viewType == CATEGORY) {
            ((CategoryItemView) holder).setTvCategory(item.category);
        } else {
            ((LiquorItemView) holder).setLiquorItem(item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        LiquorItem item = items.get(position);

        return (item.category == null) ? ITEM : CATEGORY;
    }
}
