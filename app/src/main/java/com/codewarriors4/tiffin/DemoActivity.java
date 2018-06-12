package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codewarriors4.tiffin.R;
import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;

import java.util.Timer;
import java.util.TimerTask;

public class DemoActivity extends AppCompatActivity{
    private ProgressBar progressBar;
    private TextView textView;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
            if(respondPackage.getParams().containsKey(RespondPackage.SUCCESS)){
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.SUCCESS));
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context,
                        respondPackage.getParams().get(RespondPackage.SUCCESS), Toast.LENGTH_LONG)
                        .show();
                textView.append(respondPackage.getParams().get(RespondPackage.SUCCESS));
            }else{
                //submitButton.setEnabled(true);
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.FAILED));
                Toast.makeText(context,
                        respondPackage.getParams().get(RespondPackage.FAILED), Toast.LENGTH_SHORT)
                        .show();
                progressBar.setVisibility(View.GONE);
            }

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout);
        progressBar = findViewById(R.id.demoProgress);
        progressBar.setVisibility(View.VISIBLE);
        String response = getIntent().getStringExtra("access_token");
        Log.d("DemoActivityResponse", ""+response);
        textView = (TextView)findViewById(R.id.textView4);
        textView.append(response);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        
        getUserInformation(response);
    }

    private void getUserInformation(String str)
    {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.USER);
        requestPackage.setHeader("Authorization", "Bearer " +str);
        requestPackage.setHeader("Accept", "application/json");
        requestPackage.setMethod("POST");
        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra(HttpService.REQUEST_PACKAGE, requestPackage);
        startService(intent);
    }


    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }
}
