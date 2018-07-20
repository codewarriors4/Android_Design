package com.codewarriors4.tiffin;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.PackageManager;
import android.icu.text.UnicodeSetSpanner;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codewarriors4.tiffin.adapters.HMPackagesListAdapter;
import com.codewarriors4.tiffin.adapters.HomeMakerListAdapter;
import com.codewarriors4.tiffin.models.HMPackagesModel;
import com.codewarriors4.tiffin.models.HomeMakerListItems;
import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.DatabaseHelper;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.location.LocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

import butterknife.OnClick;

public class TiffinSeekerDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    // private ProgressBar progressBar;

    String response = "";
    boolean flag = false;

    private SessionUtli sessionUtli;
    TextView textView;


    private HomeMakerListAdapter listViewAdapter;

    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;

    DatabaseHelper mDatabaseHelper;
    List<Address> fromLocation = null;

    private FusedLocationProviderClient mFusedLocationClient;
    RecyclerView recyclerView;
    ProgressBar homeMakerProgressList;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
            if (respondPackage.getParams().containsKey(RespondPackage.SUCCESS)) {
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.SUCCESS));
                try {

                    sessionUtli.setValues(respondPackage.getParams().get(RespondPackage.SUCCESS));
                    if (sessionUtli.getValue("UserType").equals("0")) {

                    } else {

                    }

                } catch (Exception e) {
                    Log.d("ERRORJSON", e.getMessage());
                }

            } else {
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.FAILED));
                Toast.makeText(context,
                        respondPackage.getParams().get(RespondPackage.FAILED), Toast.LENGTH_SHORT)
                        .show();

            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiffin_seeker);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_gps);
        setSupportActionBar(toolbar);
        ImageButton getLocationBtn = toolbar.findViewById(R.id.action_bar_button);
        getLocationBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.tiffin_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.tiffin_nav_view);
        View header = navigationView.getHeaderView(0);
        textView = (TextView) header.findViewById(R.id.email_holder_tiffin);
        navigationView.setNavigationItemSelectedListener(this);

//        LocalBroadcastManager.getInstance(this)
//                .registerReceiver(mBroadcastReceiver,
//                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        homeMakerProgressList = findViewById(R.id.progress_get_homemaker);
        recyclerView = (RecyclerView) findViewById(R.id.search_home_maker_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor cfcmtoken = mDatabaseHelper.fetch();
        String fcmtoken = cfcmtoken.getString(cfcmtoken.getColumnIndex("fcmkey"));
        sessionUtli.setValue("fcmtoken", fcmtoken);
        new MyAsynTask().execute("");

//        if(sessionUtli.getValue("UserType").equals("0.0")){
//
//        }
//            //greetingTextView.setText("Welcome TiffinSeeker");
//        else{
//           // greetingTextView.setText("Welcome HomeMaker");
//        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        //listViewAdapter = new HomeMakerListAdapter(this);
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                homeMakerProgressList.setVisibility(View.VISIBLE);
                                doLocation(location);
                            }else{
                                Toast.makeText(TiffinSeekerDashboardActivity.this, "GPS SETTING ERROR", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void doLocation(Location lastKnownLocation) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            fromLocation = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
            new LocationAsynTask().execute(fromLocation.get(0).getPostalCode());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView app_bar_editTxt = findViewById(R.id.app_bar_editTxt);
        app_bar_editTxt.setText(fromLocation.get(0).getAddressLine(0).split(",")[0] +"  " + fromLocation.get(0).getPostalCode());
    }

//    @OnClick(R.id.view_hm_details)
    public void viewDetails(View view){

        Intent intent = new Intent(this, TSViewHMProfile.class);
        startActivity(intent);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.account) {

            startActivity(new Intent(this, TiffinSeeker_Profile.class));

        } else if (id == R.id.menu) {

        } else if (id == R.id.ts_view_orders) {

            startActivity(new Intent(this, TSViewTSSubscription.class));

        } else if (id == R.id.legal) {
            startActivity(new Intent(this, Legal.class));

        } else if (id == R.id.privacy) {
            startActivity(new Intent(this, Privacy.class));

        }else if (id == R.id.logout) {
            sessionUtli.clearAll();
            startActivity(new Intent(this, MainActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.tiffin_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.tiffin_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        doLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {}

    //        if(getIntent().getBooleanExtra("isNewLogin", false)){
//            //textView = (TextView)findViewById(R.id.textView4);
//
//            LocalBroadcastManager.getInstance(getApplicationContext())
//                    .registerReceiver(mBroadcastReceiver,
//                            new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
//                getUserInformation((String)sessionUtli.getValue("access_token"));
//
//        }else{
//            //textView = (TextView)findViewById(R.id.textView4);
//
//            if(sessionUtli.getValue("UserType").equals("0")){
//
//
//            }else{
//
//
//            }
//        }
//
//        if(sessionUtli.getValue("UserType").equals("0.0"))
//            greetingTextView.setText("Welcome TiffinSeeker");
//        else{
//            greetingTextView.setText("Welcome HomeMaker");
//        }
    public String getUserInfo(String fcmtoken) throws Exception {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.FCMTOKENSTORE);
        requestPackage.setMethod("POST");
        requestPackage.setParam("fcmToken", fcmtoken);
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        return HttpHelper.downloadFromFeed(requestPackage);
    }

    public String getHomeMakerList(String zipcode) throws Exception {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.GETHOMEMAKERS);
        requestPackage.setMethod("POST");
        requestPackage.setParam("ZipCode", zipcode);
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        return HttpHelper.downloadFromFeed(requestPackage);
    }

    private class MyAsynTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                return getUserInfo(sessionUtli.getValue("fcmtoken"));
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

    private class LocationAsynTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String homeMakerList = getHomeMakerList(strings[0]);
                return homeMakerList;
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
                JSONObject mainObject = new JSONObject(aVoid);
                JSONArray uniObject = mainObject.getJSONArray("matched_homemakers");
                if(uniObject != null){
                    initValues(uniObject);
                }else{
                    Toast.makeText(TiffinSeekerDashboardActivity.this, "No Homamaker around \n your location", Toast.LENGTH_LONG ).show();
                }
            } catch (JSONException e) {
                Toast.makeText(TiffinSeekerDashboardActivity.this, "Zip code error", Toast.LENGTH_LONG).show();
                homeMakerProgressList.setVisibility(View.GONE);
            }


        }

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void initValues(JSONArray uniObject) throws JSONException
    {
        ArrayList homeListArray = new ArrayList();

        JSONArray jsonarray = new JSONArray(uniObject.toString());
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
//            String id = String.valueOf(i+1);
            int id = jsonobject.getInt("id");
            int hmId = jsonobject.getInt("HMId");
            String email = jsonobject.getString("email");
            String UserZipCode = jsonobject.getString("UserZipCode");
            //Double packCost = jsonobject.getDouble("UserZipCode");

            HomeMakerListItems homeList = new HomeMakerListItems(id , hmId, email, UserZipCode, UserZipCode);

            homeListArray.add(homeList);


        }
        HomeMakerListAdapter adapter = new HomeMakerListAdapter(this, homeListArray);
        recyclerView.setAdapter(adapter);
        homeMakerProgressList.setVisibility(View.GONE);

    }


}
