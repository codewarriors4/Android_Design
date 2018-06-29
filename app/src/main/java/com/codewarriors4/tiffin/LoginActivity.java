package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.codewarriors4.tiffin.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private  View view;
    private  EditText emailid, password;
    private  Button loginButton;
    private  TextView forgotPassword, signUp;
    private  CheckBox show_hide_password;
    private  Animation shakeAnimation;
    private  LinearLayout loginLayout;
    private ProgressBar progressBar;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

    public void onReceive(Context context, Intent intent) {
//        String str = (String) intent
//                .getStringExtra(HttpService.MY_SERVICE_PAYLOAD);
//        Log.d("JsonResponseData", "onReceive: " + str);
//        Toast.makeText(context, str, Toast.LENGTH_SHORT)
//                .show();
//
//
//        Intent demoIntent = new Intent(context, TiffinSeekerDashboardActivity.class);
//        demoIntent.putExtra("response", str);
//        startActivity(demoIntent);

        RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);

        if(respondPackage.getParams().containsKey(RespondPackage.SUCCESS)){
            String access_token ="";
            try {
                access_token = respondPackage.getParams().get(RespondPackage.SUCCESS);
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.SUCCESS));
                progressBar.setVisibility(View.GONE);
//            Toast.makeText(context,
//                    access_token, Toast.LENGTH_LONG)
//                    .show();
                SharedPreferences sharedPreference = getSharedPreferences(
                        Constants.SHAREDPREFERNCE, MODE_PRIVATE
                );

                SessionUtli session = SessionUtli.getSession(sharedPreference);
                session.setValues(access_token);
                new UserHandler().startActivity(session, LoginActivity.this);
//                Intent demoIntent = new Intent(context, TiffinSeekerDashboardActivity.class);
//                demoIntent.putExtra("isNewLogin", true);
//                context.startActivity(demoIntent);
//                finish();
            } catch (Exception e) {
                Log.d("LOGINACTIVITY:ERROR", e.getMessage());
            }

//
        }else{
            progressBar.setVisibility(View.GONE);
            loginButton.setEnabled(true);

            Log.d("JsonResponseData", "onReceive: "
                    + respondPackage.getParams().get(RespondPackage.FAILED));
            Toast.makeText(context,
                    respondPackage.getParams().get(RespondPackage.FAILED), Toast.LENGTH_SHORT)
                    .show();

        }

    }
};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = getLayoutInflater().inflate(R.layout.login_layout, container, false);
        initViews();
        setListeners();

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        doGreeting();

    }

    private void doGreeting() {

//
//        String greetingString = getIntent().getStringExtra("REGISTRATION_GREETING");
//        if(!(greetingString == null)){
//           Toast.makeText(this, greetingString, Toast.LENGTH_LONG).show();
//
//        }else{
//            Toast.makeText(this, "No Intent", Toast.LENGTH_LONG).show();
//        }
    }

    private void initViews() {


        emailid = (EditText) findViewById(R.id.login_emailid);
        password = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.loginBtn);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        signUp = (TextView) findViewById(R.id.createAccount);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);


        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake);

        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;

            case R.id.forgot_password:

                // Replace forgot password fragment with animation
//			fragmentManager
//					.beginTransaction()
//					.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
//					.replace(R.id.frameContainer,
//							new ForgotPassword_Fragment(),
//							Utils.ForgotPassword_Fragment).commit();
                finish();
                startActivity(new Intent(this, ForgotPassword.class));
                break;
            case R.id.createAccount:
                finish();
                startActivity(new Intent(this, SignupActivity.class));

                // Replace signup frgament with animation
//			fragmentManager
//					.beginTransaction()
//					.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
//					.replace(R.id.frameContainer, new SignUp_Fragment(),
//							Utils.SignUp_Fragment).commit();
                break;
        }

    }

    private void checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            emailid.startAnimation(shakeAnimation);
            password.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(this, view,
                    "Enter both credentials.");

        }
        // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(this, view,
                    "Your Email Id is Invalid.");
            // Else do login and do your stuff
        else {
            loginHandler();
        }

    }

    private void loginHandler()
    {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.LOGIN);
        requestPackage.setParam("email", emailid.getText().toString());
        requestPackage.setParam("password", password.getText().toString());
        requestPackage.setMethod("POST");
        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra(HttpService.REQUEST_PACKAGE, requestPackage);

        //intent.setData(Uri.parse(JSON_URL));
        startService(intent);

    }

    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    //@Override
//    protected void onStop() {
//        super.onStop();
//        LocalBroadcastManager.getInstance(getApplicationContext())
//                .unregisterReceiver(mBroadcastReceiver);
//    }
}
