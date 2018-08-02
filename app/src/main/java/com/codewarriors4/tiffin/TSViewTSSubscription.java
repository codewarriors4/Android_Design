package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codewarriors4.tiffin.adapters.HMPackagesListAdapter;
import com.codewarriors4.tiffin.adapters.TSViewHMPackagesListAdapter;
import com.codewarriors4.tiffin.adapters.TSViewSubsListAdapter;
import com.codewarriors4.tiffin.models.TSSubscriptionsModel;
import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.PaymentSucessDialog;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TSViewTSSubscription extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int PHONELENGHT = 10;
    static final int PICK_CONTACT_REQUEST = 1;// The request code
    static final String REQUEST_PARAM = "response";

    RecyclerView recyclerView;
    TSViewSubsListAdapter adapter;
    private JsonObject hmPackagesListJSONResponse;

    List<TSSubscriptionsModel> packageList;

    private  View view;

    private SessionUtli sessionUtli;
    private FrameLayout progress;
    private LinearLayout profileBody;



    protected void onCreate(Bundle savedInstanceState) {
        setTitle("My Subscriptions");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ts_view_subscriptions);

        packageList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.ts_view_subscriptions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);
        profileBody = findViewById(R.id.profile_body);
        progress = findViewById(R.id.progress_overlay);
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = getLayoutInflater().inflate(R.layout.login_layout, container, false);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        // Check which request we're responding to
//        if (requestCode == PICK_CONTACT_REQUEST) {
//            if(resultCode == 1){
//                String ok = data.getStringExtra(REQUEST_PARAM);
//                if(ok.equals("ok")){
//                    new CustomToast().Show_Toast(this,  findViewById(android.R.id.content), "ReView Updated!");
//                }else{
//
//                }
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        packageList.clear();
        new MyAsynTask().execute("");
    }

    public String getHMPackagesList() throws Exception {
        Intent i = getIntent();
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.TSVIEWSUBSCRIPTIONS);
        requestPackage.setMethod("POST");
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        return HttpHelper.downloadFromFeed(requestPackage);
    }

    private class MyAsynTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String hmPackagesList = getHMPackagesList();
                return hmPackagesList;
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
            try {
                Log.d("Testing data", "onPostExecute: " + aVoid);
                super.onPostExecute(aVoid);
                JSONArray uniObject = new JSONArray(aVoid);
                Log.d("JSONVALUE", "test");
                initValues(uniObject);
            }
            catch(Exception e){
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG);
            }
        }

    }


    private void initValues(JSONArray uniObject) throws JSONException
    {

        JSONArray jsonarray = new JSONArray(uniObject.toString());
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String id = String.valueOf(jsonobject.getInt("id"));
            int hmID = jsonobject.getInt("HomeMakerId");
            int subID = jsonobject.getInt("SubId");
            int packID = jsonobject.getInt("HMPid");
            String hmName = jsonobject.getString("UserFname") + " " + jsonobject.getString("UserLname") ;
            String packTitle = jsonobject.getString("HMPName");
            String packDesc = jsonobject.getString("HMPDesc");
            String subStartDate = jsonobject.getString("SubStartDate");
            String subEndDate = jsonobject.getString("SubEndDate");
            Double packageCost = jsonobject.getDouble("HMPCost");
            float ratingCount = (float) jsonobject.getDouble("personalRating");
            String driverName = jsonobject.getString("driverName");
            String driverPhoneNumber = jsonobject.getString("driverPhone");
            String driverUID = String.valueOf(jsonobject.getInt("driverUniqueCode"));

            TSSubscriptionsModel model= new TSSubscriptionsModel(
                    id,
                    hmID,
                    subID,
                    packID,
                    hmName,
                    packTitle,
                    packDesc,
                    subStartDate,
                    subEndDate,
                    packageCost,
                    ratingCount,
                    driverName,
                    driverPhoneNumber,
                    driverUID

            );

            packageList.add(model);


        }
        adapter = new TSViewSubsListAdapter(TSViewTSSubscription.this, packageList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TSViewSubsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(TSViewTSSubscription.this, TSReviewHM.class);
                i.putExtra("HomeMakerId", String.valueOf(packageList.get(position).getHmID()));
                i.putExtra("HMName", String.valueOf(packageList.get(position).getHmName()));
                i.putExtra("request_data", packageList.get(position));
                startActivityForResult(i,PICK_CONTACT_REQUEST);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
