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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.PaymentSucessDialog;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TSSubPayment extends AppCompatActivity implements PaymentSucessDialog.PaymentSuccessListener, View.OnClickListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int PHONELENGHT = 10;


    private View view;

    @BindView(R.id.card_name_value)
    EditText card_name;
    @BindView(R.id.card_number_value)
    EditText card_number;
    @BindView(R.id.card_exp_date_mnth)
    EditText card_exp_date_mnth;
    @BindView(R.id.card_exp_date_year)
    EditText card_exp_date_year;
    @BindView(R.id.card_cvv_value)
    EditText card_cvv;


    @BindView(R.id.payment_btn)
    Button payment_btn;

    Context TSViewHMPackageCtx;


    private SessionUtli sessionUtli;
    private FrameLayout progress;
    private LinearLayout profileBody;
    private JsonObject hmPackageDetails;


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
//            String str = (String) intent
//                    .getStringExtra(HttpService.MY_SERVICE_PAYLOAD);


            RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
            if (respondPackage.getParams().containsKey(RespondPackage.SUCCESS)) {
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.SUCCESS));
                progress.setVisibility(View.GONE);
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


    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Subscription Payment");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ts_sub_payment);
        progress = findViewById(R.id.progress_overlay);
        progress.setVisibility(View.GONE);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);
        profileBody = findViewById(R.id.profile_body);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    @OnClick(R.id.payment_btn)
    public void submit(View view) {
        checkValidation();
    }

    private void checkValidation() {
        String getCardName = card_name.getText().toString();
        String getCadNum = card_number.getText().toString();
        String getCardExpMnth = card_exp_date_mnth.getText().toString();
        String getCardExpYear = card_exp_date_year.getText().toString();
        String getCardCVV = card_cvv.getText().toString();


        if (getCardName.equals("") || getCadNum.equals("") || getCardExpMnth.equals("") || getCardExpYear.equals("") || getCardCVV.equals("")) {
            new CustomToast().Show_Toast(this, findViewById(android.R.id.content),
                    "All fields are required.");
        } else if (!getCadNum.equals("1111222233334444")) {
            new CustomToast().Show_Toast(this, findViewById(android.R.id.content),
                    "Invalid card");
        } else {
            submit();
        }

    }

    public void submit() {

        Intent i = getIntent();
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        String cardNumber =  card_number.getText().toString().trim();
        String expMonth = card_exp_date_mnth.getText().toString().trim();
        String expYear = card_exp_date_year.getText().toString().trim();
        String cvc = card_cvv.getText().toString().trim();
        String packageId = i.getStringExtra("package_id");
        String packageCost = i.getStringExtra("package_cost");
        String packageHST = i.getStringExtra("package_hst");
        String packageTotal = i.getStringExtra("package_total");
        String homeMakerId = i.getStringExtra("HomeMakerId");
        Intent newIntent = new Intent (this, ConfirmPaymentActivity.class);
        newIntent.putExtra("cardNumber", cardNumber);
        newIntent.putExtra("expMonth", expMonth);
        newIntent.putExtra("expYear", expYear);
        newIntent.putExtra("cvc", cvc);
        newIntent.putExtra("packageId", packageId);
        newIntent.putExtra("packageCost", packageCost);
        newIntent.putExtra("packageHST", packageHST);
        newIntent.putExtra("packageTotal", packageTotal);
        newIntent.putExtra("homeMakerId", homeMakerId);
        startActivity(newIntent);
    }




    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Working", Toast.LENGTH_LONG).show();
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
