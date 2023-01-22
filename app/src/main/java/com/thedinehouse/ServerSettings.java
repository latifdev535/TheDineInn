package com.thedinehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thedinehouse.api.DineHouseApiInterface;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ServerSettings extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText ed_IpAddress;
    private Button btn_saveIpAddr;
    private TextView txt_deviceId,txt_IpAdress;
    private LinearLayout llIpAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_settings);

        ed_IpAddress = findViewById(R.id.ed_serverIP);
        btn_saveIpAddr = findViewById(R.id.btn_saveIpAddr);
        txt_deviceId = findViewById(R.id.txt_deviceId);
        txt_IpAdress = findViewById(R.id.txt_IpAdress);
        llIpAddress = findViewById(R.id.ipAddressLl);
        ed_IpAddress.addTextChangedListener(this);
        txt_deviceId.setText(TheDineHouseHelper.getDeviceId(ServerSettings.this));
        txt_IpAdress.setText(TheDineHouseHelper.getBaseURL(this));
        List<String> routerIPList = getRouterIPList();
        for (String ipAddr : routerIPList) {
            TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setText(ipAddr);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ed_IpAddress.setText(ipAddr.concat(":8080"));
                }
            });
            llIpAddress.addView(tv);
        }

        btn_saveIpAddr.setOnClickListener(this);

    }

    public static List<String> getRouterIPList(){
        List<String> ipAddresses = new ArrayList<>();
        try{
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                            ipAddresses.add(inetAddress.getHostAddress().toString());
                        }

                    }
                }

        }catch (Exception e){
        }
        return ipAddresses;
    }


    @Override
    public void onClick(View view) {
        TheDineHouseHelper.saveSharedPref(ServerSettings.this, TheDineHouseConstants.ServerSetting.IP_ADDRESS,ed_IpAddress.getText().toString());
        startActivity(new Intent(ServerSettings.this,Login.class));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        txt_IpAdress.setText("http://".concat(ed_IpAddress.getText().toString()).concat(DineHouseApiInterface.BASE_URL));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}