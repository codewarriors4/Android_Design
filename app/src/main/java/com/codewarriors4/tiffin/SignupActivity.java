package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.codewarriors4.tiffin.models.RegistrationModel;
import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.Utils;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static View view;
    private static EditText emailId, password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    private static ToggleButton isHomemaker;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String str = (String) intent
                    .getStringExtra(HttpService.MY_SERVICE_PAYLOAD);
            Log.d("JsonResponseData", "onReceive: " + str);
            Toast.makeText(context, str, Toast.LENGTH_SHORT)
                    .show();

        }
    };



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        initViews();
        setListeners();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
    }

    private void initViews() {

        emailId = (EditText) findViewById(R.id.userEmailId);

        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        signUpButton = (Button) findViewById(R.id.signUpBtn);
        login = (TextView)findViewById(R.id.already_user);
        terms_conditions = (CheckBox) findViewById(R.id.terms_conditions);
        //isHomemaker = (ToggleButton) findViewById(R.id.toggleButton);

        // Setting text selector over textviews
        /* XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        } */
    }


    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts

        String getEmailId = emailId.getText().toString();

        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();
        boolean isHomemakerChecked = isHomemaker.isChecked();
        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0){

            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");
        }
        // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(this, view,
                    "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(this , view,
                    "Both password doesn't match.");

            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            new CustomToast().Show_Toast(this, view,
                    "Please select Terms and Conditions.");

            // Else do signup or do your stuff
        else {
            Toast.makeText(this, "Do SignUp.", Toast.LENGTH_SHORT)
                    .show();
            RegistrationModel reg = new RegistrationModel(getEmailId, getPassword, isHomemakerChecked ? 1 : 0, getConfirmPassword);
            signUpHandler(reg);
        }

    }

    private void signUpHandler(RegistrationModel reg)
    {
        RequestPackage requestPackage = new RequestPackage();
        Gson gson = new Gson();
        String toJsonString = gson.toJson(reg);
        requestPackage.setEndPoint("http://10.192.88.87/tiffin_service/web/public/api/register");
        requestPackage.setParam("email", reg.getEmailID());
        requestPackage.setParam("password", reg.getPassword());
        requestPackage.setParam("password_confirmation", reg.getConfirmPassword());
        requestPackage.setParam("UserType", reg.getUserType()+"");
        requestPackage.setMethod("POST");
        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra(HttpService.REQUEST_PACKAGE, requestPackage);

        //intent.setData(Uri.parse(JSON_URL));
        startService(intent);
//    } else {
//        Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
//    }
    }

    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                // Replace login activity
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }
}
