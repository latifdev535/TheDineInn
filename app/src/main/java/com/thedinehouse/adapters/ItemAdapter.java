package com.thedinehouse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.thedinehouse.R;
import com.thedinehouse.TheDineHouseConstants;
import com.thedinehouse.model.Item;

import java.util.List;
import java.util.Objects;

public class ItemAdapter extends BaseAdapter  {
    private List<Item> itemList;
    private Context context;

    public ItemAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Item getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.list_item, null);

        //set layout for displaying items
        TextView text = (TextView) view.findViewById(R.id.itemName);
        TextView price = view.findViewById(R.id.price);
        TextView quantity = (TextView) view.findViewById(R.id.quantity);

        Item item = itemList.get(position);
        text.setText(item.getName());
        if (item.isVeg()){
            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.vegan, 0);
        } else {
            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.meat, 0);
        }
        quantity.setText(String.valueOf(item.getQuantity()));
        price.setText(String.valueOf(item.getPrice()));

        Button btnIncrement = view.findViewById(R.id.increment);
        Button btnDecrement = view.findViewById(R.id.decrement);

        btnIncrement.setOnClickListener(btnView -> modifyItemInventory(item,quantity,item.getQuantity()+1));
        btnDecrement.setOnClickListener(btnView -> modifyItemInventory(item,quantity,item.getQuantity()-1));
        return view;
    }


    void modifyItemInventory(Item item, TextView quantity, int count){
        if(count<0) return;
        quantity.setText(String.valueOf(count));
        item.setQuantity(count);
        if (TheDineHouseConstants.getCartItems().containsKey(item.getItemId())) {
            if (count == 0) {
                TheDineHouseConstants.getCartItems().remove(item.getItemId());
            } else {
                Objects.requireNonNull(TheDineHouseConstants.getCartItems().get(item.getItemId())).setQuantity(count);
            }
        } else {
            TheDineHouseConstants.getCartItems().put(item.getItemId(),item);
        }

    }

}
