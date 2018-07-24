package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TSSubPayment extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int PHONELENGHT = 10;


    private  View view;

    @BindView(R.id.card_name_value)
    EditText card_name;
    @BindView(R.id.card_number_value)
    EditText card_number;
    @BindView(R.id.card_exp_date_mnth)
    EditText card_exp_date_mnth;
    @BindView(R.id.card_exp_date_year)
    EditText card_exp_date_year;
    @BindView(R.id.card_cvv_value)
    EditText card_cvv;


    @BindView(R.id.payment_btn)
    Button payment_btn;

    Context TSViewHMPackageCtx;



    private SessionUtli sessionUtli;
    private FrameLayout progress;
    private LinearLayout profileBody;
    private JsonObject hmPackageDetails;


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
//            String str = (String) intent
//                    .getStringExtra(HttpService.MY_SERVICE_PAYLOAD);


            RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
            if(respondPackage.getParams().containsKey(RespondPackage.SUCCESS)){
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.SUCCESS));
                Toast.makeText(context, "Update Succesfull", Toast.LENGTH_SHORT).show();

            }else{
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.FAILED));
                Toast.makeText(context, "Please Select Image", Toast.LENGTH_SHORT).show();
            }

        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Subscription Payment");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ts_sub_payment);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);
        profileBody = findViewById(R.id.profile_body);
        progress = findViewById(R.id.progress_overlay);
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = getLayoutInflater().inflate(R.layout.login_layout, container, false);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        new MyAsynTask().execute("");
    }


    @OnClick(R.id.payment_btn)
    public void submit(View view){
        checkValidation();
    }

    private void checkValidation()
    {
        String getCardName = card_name.getText().toString();
        String getCadNum = card_number.getText().toString();
        String getCardExpMnth = card_exp_date_mnth.getText().toString();
        String getCardExpYear = card_exp_date_year.getText().toString();
        String getCardCVV = card_cvv.getText().toString();



        if(getCardName.equals("") || getCadNum.equals("") || getCardExpMnth.equals("") || getCardExpYear.equals("") || getCardCVV.equals("")){
            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");
        }else if(!getCadNum.equals("1111222233334444")){
            new CustomToast().Show_Toast(this, view,
                    "Invalid card");
        }
        else{
            submit();
        }

    }

    public void submit(){

        Intent i = getIntent();



        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.TSMAKEPAYMENT);
        requestPackage.setMethod("POST");
        requestPackage.setParam("card_number", card_number.getText().toString().trim());
        requestPackage.setParam("expiration_month", card_exp_date_mnth.getText().toString().trim());
        requestPackage.setParam("expiration_year", card_exp_date_year.getText().toString().trim());
        requestPackage.setParam("cvc", card_cvv.getText().toString().trim());

        requestPackage.setParam("HMPid",  i.getStringExtra("package_id"));
        requestPackage.setParam("subtotal",  i.getStringExtra("package_cost"));

        requestPackage.setParam("hst",  i.getStringExtra("package_hst"));
        requestPackage.setParam("total",  i.getStringExtra("package_total"));
        requestPackage.setParam("HomeMakerId",  i.getStringExtra("HomeMakerId"));







        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra(HttpService.REQUEST_PACKAGE, requestPackage);

        startService(intent);
    }


    public String makePayment() throws Exception {
 /*       Intent i = getIntent();
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.TSMAKEPAYMENT);
        requestPackage.setMethod("POST");
        requestPackage.setParam("HMPId", i.getStringExtra("package_id"));
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        return HttpHelper.downloadFromFeed(requestPackage);*/
 return "success";
    }

    private class MyAsynTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
              return makePayment();
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
            /*profileBody.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);*/

        }

        @Override
        protected void onPostExecute(String aVoid) {
 /*           try {
                Log.d("Testing data", "onPostExecute: " + aVoid);
                super.onPostExecute(aVoid);
               *//* profileBody.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);*//*
                JsonObject jsonObject = new Gson().fromJson(aVoid, JsonObject.class);
                hmPackageDetails = jsonObject.get("home_maker_packages")       // get the 'user' JsonElement
                        .getAsJsonObject(); // get it as a JsonObject

                //System.out.println(name);

                // HashMap<String, Object> hashMap = new Gson().fromJson(aVoid, HashMap.class);
           *//*     for (String key : hashMap.keySet()) {
                    Log.d("JSONVALUE", key + ": " + hashMap.get(key));
                }*//*

                Log.d("JSONVALUE", "test");

                //if(hashMap.get("UserZipCode") != null)
                initValues(hmPackageDetails);
            }

            catch(Exception e){
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG);
            }*/
        }

    }


    private void initValues(final JsonObject hmPackageDetails)
    {




    }

    /*    private int getIndex(Spinner spinner, String myString){

            int index = 0;

            for (int i=0;i<spinner.getCount();i++){
                if (spinner.getItemAtPosition(i).equals(myString)){
                    index = i;
                }
            }
            return index;
        }*/
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }





}
