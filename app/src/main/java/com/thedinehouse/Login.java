package com.thedinehouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.thedinehouse.api.DineHouseApiInterface;
import com.thedinehouse.model.LoginData;
import com.thedinehouse.model.LoginRequest;
import com.thedinehouse.model.LoginResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity implements View.OnClickListener, Callback<LoginResponse> {

    private Button login;
    private EditText ed_username,ed_password;
    private ProgressBar idLoadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        idLoadingPB = findViewById(R.id.idLoadingPB);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(this);
        ed_username.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this,ServerSettings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {

        if(TheDineHouseConstants.TEST) {
//        --------- Test
            TheDineHouseHelper.saveSharedPref(this, TheDineHouseConstants.ServerSetting.ADMIN, TheDineHouseConstants.ServerSetting.ADMIN.name());
            TheDineHouseHelper.saveSharedPref(this, TheDineHouseConstants.ServerSetting.USER_ID, "latif");

            DBHandler dbHandler = new DBHandler(this);
            Map<Integer, String> categoriesList = dbHandler.getCategoriesList();

            if (categoriesList.isEmpty()) {
                startActivity(new Intent(Login.this, LoadData.class));
            } else {
                startActivity(new Intent(Login.this, MainActivity.class));
            }
//        ----Test
        }else {
            if (validate()) {
                TheDineHouseHelper.showLoading(idLoadingPB, login);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(TheDineHouseHelper.getBaseURL(this))
                        .addConverterFactory(GsonConverterFactory.create()).build();
                DineHouseApiInterface apiCalls = retrofit.create(DineHouseApiInterface.class);
                Call<LoginResponse> loginResponseCall = apiCalls.
                        loginAPI(new LoginRequest(TheDineHouseHelper.getText(ed_username),
                                TheDineHouseHelper.getText(ed_password),
                                TheDineHouseHelper.getDeviceId(this)));
                loginResponseCall.enqueue(this);
            }
        }

    }

    private boolean validate() {
        boolean result = true;
        if (TheDineHouseHelper.isEditTextEmpty(ed_username)){
            ed_username.setError("Please enter user name");
            TheDineHouseHelper.toast(this,"Please enter user name");
            result = false;
            ed_username.requestFocus();
        } else if (TheDineHouseHelper.isEditTextEmpty(ed_password)) {
            ed_password.setError("Please enter password");
            TheDineHouseHelper.toast(this,"Please enter password");
            result = false;
            ed_password.requestFocus();
        }
        return result;
    }

    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        TheDineHouseHelper.stopLoading(idLoadingPB,login);

        LoginResponse loginResponse = response.body();
        if(loginResponse != null && loginResponse.getStatus().equalsIgnoreCase(TheDineHouseConstants.SUCCESS)){
            LoginData responseData = loginResponse.getData();
            String token = responseData.getToken();
            String userId = responseData.getUserId();

            TheDineHouseHelper.saveSharedPref(this, TheDineHouseConstants.ServerSetting.USER_ID,userId);
            TheDineHouseHelper.saveSharedPref(this, TheDineHouseConstants.ServerSetting.TOKEN,token);

            if (responseData.isAdmin()) {
                TheDineHouseHelper.saveSharedPref(this, TheDineHouseConstants.ServerSetting.ADMIN,TheDineHouseConstants.ServerSetting.ADMIN.name());
            } else {
                TheDineHouseHelper.saveSharedPref(this, TheDineHouseConstants.ServerSetting.ADMIN,TheDineHouseConstants.EMPTY);
            }

            TheDineHouseHelper.toast(this,"Succssfully loged in..!");
            DBHandler dbHandler = new DBHandler(this);
            Map<Integer,String> categoriesList = dbHandler.getCategoriesList();
            if (categoriesList.isEmpty()) {
                startActivity(new Intent(Login.this, LoadData.class));
            } else {
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        } else {
            TheDineHouseHelper.toast(this,"Invalid login..!");
        }
    }

    @Override
    public void onFailure(Call<LoginResponse> call, Throwable t) {
        TheDineHouseHelper.stopLoading(idLoadingPB,login);
        TheDineHouseHelper.toast(this,"Error calling login API..!".concat(TheDineHouseHelper.getErrorMessage(t)));
    }
}