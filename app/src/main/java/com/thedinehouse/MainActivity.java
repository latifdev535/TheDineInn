package com.thedinehouse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.thedinehouse.model.Item;

import org.apache.commons.collections4.map.LinkedMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private LinkedMap<String, ArrayList<Item>> menuItems;
    private ViewPageAdapter viewPageAdapter;
    private SwitchCompat vegan;
    private TextView edit_tableName;
    private TextView etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vegan = findViewById(R.id.vegan);
        edit_tableName = findViewById(R.id.edit_tableName);
        etSearch = findViewById(R.id.etSearch);
        etSearch.setOnClickListener(this);
        vegan.setOnCheckedChangeListener(this);
        menuItems = getMenuItems(getIntent().getExtras(),false);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        viewPageAdapter = new ViewPageAdapter(this,menuItems);
        viewPager2.setAdapter(viewPageAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(menuItems.get(position))).attach();

    }

    private LinkedMap<String, ArrayList<Item>> getMenuItems(Bundle bundle, boolean vegan) {

        menuItems = new LinkedMap();
        DBHandler dbHandler = new DBHandler(this);
        Map<Integer,String> categoriesMap = dbHandler.getCategoriesList();
        List<Item> itemsList = dbHandler.getItemsList();

        if (bundle != null) {
            loadPastOrder(bundle,itemsList);
        } else {
            loadExistingOrder(itemsList);
        }

        for (Item item : itemsList) {
            Integer categoryId = item.getCategoryId();
            if (menuItems.containsKey(categoriesMap.get(categoryId))) {
                if (vegan) {
                    if (item.isVeg()) {
                        menuItems.get(categoriesMap.get(categoryId)).add(item);
                    }
                } else {
                    menuItems.get(categoriesMap.get(categoryId)).add(item);
                }
            } else {
                if (vegan) {
                    if (item.isVeg()) {
                        ArrayList<Item> list = new ArrayList<>(10);
                        list.add(item);
                        menuItems.put(categoriesMap.get(categoryId), list);
                    }
                } else {
                    ArrayList<Item> list = new ArrayList<>(10);
                    list.add(item);
                    menuItems.put(categoriesMap.get(categoryId),list);
                }
            }
        }
        return menuItems;
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

    private void loadPastOrder(Bundle bundle, List<Item> itemsList) {
        ArrayList<Item> items = (ArrayList<Item>) bundle.getSerializable(TheDineHouseConstants.CART);
        if (items != null) {
            for (Item item : itemsList) {
                for (Item i : items) {
                    if (item.getItemId().equals(i.getItemId())) {
                        item.setQuantity(i.getQuantity());
                        TheDineHouseConstants.getCartItems().put(item.getItemId(), item);
                    }
                }
            }
            String orderId = bundle.getString(TheDineHouseConstants.ORDER_ID);
            String tableName = bundle.getString(TheDineHouseConstants.TABLE_NAME);
            String tableServer = bundle.getString(TheDineHouseConstants.TABLE_SERVER);
            edit_tableName.setText(tableName);
            edit_tableName.setTag(orderId);
            edit_tableName.setTag(R.id.table_server,tableServer);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order_menu, menu);
        MenuItem item = menu.findItem(R.id.action_expenses);
        if(TheDineHouseHelper.getSharesPref(this, TheDineHouseConstants.ServerSetting.ADMIN).equalsIgnoreCase(TheDineHouseConstants.ServerSetting.ADMIN.name())) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_cart:

                if (TheDineHouseConstants.getCartItems().isEmpty()) {
                    TheDineHouseHelper.showOkDialog(MainActivity.this,"No items in cart");
                } else {
                    Intent intent = new Intent(this, Cart.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TheDineHouseConstants.CART, new ArrayList(TheDineHouseConstants.getCartItems().values()));
                    if (edit_tableName.getTag()!=null) {
                        bundle.putString(TheDineHouseConstants.ORDER_ID, edit_tableName.getTag().toString());
                        bundle.putString(TheDineHouseConstants.TABLE_NAME, edit_tableName.getText().toString());
                        bundle.putString(TheDineHouseConstants.TABLE_SERVER, edit_tableName.getTag(R.id.table_server).toString());
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return true;
            case R.id.action_reload:

                if(TheDineHouseConstants.getCartItems().isEmpty()){
                    startActivity(new Intent(this,LoadData.class));
                } else {
                     new AlertDialog.Builder(
                            this).setTitle("Warning..!").setMessage("Your cart items will be lost")
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, id) -> {
                                TheDineHouseConstants.emptyCart();
                                dialog.cancel();
                                startActivity(new Intent(MainActivity.this,LoadData.class));

                            }).setNeutralButton("Cancel", (dialog, id) -> dialog.cancel()).create().show();

                }
                return true;
            case R.id.action_orders:
                if(TheDineHouseConstants.getCartItems().isEmpty()){
                    startActivity(new Intent(this,OrdersList.class));
                } else {
                    new AlertDialog.Builder(
                            this).setTitle("Warning..!").setMessage("Your cart items will be lost")
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, id) -> {
                                TheDineHouseConstants.emptyCart();
                                dialog.cancel();
                                startActivity(new Intent(this,OrdersList.class));

                            }).setNeutralButton("Cancel", (dialog, id) -> dialog.cancel()).create().show();

                }

                return true;
            case R.id.action_clear_cart:
                if(TheDineHouseConstants.getCartItems().isEmpty()){
                    TheDineHouseConstants.emptyCart();
                    edit_tableName.setText(TheDineHouseConstants.EMPTY);
                    edit_tableName.setTag(TheDineHouseConstants.EMPTY);
                    edit_tableName.setTag(R.id.table_server,TheDineHouseConstants.EMPTY);
                    reloadMenu(getMenuItems(null, false));
                } else {
                    new AlertDialog.Builder(
                            this).setTitle("Warning..!").setMessage("Your cart items will be lost")
                            .setCancelable(false)
                            .setPositiveButton("Ok", (dialog, id) -> {
                                TheDineHouseConstants.emptyCart();
                                edit_tableName.setText(TheDineHouseConstants.EMPTY);
                                edit_tableName.setTag(TheDineHouseConstants.EMPTY);
                                edit_tableName.setTag(R.id.table_server,TheDineHouseConstants.EMPTY);
                                reloadMenu(getMenuItems(null, false));
                                dialog.cancel();

                            }).setNeutralButton("Cancel", (dialog, id) -> dialog.cancel()).create().show();
                }
                return true;
            case R.id.action_expenses:
                startActivity(new Intent(this,BulkExpenses.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("DO YOU REALLY WANT TO EXIT ?")
                    .setCancelable(false)
                    .setPositiveButton("YES", (dialog, i) -> {
                        finishAffinity();
                        finish();
                    })
                    .setNegativeButton("NO", (dialog, i) -> dialog.cancel()).create().show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean vegan) {
        LinkedMap<String, ArrayList<Item>> menuItems;
        if (vegan) {
            menuItems = getMenuItems(null, true);
        } else {
            menuItems = getMenuItems(null,false);
        }
        reloadMenu(menuItems);
    }

    private void reloadMenu(LinkedMap<String, ArrayList<Item>> menuItems){
        viewPageAdapter = new ViewPageAdapter(this,menuItems);
        viewPager2.setAdapter(viewPageAdapter);
        viewPageAdapter.notifyDataSetChanged();
        viewPager2.invalidate();
        viewPager2.setCurrentItem(tabLayout.getSelectedTabPosition());
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this,SearchItem.class));
    }
}