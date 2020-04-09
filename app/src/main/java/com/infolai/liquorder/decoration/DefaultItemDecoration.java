package com.infolai.liquorder.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DefaultItemDecoration extends RecyclerView.ItemDecoration {
    private int horizontalSpace;
    private int verticalSpace;

    public DefaultItemDecoration(int hSpace, int vSpace) {
        horizontalSpace = hSpace;
        verticalSpace = vSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = horizontalSpace;
        outRect.right = horizontalSpace;
        outRect.bottom = verticalSpace;
        if (parent.getChildAdapterPosition(view) == 0) outRect.top = verticalSpace;
    }
}
