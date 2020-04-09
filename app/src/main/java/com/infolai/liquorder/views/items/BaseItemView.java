package com.infolai.liquorder.views.items;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infolai.liquorder.adapters.IOnItemClickListener;

import butterknife.Unbinder;

public class BaseItemView extends RecyclerView.ViewHolder {
    public Context context;
    public View view;
    public IOnItemClickListener onItemClickListener;

    public BaseItemView(@NonNull View itemView, IOnItemClickListener listener) {
        super(itemView);
        context = itemView.getContext();
        view = itemView;
        onItemClickListener = listener;
    }

}
