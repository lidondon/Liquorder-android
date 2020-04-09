package com.infolai.liquorder.views.items;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.infolai.liquorder.R;
import com.infolai.liquorder.adapters.IOnItemClickListener;
import com.infolai.liquorder.models.Order;
import com.infolai.liquorder.utilities.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderItemView extends BaseItemView {
    @BindView(R.id.clRoot)
    ConstraintLayout clRoot;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvFormNumber)
    TextView tvFormNumber;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvCreateDate)
    TextView tvCreateDate;


    public OrderItemView(@NonNull View itemView, IOnItemClickListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);
    }

    public void setItem(Order order) {
        tvStatus.setText(order.orderStatus);
        Util.setStatusTextView(context, tvStatus, order.orderStatus);
        tvFormNumber.setText(order.formNumber);
        tvName.setText(order.cellarerName);
        tvCreateDate.setText(order.createDateTime);
        clRoot.setOnClickListener(v -> onItemClickListener.onItemClick(view, order));
    }
}
