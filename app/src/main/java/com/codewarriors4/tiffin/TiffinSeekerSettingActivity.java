package com.codewarriors4.tiffin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TiffinSeekerSettingActivity extends AppCompatActivity
{
    private SessionUtli sessionUtli;
    private SwitchCompat pushforLicence;
    private boolean isActivityStarted = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiffin_s_setting_layout);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        pushforLicence = findViewById(R.id.setting_licence);

        pushforLicence.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isActivityStarted)
                   new HttpSettingValues().execute("3", isChecked ? "0" : "1");
            }
        });


        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        new HttpSettingRequest().execute("");
    }


    private class HttpSettingRequest extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                return getSettingsData();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray settingJson = new JSONArray(s);
                if(settingJson.length() == 2){
                    pushforLicence.setChecked( settingJson.getJSONObject(0).getInt("status") == 0 ? true : false);
                    isActivityStarted = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class HttpSettingValues extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(Constants.BASE_URL + Constants.SETSETTINGS);
            requestPackage.setMethod("POST");
            requestPackage.setParam("MFCMSIdFk", strings[0]);
            requestPackage.setParam("status", strings[1]);
            requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
            requestPackage.setHeader("Accept", "application/json; q=0.5");
            try {
                return HttpHelper.downloadFromFeed(requestPackage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                String status = new JSONObject(s).getString("status");
                if(status.equals("success")){
                    Toast.makeText(TiffinSeekerSettingActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getSettingsData() throws Exception {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.GETSETTINGS);
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
