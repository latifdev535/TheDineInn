package com.thedinehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.thedinehouse.adapters.ItemAdapter;
import com.thedinehouse.model.Item;

import org.apache.commons.collections4.map.LinkedMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SearchItem extends AppCompatActivity implements TextWatcher {

    private EditText etSearchItem;
    private ListView searchListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        etSearchItem = findViewById(R.id.etSearchItem);
        searchListView = findViewById(R.id.llSearchItems);
        etSearchItem.addTextChangedListener(this);
        List<Item> allMenuItems = getAllMenuItems();
        loadAllItems(allMenuItems);
    }

    List<Item> getAllMenuItems(){
        DBHandler dbHandler = new DBHandler(this);
        return dbHandler.getItemsList();
    }

    private void loadAllItems(List<Item> itemsList) {
        loadExistingOrder(itemsList);
        ItemAdapter itemAdapter = new ItemAdapter(itemsList,this);
        searchListView.setAdapter(itemAdapter);

    }

    private void loadExistingOrder(List<Item> itemsList) {
        Collection<Item> items = TheDineHouseConstants.getCartItems().values();
        if (!items.isEmpty()) {
            for (Item item : itemsList){
                for (Item i : items) {
                    if (item.getItemId().equals(i.getItemId())) {
                        item.setQuantity(i.getQuantity());
                        TheDineHouseConstants.getCartItems().put(item.getItemId(),item);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (etSearchItem.getText().toString().length() >0) {
            List<Item> filteredMenuItems = new ArrayList<>();
            List<Item> menuItems = getAllMenuItems();

            for (Item item : menuItems){
                if (searchedItem(item,etSearchItem)){
                    filteredMenuItems.add(item);
                }
            }

            loadAllItems(filteredMenuItems);
        } else {
            loadAllItems(getAllMenuItems());
        }
    }

    private boolean searchedItem(Item item, EditText etSearchItem) {
        boolean contains = item.getName().toLowerCase().contains(etSearchItem.getText().toString().toLowerCase());
        if(!contains) {
            String[] strings = item.getName().split(" ");
            String searchString = TheDineHouseConstants.EMPTY;
            for (String str : strings){
                char c = str.charAt(0);
                searchString = searchString.concat(String.valueOf(c).toLowerCase());
            }
            return searchString.startsWith(etSearchItem.getText().toString().trim().toLowerCase());
        }
        return true;
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}