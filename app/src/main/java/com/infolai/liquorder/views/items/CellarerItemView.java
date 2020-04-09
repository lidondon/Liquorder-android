package com.infolai.liquorder.views.items;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.infolai.liquorder.R;
import com.infolai.liquorder.adapters.IOnItemClickListener;
import com.infolai.liquorder.models.Cellarer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CellarerItemView extends BaseItemView {
    @BindView(R.id.clRoot)
    ConstraintLayout clRoot;
    @BindView(R.id.tvFirstWord)
    TextView tvFirstWord;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLiaison)
    TextView tvLiaison;
    @BindView(R.id.tvMobile)
    TextView tvMobile;

    public CellarerItemView(@NonNull View itemView, IOnItemClickListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);
    }

    public void setCellarer(Cellarer cellarer) {
        tvFirstWord.setText(cellarer.name.substring(0, 1));
        tvName.setText(cellarer.name);
        tvLiaison.setText(cellarer.liaison);
        tvMobile.setText(cellarer.mobile);

        clRoot.setOnClickListener(v -> onItemClickListener.onItemClick(view, cellarer));
    }
}
