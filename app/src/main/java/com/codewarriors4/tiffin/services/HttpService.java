package com.codewarriors4.tiffin.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;


import java.io.IOException;

public class HttpService extends IntentService {

    public static final String TAG = "HttpService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";
    public static final String REQUEST_PACKAGE = "requestPackage";

    public HttpService() {
        super("HttpService");
    }

    protected void onHandleIntent(Intent intent) {
        RequestPackage requestPackage =
                intent.getParcelableExtra(REQUEST_PACKAGE);
        RespondPackage respondPackage = new RespondPackage();
        String response;
        try {
            response = HttpHelper.downloadFromFeed(requestPackage);
            respondPackage.setParam(RespondPackage.SUCCESS, response);
            Log.d("HttpResponse", "onHandleIntent: " + response.toString());
        } catch (Exception e) {

            Log.d("HttpResponse", "onHandleIntent_ERROR: " +  e.getMessage());
            response = e.getMessage();
            respondPackage.setParam(RespondPackage.FAILED, response);
        }

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, respondPackage);
        //messageIntent.putExtra(MY_SERVICE_PAYLOAD, response);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }


}
