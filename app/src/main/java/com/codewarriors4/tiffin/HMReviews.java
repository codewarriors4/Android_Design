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
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codewarriors4.tiffin.adapters.HMReviewsListAdapter;
import com.codewarriors4.tiffin.models.HMReviewsModel;
import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class HMReviews extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int PHONELENGHT = 10;

    RecyclerView recyclerView;
    HMReviewsListAdapter adapter;
    private JsonObject tsReviewsListJSONResponse;

    List<HMReviewsModel> reviewList;

    private  View view;


    private SessionUtli sessionUtli;
    private FrameLayout progress;
    private LinearLayout profileBody;

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
        setTitle("Reviews");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hm_view_reviews);

        reviewList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.hm_view_reviews_parent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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


    public String getHMReviewsList() throws Exception {

        //Log.d("Testing data1", sessionUtli.getValue("access_token"));

        Intent i = getIntent();
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.TSVIEWHMRATINGS);
        requestPackage.setMethod("POST");
        requestPackage.setParam("HomeMakerID", i.getStringExtra("HomeMakerID"));
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        return HttpHelper.downloadFromFeed(requestPackage);
    }

    private class MyAsynTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String tsReviewsList = getHMReviewsList();
                return tsReviewsList;
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
            //  profileBody.setVisibility(View.GONE);
            //progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String aVoid) {
            try {
                Log.d("Testing data", "onPostExecute: " + aVoid);
                super.onPostExecute(aVoid);
                // profileBody.setVisibility(View.VISIBLE);
                // progress.setVisibility(View.GONE);

                JSONArray uniObject = new JSONArray(aVoid);
                initValues(uniObject);

            }

            catch(Exception e){
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG);
            }
        }

    }


    private void initValues(JSONArray uniObject) throws JSONException
    {
        Log.d("jsondump", "tsReviewsList");



        Intent intent = new Intent();
        JSONArray jsonarray = new JSONArray(uniObject.toString());
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String id = String.valueOf(i+1);
            String hmID =  intent.getStringExtra("HMId");
            String hmName =  intent.getStringExtra("HMName");
            String tsName =  jsonobject.getString("UserFname") + " " + jsonobject.getString("UserLname") ;
            String ratingDesc = jsonobject.getString("ReviewDesc");
            String reviewUpdateDate = jsonobject.getString("updated_at");
            float ratingCount = (float) jsonobject.getDouble("ReviewCount");

            HMReviewsModel model= new HMReviewsModel(
                    id,
                    hmID,
                    hmName,
                    tsName,
                    ratingDesc,
                    reviewUpdateDate,
                    ratingCount
            );

            reviewList.add(model);


        }
        adapter = new HMReviewsListAdapter(this, reviewList);
        recyclerView.setAdapter(adapter);


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


    protected void onDestroy(){
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }


}
