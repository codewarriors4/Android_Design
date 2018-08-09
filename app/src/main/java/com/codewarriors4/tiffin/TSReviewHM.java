package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codewarriors4.tiffin.models.TSSubscriptionsModel;
import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.codewarriors4.tiffin.TSViewTSSubscription.PICK_CONTACT_REQUEST;

public class TSReviewHM extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int PHONELENGHT = 10;
    boolean receiverRegister = false;


    private  View view;
    @BindView(R.id.rating_profile_name)
    TextView hm_name;
    @BindView(R.id.edit_rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.edit_rating_content)
    EditText ratingDesc;
    @BindView(R.id.submit_rating_btn)
    Button submitRatingBtn;

    @BindView(R.id.package_name_view)
            TextView packageNameView;
    @BindView(R.id.package_desc_view)
            TextView packageDescView;
    @BindView(R.id.sub_start_view)
            TextView subscribeStartDate;
    @BindView(R.id.sub_end_view)
            TextView subscribeEndDate;
    @BindView(R.id.driver_name)
    TextView driverNameView;
    @BindView(R.id.driver_number)
    TextView driverNumberView;
    @BindView(R.id.driver_id)
    TextView driverID;
    @BindView(R.id.package_cost_view)
            TextView packageCostView;
    @BindView(R.id.view_profile_btn)
            Button view_profile_btn;




    SessionUtli sessionUtli;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Subscription Details");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ts_create_update_review);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = getLayoutInflater().inflate(R.layout.login_layout, container, false);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        receiverRegister = true;
        new MyAsynTask().execute("");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        Intent i = getIntent();
        final TSSubscriptionsModel  request_data = (TSSubscriptionsModel)i.getParcelableExtra("request_data");
        initFields(request_data);

        view_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TSReviewHM.this, TSViewHMProfile.class);
                i.putExtra("Id", request_data.getId());
                i.putExtra("HMId", String.valueOf(request_data.getHmID()));
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!receiverRegister){
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .registerReceiver(mBroadcastReceiver,
                            new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        receiverRegister = false;
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);

    }

    private void initFields(TSSubscriptionsModel model){
        hm_name.setText(model.getHmName());
        packageNameView.setText(model.getPackTitle());
        packageDescView.setText(model.getPackDesc());
        subscribeStartDate.setText(model.getSubStartDate());
        subscribeEndDate.setText(model.getSubEndDate());
        driverNameView.setText(model.getDriverName());
        driverNumberView.setText(model.getDriverPhone());
        driverID.setText(model.getDriverUID());
        packageCostView.setText(model.getPackageCost() + "CAD");
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
//            String str = (String) intent
//                    .getStringExtra(HttpService.MY_SERVICE_PAYLOAD);


            RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
            if(respondPackage.getParams().containsKey(RespondPackage.SUCCESS)){
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.SUCCESS));
                Toast.makeText(context, "Review Updated Successfully", Toast.LENGTH_LONG).show();

                finish();
                //onBackPressed();
            }else{
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.FAILED));
                Intent responseIntent = new Intent();
                responseIntent.putExtra(TSViewTSSubscription.REQUEST_PARAM, false);
                TSReviewHM.this.setResult(PICK_CONTACT_REQUEST, intent);
                finish();
            }

        }
    };

    @OnClick(R.id.submit_rating_btn)
    public void submit(View view){
        checkValidation();
    }

    private void checkValidation()
    {
        Float ratingValue = ratingBar.getRating();
        String ratingDescription = ratingDesc.toString();


        if(ratingValue.equals("") || ratingDescription.equals("")){
            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");
        }

        else{
            submit();
        }

    }

    public void submit(){
        Intent i = getIntent();
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.TSCREATEUPDATERATINGS);
        requestPackage.setMethod("POST");
        requestPackage.setParam("HomeMakerID",i.getStringExtra("HomeMakerId"));
        requestPackage.setParam("ReviewCount", String.valueOf(ratingBar.getRating()));
        requestPackage.setParam("ReviewDesc", ratingDesc.getText().toString());

        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");

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

                JSONArray uniObject = new JSONArray(aVoid);
                initValues(uniObject);



            }catch (Exception e){
                Toast.makeText(getBaseContext(), "Error222222222222222222222222222", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void initValues(JSONArray uniObject) throws JSONException {



        JSONArray jsonarray = new JSONArray(uniObject.toString());
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            ratingBar.setRating(Float.parseFloat(jsonobject.getString("ReviewCount")));
            ratingDesc.append(jsonobject.getString("ReviewDesc"));
        }



    }


    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    public String getUserInfo() throws Exception {
        Intent i = getIntent();

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.TSVIEWHMRATING);
        String hmid = i.getStringExtra("HomeMakerId");


        requestPackage.setParam("HomeMakerID", hmid);
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
