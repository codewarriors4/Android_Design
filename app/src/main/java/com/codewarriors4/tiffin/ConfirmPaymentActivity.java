package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codewarriors4.tiffin.TiffinSeekerDashboardActivity;
import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.PaymentSucessDialog;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmPaymentActivity extends AppCompatActivity implements PaymentSucessDialog.PaymentSuccessListener, View.OnClickListener
{
    @BindView(R.id.card_num_view)
    TextView cardNumberView;

    @BindView(R.id.card_year_view)
    TextView cardEndYear;

    @BindView(R.id.card_year_month)
    TextView cardMonthEnd;

    @BindView(R.id.card_cvc)
    TextView cardCVC;

    @BindView(R.id.total_amt)
    TextView totalAmtView;

    @BindView(R.id.progress)
    ProgressBar progressBar;



    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
//            String str = (String) intent
//                    .getStringExtra(HttpService.MY_SERVICE_PAYLOAD);


            RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
            if (respondPackage.getParams().containsKey(RespondPackage.SUCCESS)) {
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.SUCCESS));
                //progress.setVisibility(View.GONE);
                String success = respondPackage.getParams().get("success");
                try {
                    JSONObject jsonObject = new JSONArray(success).getJSONObject(0);
                    int pId = jsonObject.getInt("PId");
                    String pAmt = jsonObject.getString("PAmt");
                    PaymentSucessDialog paymentSucessDialog = new PaymentSucessDialog();
                    paymentSucessDialog.setValues(pId, pAmt);
                    paymentSucessDialog.show(getFragmentManager(), "Payment");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.FAILED));
                Toast.makeText(context, "Please Select Image", Toast.LENGTH_SHORT).show();
            }

        }
    };
    private SessionUtli sessionUtli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_payment_layout);
        setTitle("Payment Confirmation");
        ButterKnife.bind(this);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        initValues();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initValues() {
        Intent intent = getIntent();
        cardNumberView.setText("XXXX-XXXX-XXXX-XX44");
        cardMonthEnd.setText(intent.getStringExtra("expMonth"));
        cardEndYear.setText(intent.getStringExtra("expYear"));
        cardCVC.setText("XXX");
        totalAmtView.setText("Payment Required is " + intent.getStringExtra("packageTotal"));
    }

    @Override
    public void onClick(View v) {

    }

    @OnClick(R.id.pay_now_btn)
    public void submit() {

        Intent i = getIntent();
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.TSMAKEPAYMENT);
        requestPackage.setMethod("POST");
        requestPackage.setParam("card_number", i.getStringExtra("cardNumber"));
        requestPackage.setParam("expiration_month",i.getStringExtra("expMonth"));
        requestPackage.setParam("expiration_year", i.getStringExtra("expYear"));
        requestPackage.setParam("cvc", i.getStringExtra("cvc"));

        requestPackage.setParam("HMPid", i.getStringExtra("packageId"));
        requestPackage.setParam("subtotal", i.getStringExtra("packageCost"));

        requestPackage.setParam("hst", i.getStringExtra("packageHST"));
        requestPackage.setParam("total", i.getStringExtra("packageTotal"));
        requestPackage.setParam("HomeMakerId", i.getStringExtra("homeMakerId"));

        requestPackage.setHeader("Authorization", "Bearer " + sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra(HttpService.REQUEST_PACKAGE, requestPackage);
        progressBar.setVisibility(View.VISIBLE);
        startService(intent);
    }


    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void getMainActivity() {
        Intent intent = new Intent(this, TiffinSeekerDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
