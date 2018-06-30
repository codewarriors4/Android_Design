package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codewarriors4.tiffin.R;
import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TiffinSeekerDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
   // private ProgressBar progressBar;

    String response = "";
    boolean flag = false;

    private SessionUtli sessionUtli;
    TextView textView;
    @BindView(R.id.vew_homemaker_details)
    Button vew_homemaker_details;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
            if(respondPackage.getParams().containsKey(RespondPackage.SUCCESS)){
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.SUCCESS));
                //progressBar.setVisibility(View.GONE);
//                Toast.makeText(context,
//                        respondPackage.getParams().get(RespondPackage.SUCCESS), Toast.LENGTH_LONG)
//                        .show();
                try {
                    //JSONObject responseJson = new JSONObject(respondPackage.getParams().get(RespondPackage.SUCCESS));
                    sessionUtli.setValues(respondPackage.getParams().get(RespondPackage.SUCCESS));
                    if(sessionUtli.getValue("UserType").equals("0")){


                    }else{


                    }

                } catch (Exception e) {
                    Log.d("ERRORJSON", e.getMessage());
                }

            }else{
                //submitButton.setEnabled(true);
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.FAILED));
                Toast.makeText(context,
                        respondPackage.getParams().get(RespondPackage.FAILED), Toast.LENGTH_SHORT)
                        .show();
                //progressBar.setVisibility(View.GONE);
            }

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiffin_seeker);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tiffin_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.tiffin_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.tiffin_nav_view);
        View header = navigationView.getHeaderView(0);
        textView = (TextView) header.findViewById(R.id.email_holder_tiffin);
        navigationView.setNavigationItemSelectedListener(this);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        if(sessionUtli.getValue("UserType").equals("0.0")){

        }
            //greetingTextView.setText("Welcome TiffinSeeker");
        else{
           // greetingTextView.setText("Welcome HomeMaker");
        }

    }


    @OnClick(R.id.vew_homemaker_details)
    public void viewDetails(View view){

        Intent intent = new Intent(this, HomemakerViewProfile.class);
        startActivity(intent);

    }




    private void initFields()
    {
        textView.setText(sessionUtli.getValue("email"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        initFields();

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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.account) {

                startActivity(new Intent(this, TiffinSeeker_Profile.class));

        } else if (id == R.id.menu) {

        } else if (id == R.id.my_subscribers) {

        } else if (id == R.id.legal) {

        } else if (id == R.id.logout) {
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


//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

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




}
