package com.thedinehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.thedinehouse.api.DineHouseApiInterface;
import com.thedinehouse.model.BulkExpensesRequest;
import com.thedinehouse.model.BulkExpensesResponse;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BulkExpenses extends AppCompatActivity implements View.OnClickListener, Callback<BulkExpensesResponse> {

    private Spinner spnMethod,spnTransType;
    private EditText amount,description;
    private Button btnSubmitAmount;
    private ProgressBar pbLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk_expenses);

        spnMethod = findViewById(R.id.spnMethod);
        spnTransType = findViewById(R.id.spnTransType);
        amount = findViewById(R.id.amount);
        description = findViewById(R.id.description);
        pbLoading = findViewById(R.id.pbLoading);
        btnSubmitAmount = findViewById(R.id.btnSubmitAmount);
        btnSubmitAmount.setOnClickListener(this);
        spnMethod.setAdapter(new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                TheDineHouseHelper.getPayments(this)));
        spnTransType.setAdapter(new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                TheDineHouseHelper.getTranGroup(this)));

    }

    @Override
    public void onClick(View view) {

        if(validated()){
            TheDineHouseHelper.showLoading(pbLoading,btnSubmitAmount);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(TheDineHouseHelper.getBaseURL(this))
                        .addConverterFactory(GsonConverterFactory.create()).build();
                DineHouseApiInterface apiCalls = retrofit.create(DineHouseApiInterface.class);
            BulkExpensesRequest bulkExpensesRequest = new BulkExpensesRequest();
            bulkExpensesRequest.setUserId(TheDineHouseHelper.getSharesPref(this, TheDineHouseConstants.ServerSetting.USER_ID));
            bulkExpensesRequest.setType(spnTransType.getSelectedItem().toString());
            bulkExpensesRequest.setMethod(spnMethod.getSelectedItem().toString());
            bulkExpensesRequest.setAmount(amount.getText().toString().trim());
            bulkExpensesRequest.setDescription(description.getText().toString().trim());
            Call<BulkExpensesResponse> bulkExpensesResponseCall = apiCalls.
                    bulkExpensesPayment(Arrays.asList(bulkExpensesRequest));
            bulkExpensesResponseCall.enqueue(this);

        }

    }

    private boolean validated() {
        boolean result = true;
        if (spnMethod.getSelectedItemPosition()==0) {
            TheDineHouseHelper.toast(this,"Please select payment method");
            result = false;
        } else if (spnTransType.getSelectedItemPosition()==0) {
            TheDineHouseHelper.toast(this,"Please select type");
            result = false;
        } else if(TheDineHouseHelper.isEditTextEmpty(amount)) {
            TheDineHouseHelper.toast(this,"Please enter amount");
            result = false;
        } else if (TheDineHouseHelper.isEditTextEmpty(description)) {
            TheDineHouseHelper.toast(this,"Please enter description");
            result = false;
        }
        return result;
    }

    @Override
    public void onResponse(Call<BulkExpensesResponse> call, Response<BulkExpensesResponse> response) {
        TheDineHouseHelper.stopLoading(pbLoading,btnSubmitAmount);
        BulkExpensesResponse body = response.body();
        if(body.getStatus().equalsIgnoreCase(TheDineHouseConstants.SUCCESS)) {
            TheDineHouseHelper.toast(this,"Expenses sent successfully");
        } else {
            TheDineHouseHelper.toast(this,"Failed to send Expenses..!");
        }
    }

    @Override
    public void onFailure(Call<BulkExpensesResponse> call, Throwable t) {
        TheDineHouseHelper.stopLoading(pbLoading,btnSubmitAmount);
        TheDineHouseHelper.toast(this,"Failed to send Expenses..! ".concat(TheDineHouseHelper.getErrorMessage(t)));
    }
}