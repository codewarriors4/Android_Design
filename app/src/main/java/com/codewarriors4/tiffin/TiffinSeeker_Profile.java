package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.codewarriors4.tiffin.utils.Utils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TiffinSeeker_Profile extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int PHONELENGHT = 10;


    private  View view;
    @BindView(R.id.first_name_tiffin)
    EditText firstNameView;
    @BindView(R.id.last_name_tiffin)
    EditText lastNameView;
    @BindView(R.id.phone_tiffin)
    EditText phoneView;
    @BindView(R.id.street_name_tiffin)
    EditText streetName;
    @BindView(R.id.city_tiffin)
    EditText city;
    @BindView(R.id.province_spinner_tiffin)
    Spinner provinceSpinner;
    @BindView(R.id.country_spinner_tiffin)
    Spinner countryView;
    @BindView(R.id.zipcode_tiffin)
    EditText zipcodeView;
    @BindView(R.id.submit_tiffin)
    Button submitProfileButton;

    SessionUtli sessionUtli;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("TiffinSeeker Profile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiffinseeker_profile);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = getLayoutInflater().inflate(R.layout.login_layout, container, false);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        new MyAsynTask().execute("");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
//            String str = (String) intent
//                    .getStringExtra(HttpService.MY_SERVICE_PAYLOAD);


            RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
            if(respondPackage.getParams().containsKey(RespondPackage.SUCCESS)){
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.SUCCESS));
                Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show();
                finish();

            }else{
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.FAILED));
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @OnClick(R.id.submit_tiffin)
    public void submit(View view){
        checkValidation();
    }

    private void checkValidation()
    {
        String getFirstName = firstNameView.getText().toString();
        String getLastName = lastNameView.getText().toString();
        String getPhoneNumber = phoneView.getText().toString();
        String getStreetName = streetName.getText().toString();
        String getCity = city.getText().toString();
        String province = (String)provinceSpinner.getSelectedItem();
        String getCountry = (String)countryView.getSelectedItem();
        String zipCode = zipcodeView.getText().toString();
        //Toast.makeText(this, getFirstName + getLastName + getPhoneNumber + province , Toast.LENGTH_SHORT).show();
        Pattern p = Pattern.compile(Utils.postalRegEx);
        //Pattern phone = Pattern.compile(Utils.phoneRegEx);

        // Matcher phoneMatch = phone.matcher(getPhoneNumber.trim());
        Matcher m = p.matcher(zipCode.trim());
        //Toast.makeText(this, ""+ (getPhoneNumber.length() == PHONELENGHT) + " " +m.find() , Toast.LENGTH_SHORT).show();
//        if(sessionUtli.getValue("isLicenceUploaded").equals("true"))
//            imageSelected = true;
        if(getFirstName.equals("") || getLastName.equals("") || getPhoneNumber.equals("")
                || getStreetName.equals("") || getCity.equals("") || province.equals("")
                || getCountry.equals("") || zipCode.equals("")){
            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");
        }else if(!m.find()){
            new CustomToast().Show_Toast(this, view,
                    "Invalid Zip Code");
        }

        else{
            submit();
        }

    }

    public void submit(){
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.TIFFINSEEKERPROFILE);
        requestPackage.setMethod("POST");
        requestPackage.setParam("UserFname", firstNameView.getText().toString().trim());
        requestPackage.setParam("UserLname", lastNameView.getText().toString().trim());
        requestPackage.setParam("UserPhone", phoneView.getText().toString().trim());
        requestPackage.setParam("UserCountry", "Canada");
        requestPackage.setParam("UserProvince", "ON");
        requestPackage.setParam("UserCity", city.getText().toString());
        requestPackage.setParam("UserZipCode", zipcodeView.getText().toString());
        requestPackage.setParam("UserCompanyName", "Tiffin Demo");
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        requestPackage.setParam("UserStreet", streetName.getText().toString());
        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra(HttpService.REQUEST_PACKAGE, requestPackage);

        startService(intent);
    }

    private class MyAsynTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                return getUserInfo();
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

           try {
               HashMap<String, Object> hashMap = new Gson().fromJson(aVoid, HashMap.class);
               for (String key : hashMap.keySet()) {
                   Log.d("JSONVALUE", key + ": " + hashMap.get(key));
               }
               if(hashMap.get("UserZipCode") != null)
                   initValues(hashMap);

           }catch (Exception e){
               Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
           }

        }

    }

    private void initValues(HashMap<String, Object> hashMap)
    {
        firstNameView.append((String)hashMap.get("UserFname"));
        lastNameView.append((String)hashMap.get("UserLname"));
        phoneView.append((String)hashMap.get("UserPhone"));
        streetName.append((String)hashMap.get("UserStreet"));
        city.append((String)hashMap.get("UserCity"));
        countryView.setSelection(0);
        zipcodeView.append((String)hashMap.get("UserZipCode"));
        provinceSpinner.setSelection(getIndex(provinceSpinner, (String)hashMap.get("UserProvince") ));
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    public String getUserInfo() throws Exception {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.TIFFINSEEKERPROFILEVIEW);
        requestPackage.setMethod("POST");
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        return HttpHelper.downloadFromFeed(requestPackage);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



}
