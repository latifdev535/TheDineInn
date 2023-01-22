package com.thedinehouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.thedinehouse.api.DineHouseApiInterface;
import com.thedinehouse.model.BaseInfoData;
import com.thedinehouse.model.BaseInfoResponse;
import com.thedinehouse.model.Item;
import com.thedinehouse.model.ItemCategory;
import com.thedinehouse.model.OrderLocation;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadData extends AppCompatActivity implements View.OnClickListener, Callback<BaseInfoResponse> {

    private Button btnReload;
    private ProgressBar idLoadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);
        btnReload = findViewById(R.id.btn_ReloadData);
        idLoadingPB = findViewById(R.id.idLoadingPB);
        btnReload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (TheDineHouseConstants.TEST) {
// ============== Test
            DBHandler dbHandler = new DBHandler(this);
            dbHandler.deleteTableData(DBHandler.TB_CATEGORY);
            dbHandler.deleteTableData(DBHandler.TB_LOCATION);
            dbHandler.deleteTableData(DBHandler.TB_ITEM);
            dbHandler.deleteTableData(DBHandler.TB_PAYMENT_METHOD);
            dbHandler.deleteTableData(DBHandler.TB_TRANS_GROUP);
            dbHandler.deleteTableData(DBHandler.TB_WAITER);
            List<ItemCategory> itemCategoryList = TheDineHouseConstants.getCategories();
            List<OrderLocation> locationList = TheDineHouseConstants.getOrderLocation();
            List<Item> itemList = TheDineHouseConstants.getItems();
            List<String> paymentMethods = TheDineHouseConstants.getPayments();
            List<String> serversList = TheDineHouseConstants.getServers();
            List<String> tranGroupList = TheDineHouseConstants.getTransGroup();

            dbHandler.insertCategoryBulk(itemCategoryList);
            dbHandler.insertLocationBulk(locationList);
            dbHandler.insertItemBulk(itemList);
            dbHandler.insertPaymentMethods(paymentMethods);
            dbHandler.insertServers(serversList);
            dbHandler.insertTransType(tranGroupList);
            startActivity(new Intent(this, MainActivity.class));
// ============ Test
        } else {
            TheDineHouseHelper.showLoading(idLoadingPB, btnReload);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TheDineHouseHelper.getBaseURL(this))
                    .addConverterFactory(GsonConverterFactory.create()).build();
            DineHouseApiInterface apiCalls = retrofit.create(DineHouseApiInterface.class);

            Call<BaseInfoResponse> baseInfoResponseCall =
                    apiCalls.getBaseInfoAPI("Bearer " + TheDineHouseHelper.getSharesPref(this, TheDineHouseConstants.ServerSetting.TOKEN));

            baseInfoResponseCall.enqueue(this);
        }
    }

    @Override
    public void onResponse(Call<BaseInfoResponse> call, Response<BaseInfoResponse> response) {
        TheDineHouseHelper.stopLoading(idLoadingPB,btnReload);
        BaseInfoResponse baseInfoResponse = response.body();

        if (baseInfoResponse != null && baseInfoResponse.getStatus().equalsIgnoreCase(TheDineHouseConstants.SUCCESS)) {

            DBHandler dbHandler = new DBHandler(this);
            dbHandler.deleteTableData(DBHandler.TB_CATEGORY);
            dbHandler.deleteTableData(DBHandler.TB_LOCATION);
            dbHandler.deleteTableData(DBHandler.TB_ITEM);
            dbHandler.deleteTableData(DBHandler.TB_PAYMENT_METHOD);
            dbHandler.deleteTableData(DBHandler.TB_TRANS_GROUP);
            dbHandler.deleteTableData(DBHandler.TB_WAITER);

            BaseInfoData data = baseInfoResponse.getData();

            List<ItemCategory> itemCategoryList = data.getCategories();
            List<OrderLocation> locationList = data.getLocations();
            List<Item> itemList = data.getItems();
            List<String> paymentMethods = data.getPaymentMethods();
            List<String> servers = data.getServers();
            List<String> tranGroups = data.getTranGroups();

            dbHandler.insertCategoryBulk(itemCategoryList);
            dbHandler.insertLocationBulk(locationList);
            dbHandler.insertItemBulk(itemList);
            dbHandler.insertPaymentMethods(paymentMethods);
            dbHandler.insertServers(servers);
            dbHandler.insertTransType(tranGroups);

            TheDineHouseHelper.toast(this,"Data loaded successfully..!");
            startActivity(new Intent(this, MainActivity.class));
        } else {
            TheDineHouseHelper.toast(this,"Error response..!");
        }

    }

    @Override
    public void onFailure(Call<BaseInfoResponse> call, Throwable t) {
        TheDineHouseHelper.stopLoading(idLoadingPB,btnReload);
        TheDineHouseHelper.toast(this,"Error calling API ".concat(TheDineHouseHelper.getErrorMessage(t)));
    }

    @Override
    public void onBackPressed() {
        TheDineHouseConstants.emptyCart();
        DBHandler dbHandler = new DBHandler(this);
        Map<Integer,String> categoriesList = dbHandler.getCategoriesList();
        if (categoriesList.isEmpty()) {
            startActivity(new Intent(LoadData.this, Login.class));
        } else {
            startActivity(new Intent(LoadData.this, MainActivity.class));
        }
    }

}