package com.codewarriors4.tiffin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.codewarriors4.tiffin.models.SubscribersListModel;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionFullView extends AppCompatActivity
{
    @BindView(R.id.full_name)
    public TextView fullName;
    @BindView(R.id.email_view)
    public TextView emailView;
    @BindView(R.id.address_view)
    public TextView addressView;
    @BindView(R.id.phone_no_view)
    public TextView phoneNoView;
    @BindView(R.id.package_name)
    public TextView packageName;
    @BindView(R.id.descri_view)
    public TextView descriView;
    @BindView(R.id.price_view)
    public TextView priceView;
    @BindView(R.id.start_date)
    public TextView startDateView;
    @BindView(R.id.end_date)
    public TextView endDateView;

    private SessionUtli sessionUtli;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_full_view_layout);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        setTitle("Subscription Details");
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        if(intent.hasExtra("Example Item")) {
            SubscribersListModel subscribersListModel = intent.getParcelableExtra("Example Item");
            initValue(subscribersListModel);
        }else if(intent.hasExtra("recent_sub_id")){
            new GetRecentSub().execute(intent.getStringExtra("recent_sub_id"));
        }
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initValue(SubscribersListModel subscribersListModel)
    {
        fullName.setText(subscribersListModel.getUserName());
        emailView.setText(subscribersListModel.getUsereEmail());
        addressView.setText(subscribersListModel.getUserStreet());
        phoneNoView.setText(subscribersListModel.getPhoneNumber());
        packageName.setText(subscribersListModel.getPackageName());
        descriView.setText(subscribersListModel.getPackageDesc());
        priceView.setText(subscribersListModel.getPackageCost() + "CAD");
        packageName.setText(subscribersListModel.getPackageName());
        startDateView.setText(subscribersListModel.getStartDate());
        endDateView.setText(subscribersListModel.getEndDate());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetRecentSub extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String subId = strings[0];
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(Constants.BASE_URL + Constants.GETMYRECENTSUBSCRIBTION);
            requestPackage.setMethod("POST");
            requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
            requestPackage.setParam("", "");
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

                JSONObject subscriber = new JSONObject(s);
                String firstName = subscriber.getString("UserFname");
                String lastName = subscriber.getString("UserLname");
                String email = subscriber.getString("email");
                String street = subscriber.getString("UserStreet");
                String phoneNumber = subscriber.getString("UserPhone");
                String cost = subscriber.getString("SubCost");
                String packageName = subscriber.getString("HMPName");
                String packageDesc = subscriber.getString("HMPDesc");
                String startDate = subscriber.getString("SubStartDate").split(" ")[0];
                String endDate = subscriber.getString("SubEndDate").split(" ")[0];
                super.onPostExecute(s);
                initValue(new SubscribersListModel(firstName + " " +lastName,
                        email, street, phoneNumber, cost, packageName, packageDesc, startDate, endDate));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
