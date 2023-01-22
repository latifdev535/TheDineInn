package com.thedinehouse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.thedinehouse.model.Item;

import org.apache.commons.collections4.map.LinkedMap;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentStateAdapter {

    private LinkedMap<String,ArrayList<Item>> data;

    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity,LinkedMap<String,ArrayList<Item>> data) {
        super(fragmentActivity);
        this.data = data;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new BlankFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TheDineHouseConstants.TAB_KEY,data.getValue(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
