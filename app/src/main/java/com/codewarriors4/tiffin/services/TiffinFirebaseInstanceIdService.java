package com.codewarriors4.tiffin.services;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.codewarriors4.tiffin.TiffinSeeker_Profile;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.DatabaseHelper;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import java.util.HashMap;

public class TiffinFirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {


    SessionUtli sessionUtli;

    private String refreshedToken;
    DatabaseHelper mDatabaseHelper;


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("token", "Refreshed token: " + refreshedToken);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        sessionUtli.setValue("fcmtoken",refreshedToken);
        String token = sessionUtli.getValue("fcmtoken");
        mDatabaseHelper = new DatabaseHelper(this);
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        if(!mDatabaseHelper.checkFCMExists(token)){
            boolean insertData = mDatabaseHelper.addData(token);
        }

    
        //new MyAsynTask().execute("");
        }


    public String getUserInfo(String fcmtoken) throws Exception {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.FCMTOKENSTORE);
        requestPackage.setMethod("POST");
        requestPackage.setParam("fcmToken", fcmtoken);
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        return HttpHelper.downloadFromFeed(requestPackage);
    }

    private class MyAsynTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                return getUserInfo(refreshedToken);
            } catch (Exception e) {
                return e.getMessage();
            }


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.d("JSONVALUE", "done");


        }

    }



}
