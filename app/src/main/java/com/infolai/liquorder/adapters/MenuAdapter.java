package com.infolai.liquorder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infolai.liquorder.R;
import com.infolai.liquorder.models.MenuItem;
import com.infolai.liquorder.views.items.CategoryItemView;
import com.infolai.liquorder.views.items.EmptyFooterView;
import com.infolai.liquorder.views.items.MenuItemView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int FOOTER = 0;
    private static final int CATEGORY = 1;
    private static final int ITEM = 2;

    private List<MenuItem> items;
    private IOnItemClickListener onItemClickListener;
    private int parentHeight;
    private int categoryHeight;
    private int itemHeight;

    public MenuAdapter(List<MenuItem> iList, IOnItemClickListener listener) {
        items = iList;
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        RecyclerView.ViewHolder result = new MenuItemView(view, onItemClickListener);

        if (viewType == CATEGORY) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
            result = new CategoryItemView(view);
        }
//        else if (viewType == FOOTER) {
//            view = new View(parent.getContext());
//            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, parentHeight - categoryHeight - itemHeight));
//            result = new EmptyFooterView(view);
//        }
//        final View tempView = view;
//        view.post(() -> {
//            parentHeight = (parentHeight == 0) ? parent.getHeight() : parentHeight;
//            if (viewType == CATEGORY) {
//                categoryHeight = (categoryHeight == 0) ? tempView.getHeight() : categoryHeight;
//            } else if (viewType == ITEM) {
//                itemHeight = (itemHeight == 0) ?  tempView.getHeight() : itemHeight;
//            }
//        });

        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < items.size()) {
            int viewType = getItemViewType(position);
            MenuItem item = items.get(position);

            if (viewType == CATEGORY) {
                ((CategoryItemView) holder).setTvCategory(item.category);
            } else {
                ((MenuItemView) holder).setMenuItem(item);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        //int result = FOOTER;

//        if (position < items.size()) result = (items.get(position).category == null) ? ITEM : CATEGORY;
//
//        return result;

        return (items.get(position).category == null) ? ITEM : CATEGORY;
    }
}
