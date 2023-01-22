package com.thedinehouse.api;

import com.thedinehouse.model.BaseInfoResponse;
import com.thedinehouse.model.BillResponse;
import com.thedinehouse.model.BulkExpensesRequest;
import com.thedinehouse.model.BulkExpensesResponse;
import com.thedinehouse.model.LoginRequest;
import com.thedinehouse.model.LoginResponse;
import com.thedinehouse.model.OrderPaymentRequest;
import com.thedinehouse.model.OrderPaymentResponse;
import com.thedinehouse.model.OrderRequest;
import com.thedinehouse.model.OrderResponse;
import com.thedinehouse.model.OrderResponseInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DineHouseApiInterface {

    String BASE_URL = "/dinehouse/api/v1/";
    String LOGIN_URL = "login";
    String BASE_INFO_URL = "baseInfo";
    String ORDER_URL = "order";
    String ORDERS_URL = "orders";
    String BILL_URL = "bill";
    String ORDER_PAYMENT_URL = "order-payment";
    String BULK_EXPENSES_URL = "expenses";

    @POST(LOGIN_URL)
    Call<LoginResponse> loginAPI(@Body LoginRequest dataModal);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET(BASE_INFO_URL)
    Call<BaseInfoResponse> getBaseInfoAPI(@Header("Authorization") String token);

    @POST(ORDER_URL)
    Call<OrderResponse> orderAPI(@Body OrderRequest orderRequest);

    @PUT(ORDER_URL)
    Call<OrderResponse> updateOrderAPI(@Body OrderRequest orderRequest);

    @GET(ORDERS_URL+"/{userId}")
    Call<OrderResponseInfo> getOrderAPI(@Path(value = "userId") String userId);

    @GET(ORDERS_URL)
    Call<OrderResponseInfo> getAllOrderAPI();

    @GET(BILL_URL+"/{orderId}")
    Call<BillResponse> getBillAPI(@Path(value = "orderId") String orderId);

    @POST(ORDER_PAYMENT_URL)
    Call<OrderPaymentResponse> orderPayment(@Body OrderPaymentRequest orderRequest);

    @POST(BULK_EXPENSES_URL)
    Call<BulkExpensesResponse> bulkExpensesPayment(@Body List<BulkExpensesRequest> orderRequestList);


}
