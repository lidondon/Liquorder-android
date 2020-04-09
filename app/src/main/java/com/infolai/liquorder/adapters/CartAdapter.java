package com.infolai.liquorder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infolai.liquorder.R;
import com.infolai.liquorder.models.BaseItem;
import com.infolai.liquorder.views.items.CartItemView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartItemView> {
    private List<BaseItem> items;
    private IOnItemClickListener onItemClickListener;

    public CartAdapter(List<BaseItem> iList, IOnItemClickListener listener) {
        items = iList;
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public CartItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);

        return new CartItemView(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemView holder, int position) {
        holder.setItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
