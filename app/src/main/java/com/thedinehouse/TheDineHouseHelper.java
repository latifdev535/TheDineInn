package com.thedinehouse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thedinehouse.api.DineHouseApiInterface;
import com.thedinehouse.model.OrderLocation;

import java.util.ArrayList;
import java.util.List;

public class TheDineHouseHelper {


    public static void saveSharedPref(Context context,TheDineHouseConstants.ServerSetting key,String value){
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(TheDineHouseConstants.SharedPrefVersion,Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(key.name(), value);
        myEdit.commit();
    }

    public static String getSharesPref(Context context,TheDineHouseConstants.ServerSetting key){
        // Retrieving the value using its keys the file name
        SharedPreferences sh = context.getSharedPreferences(TheDineHouseConstants.SharedPrefVersion, Context.MODE_PRIVATE);
        String value = sh.getString(key.name(), TheDineHouseConstants.EMPTY);

        return value;
    }


    public static void toast(Context context, String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void showOkDialog(Context context,String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(TheDineHouseConstants.OK, null)
                .show();
    }

    public static boolean isEditTextEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    public static String getBaseURL(Context context) {
        String ipAddress = getSharesPref(context, TheDineHouseConstants.ServerSetting.IP_ADDRESS);
        return "http://".concat(ipAddress).concat(DineHouseApiInterface.BASE_URL);
    }

    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static String getDeviceId(Context context) {
        String id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return id;
    }

    public static void showLoading(ProgressBar loadingPB, Button button) {
        loadingPB.setVisibility(View.VISIBLE);
        button.setVisibility(View.GONE);
    }

    public static void stopLoading(ProgressBar loadingPB, Button button) {
        loadingPB.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }

    public static String getErrorMessage(Throwable throwable) {
        return throwable.getLocalizedMessage();
    }

    public static List<String> getPayments(Context context) {
        List<String> paymentTypes = new ArrayList<>();
        paymentTypes.add(TheDineHouseConstants.PLEASE_SELECT);
        DBHandler dbHandler = new DBHandler(context);
        paymentTypes.addAll(dbHandler.getPaymentList());
        return paymentTypes;
    }

    public static List<String > getTranGroup(Context context) {
        List<String> paymentTypes = new ArrayList<>();
        paymentTypes.add(TheDineHouseConstants.PLEASE_SELECT);
        DBHandler dbHandler = new DBHandler(context);
        paymentTypes.addAll(dbHandler.getTranGroupList());
        return paymentTypes;
    }

    public static List<String> getTables(Context context) {
        DBHandler dbHandler = new DBHandler(context);
        List<String> orderLocationList = dbHandler.getOrderLocationList();
        List<String> tableNames = new ArrayList<>();
        tableNames.add(TheDineHouseConstants.PLEASE_SELECT);
        tableNames.addAll(orderLocationList);
        return tableNames;
    }

    public static List<String> getServers(Context context) {
        DBHandler dbHandler = new DBHandler(context);
        List<String> serverList = dbHandler.getServersList();
        List<String> tableNames = new ArrayList<>();
        tableNames.add(TheDineHouseConstants.PLEASE_SELECT);
        tableNames.addAll(serverList);
        return tableNames;
    }
}
