package com.codewarriors4.tiffin;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.DatabaseHelper;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;

public class HomemakerDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SessionUtli sessionUtli;
    DatabaseHelper mDatabaseHelper;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homemaker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.homemaker_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.homemaker_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        mDatabaseHelper = new DatabaseHelper(this);
        Cursor cfcmtoken = mDatabaseHelper.fetch();
        String fcmtoken = cfcmtoken.getString(cfcmtoken.getColumnIndex("fcmkey"));
        sessionUtli.setValue("fcmtoken",fcmtoken);
        new MyAsynTask().execute(""); // let this run first

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.homemaker_drawer_layout);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homemaker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.account) {

            Intent i = new Intent(this, Homemaker_Profile.class);
            startActivity(i);
        } else if (id == R.id.menu) {

        } else if (id == R.id.create_new) {

            if(sessionUtli.getValue("isActive").equals("1.0")) {
                Intent i = new Intent(this, HomemakerCreatePackages.class);
                startActivity(i);
            }else{
                Toast.makeText(this, "Verification is pending, Please Contact Admin", Toast.LENGTH_LONG).show();
            }


        } else if (id == R.id.view_menu) {
            if(sessionUtli.getValue("isActive").equals("1.0")) {
                Intent i = new Intent(this, HomemakerViewPackages.class);
                startActivity(i);
            }else{
                Toast.makeText(this, "Verification is pending, Please Contact Admin", Toast.LENGTH_LONG).show();
            }



        }else if (id == R.id.my_subscribers) {

            if(sessionUtli.getValue("isActive").equals("1.0")) {
                startActivity(new Intent(this, SubscribersListActivity.class));
            }else{
                Toast.makeText(this, "Verification is pending, Please Contact Admin", Toast.LENGTH_LONG).show();
            }

//        }else if (id == R.id.my_daily_subs) {
//
//
//
//        }else if (id == R.id.my_monthly_subs) {
//            startActivity(new Intent(this, Legal.class));
//
//
//        }else if (id == R.id.my_all_subs) {
//            startActivity(new Intent(this, Legal.class));
//
//
        } else if (id == R.id.legal) {
            startActivity(new Intent(this, Legal.class));

        }else if (id == R.id.privacy) {
            startActivity(new Intent(this, Privacy.class));

        } else if (id == R.id.logout) {
            sessionUtli.clearAll();
            startActivity(new Intent(this, MainActivity.class));

        }else if(id == R.id.review){
            Intent i = new Intent(this, HMReviews.class);
            String hmId = sessionUtli.getValue("HMId");
            i.putExtra("HomeMakerID", sessionUtli.getValue("HMId"));
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.homemaker_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
