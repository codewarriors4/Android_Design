package com.codewarriors4.tiffin;

import android.Manifest;
import android.app.DialogFragment;
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
import android.os.Handler;
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
import com.codewarriors4.tiffin.adapters.TSViewSubsListAdapter;
import com.codewarriors4.tiffin.models.HMPackagesModel;
import com.codewarriors4.tiffin.models.HomeMakerListItems;
import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.DatabaseHelper;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.LocationDialog;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.location.LocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

import butterknife.OnClick;

public class TiffinSeekerDashboardActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        LocationListener,
        LocationDialog.LocationDiglogActivityListner{
    // private ProgressBar progressBar;

    String response = "";
    boolean flag = false;
    boolean doubleBackToExitPressedOnce = false;

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
    public LocationCallback locationCallback;

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
        View headerView = navigationView.getHeaderView(0);
        ((TextView)headerView.findViewById(R.id.email_holder_tiffin)).setText(sessionUtli.getValue("email"));
//        ((TextView)().findViewById(R.id.email_holder_tiffin)).setText(sessionUtli.getValue("email"));

//        if(sessionUtli.getValue("UserType").equals("0.0")){
//
//        }
//            //greetingTextView.setText("Welcome TiffinSeeker");
//        else{
//           // greetingTextView.setText("Welcome HomeMaker");
//        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                doLocation(locationResult.getLastLocation());
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                if(locationAvailability.isLocationAvailable()){
                    getLocation();
                    mFusedLocationClient.removeLocationUpdates(this);
                }
            }
        };



        //listViewAdapter = new HomeMakerListAdapter(this);
        //getLocation();
        if(getIntent().hasExtra("HMPId")) {
            Intent i = new Intent(this, TSViewHMPackage.class);
            i.putExtra("package_id", getIntent().getStringExtra("HMPId"));
            startActivity(i);
        }
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

                                    LocationRequest mLocationRequest = new LocationRequest();
                                    mLocationRequest.setInterval(0);
                                    mLocationRequest.setFastestInterval(0);
                                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
                                    //Toast.makeText(TiffinSeekerDashboardActivity.this, "GPS SETTING ERROR", Toast.LENGTH_LONG).show(); // new LocationAsynTask().execute(fromLocation.get(0).getPostalCode()); paste her wit hzip
                            }
                        }
                    });

        }
    }

    private void doLocation(Location lastKnownLocation) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                fromLocation = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
                if(fromLocation.get(0).getPostalCode() != null) {
                   new LocationAsynTask().execute(fromLocation.get(0).getPostalCode());
                    TextView app_bar_editTxt = findViewById(R.id.location_auto_complete);
                    app_bar_editTxt.setText(fromLocation.get(0).getAddressLine(0).split(",")[0] + "  " + fromLocation.get(0).getPostalCode());
               }else{
                    new LocationDialog().show(getFragmentManager(), "Invalid Location Detect");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


    }


    private void doLocation(String postalCode){
        new LocationAsynTask().execute(postalCode);
        TextView app_bar_editTxt = findViewById(R.id.location_auto_complete);
        app_bar_editTxt.setText(postalCode);
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

        }
        else if (id == R.id.setting) {
            startActivity(new Intent(this, TiffinSeekerSettingActivity.class));

        }else if (id == R.id.logout) {
            sessionUtli.clearAll();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
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
        } else if(doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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

    @Override
    public void onDialogPostiveClick(String postalCode) {

        if(!postalCode.trim().equals("")){
            doLocation(postalCode.trim());
        }else{
            homeMakerProgressList.setVisibility(View.GONE);
            new CustomToast().Show_Toast(this, findViewById(android.R.id.content), "Invalid Postal Code");

        }
    }

    @Override
    public void onDialogNegativeClick() {
        homeMakerProgressList.setVisibility(View.GONE);
        new CustomToast().Show_Toast(this, findViewById(android.R.id.content), "Please Provide Postal Code \n Change GPS Setting");
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
                new LocationDialog().show(getFragmentManager(), "Invalid Location Detect");
            }


        }

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void initValues(JSONArray uniObject) throws JSONException
    {
        final ArrayList<HomeMakerListItems>homeListArray = new ArrayList();

        JSONArray jsonarray = new JSONArray(uniObject.toString());
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            int id = jsonobject.getInt("id");
            int HMid =jsonobject.getInt("HMId");
            String homeMakerFirstName = jsonobject.getString("UserFname");
            String homeMakerLastName = jsonobject.getString("UserLname");
            String homeMakerStreet = jsonobject.getString("UserStreet");
            String homeMakerCity = jsonobject.getString("UserCity");
            String homeMakerZipCode = jsonobject.getString("UserZipCode");
            String homeMakerPhone = jsonobject.getString("UserPhone");
            String homeMakerRating = jsonobject.getString("AverageRatings");

            HomeMakerListItems homeList = new HomeMakerListItems(homeMakerFirstName ,
                    homeMakerLastName, homeMakerStreet, homeMakerCity, homeMakerZipCode,homeMakerPhone, homeMakerRating, id,HMid);

            homeListArray.add(homeList);
        }
        HomeMakerListAdapter adapter = new HomeMakerListAdapter(this, homeListArray);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TSViewSubsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(TiffinSeekerDashboardActivity.this, TSViewHMProfile.class);
                i.putExtra("Id", homeListArray.get(position).getId() + "");
                i.putExtra("HMId", homeListArray.get(position).getHmID()+"");
                startActivity(i);
            }
        });
        homeMakerProgressList.setVisibility(View.GONE);

    }


}