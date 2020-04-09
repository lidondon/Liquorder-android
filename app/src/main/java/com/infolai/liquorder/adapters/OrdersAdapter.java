package com.infolai.liquorder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infolai.liquorder.R;
import com.infolai.liquorder.models.BaseItem;
import com.infolai.liquorder.models.Order;
import com.infolai.liquorder.views.items.CartItemView;
import com.infolai.liquorder.views.items.OrderItemView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrderItemView> {
    private List<Order> items;
    private IOnItemClickListener onItemClickListener;

    public OrdersAdapter(List<Order> iList, IOnItemClickListener listener) {
        items = iList;
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public OrderItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);

        return new OrderItemView(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemView holder, int position) {
        holder.setItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
