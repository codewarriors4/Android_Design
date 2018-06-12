package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.codewarriors4.tiffin.DemoActivity;
import com.codewarriors4.tiffin.LoginActivity;
import com.codewarriors4.tiffin.R;
import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener
{
    private TextView submitButton;
    private TextView backButton;
    private ProgressBar progressBar;
    private EditText emailText;
    private Animation shakeAnimation;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
            if(respondPackage.getParams().containsKey(RespondPackage.SUCCESS)){
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.SUCCESS));
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context,
                        " Reset Link was sent \n Please check your inbox", Toast.LENGTH_LONG)
                        .show();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }else{
                submitButton.setEnabled(true);
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.FAILED));
                Toast.makeText(context,
                        respondPackage.getParams().get(RespondPackage.FAILED), Toast.LENGTH_SHORT)
                        .show();
                progressBar.setVisibility(View.GONE);
            }

        }
    };



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_layout);
        initCompontent();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake);

    }

    private void initCompontent()
    {
        submitButton = findViewById(R.id.forgot_button);
        backButton = findViewById(R.id.backToLoginBtn);
        emailText = findViewById(R.id.registered_emailid);
        progressBar = findViewById(R.id.forgot_password_progressBar);
        submitButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(emailText.getText());

        switch(v.getId()) {
            case R.id.forgot_button:
                if((!emailText.getText().equals("")) && m.find()){
                    emailText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    submitButton.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    RequestPackage requestPackage = new RequestPackage();
                    requestPackage.setEndPoint(Constants.BASE_URL + Constants.FORGOTPASSWORD);
                    requestPackage.setParam("email", emailText.getText().toString());
                    requestPackage.setMethod("POST");
                    Intent intent = new Intent(this, HttpService.class);
                    intent.putExtra(HttpService.REQUEST_PACKAGE, requestPackage);
                    startService(intent);
                }else{
                    emailText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    emailText.startAnimation(shakeAnimation);
                    ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
                    View view = getLayoutInflater().inflate(R.layout.forgotpassword_layout, container, false);
                    new CustomToast().Show_Toast(this, view,
                            "Your Email Id is Invalid.");

                }
                break;
            case R.id.backToLoginBtn:
                finish();
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
