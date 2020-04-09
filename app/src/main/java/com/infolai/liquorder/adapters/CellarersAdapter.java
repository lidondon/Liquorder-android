package com.infolai.liquorder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infolai.liquorder.R;
import com.infolai.liquorder.models.Cellarer;
import com.infolai.liquorder.views.items.CellarerItemView;

import java.util.List;

public class CellarersAdapter extends RecyclerView.Adapter<CellarerItemView> {
    private List<Cellarer> cellarers;
    private IOnItemClickListener onItemClickListener;

    public CellarersAdapter(List<Cellarer> cList, IOnItemClickListener listener) {
        cellarers = cList;
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public CellarerItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cellarer, parent, false);

        return new CellarerItemView(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CellarerItemView holder, int position) {
        holder.setCellarer(cellarers.get(position));
    }

    @Override
    public int getItemCount() {
        return cellarers.size();
    }
}
