package com.infolai.liquorder.adapters;

import android.view.View;

public interface IOnItemClickListener<T> {
    void onItemClick(View view, T item);
}
