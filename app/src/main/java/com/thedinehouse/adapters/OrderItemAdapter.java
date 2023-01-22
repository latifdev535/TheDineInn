package com.thedinehouse.adapters;

import android.app.Activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thedinehouse.R;
import com.thedinehouse.TheDineHouseConstants;
import com.thedinehouse.model.Item;
import com.thedinehouse.model.OrderResponse;

import java.util.List;

public class OrderItemAdapter extends ArrayAdapter<OrderResponse> {

    private final Activity context;
    private final List<OrderResponse> ordersList;

    public OrderItemAdapter(Activity context,List<OrderResponse> ordersList) {
        super(context, R.layout.order_item,ordersList);
        this.context=context;
        this.ordersList = ordersList;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.order_item, null,true);

        StringBuilder itemsString = new StringBuilder();
        TextView titleText =  rowView.findViewById(R.id.orderId);
        TextView items =  rowView.findViewById(R.id.orderItems);
        TextView tableName = rowView.findViewById(R.id.tableName);
        TextView tablePrice = rowView.findViewById(R.id.tablePrice);
        TextView tableStatus = rowView.findViewById(R.id.tableStatus);
        OrderResponse orderResponse = ordersList.get(position);

        titleText.setText(String.valueOf(orderResponse.getId()));
        tableName.setText(orderResponse.getAddress());
        tablePrice.setText(orderResponse.getPrice());
        tableStatus.setText(orderResponse.getStatus());
        if (orderResponse.getStatus().equalsIgnoreCase(TheDineHouseConstants.ORDER_STATUS_PAID)) {
            tableStatus.setTextColor(Color.parseColor(TheDineHouseConstants.GREEN_COLOR_CODE));
        }

        for (Item item : orderResponse.getOrderItems()) {
            itemsString.append(item.getItemName()).append("("+item.getQuantity()+")").append(" | ");
        }

        items.setText(itemsString.append("--- served by : ").append(orderResponse.getServedBy()));

        return rowView;

    };
}