package com.codewarriors4.tiffin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;

public class MainActivity extends AppCompatActivity{


    Button loginActionButton;
    Button signupActionButton;
    private static final int PERMISSION_REQUEST_GPS = 1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE);
        accessPermission();
    }

    private void accessPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestGPSPermission();
        }else{
            startActivity();
        }
    }

    private void requestGPSPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this)
                    .setTitle("Permision Needed")
                    .setMessage("This permission is needed to start activity")
                    .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_GPS);
                            }
                        }).create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_GPS);
        }

    }

    private void startActivity()
    {
        //Toast.makeText(this, "Activity Started", Toast.LENGTH_LONG).show();
        if(!sharedPreferences.contains("access_token")){

            setContentView(R.layout.activity_main);
            loginActionButton = findViewById(R.id.button);
            signupActionButton = findViewById(R.id.button1);
            setOnclickHandler();

        }else{
            new UserHandler().startActivity(SessionUtli.getSession(sharedPreferences), MainActivity.this);
        }
    }

    private void setOnclickHandler() {
        loginActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });
        signupActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent= new Intent(v.getContext(), SignupActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                                            int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_GPS) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity();
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }


    // Replace Login Fragment with animation
    protected void replaceLoginFragment() {

//        fragmentManager
//                .beginTransaction()
//                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
//                .replace(R.id.frameContainer, new Login_Fragment(),
//                        Utils.Login_Fragment).commit();
    }

//    public void onLoginPressed(){
//        fragmentManager
//                .beginTransaction()
//                .replace(R.id.frameContainer, new Login_Fragment(),
//                        Utils.Login_Fragment).commit();
//    }

    @Override
    public void onBackPressed() {

        // Find the tag of signup and forgot password fragment
//        Fragment SignUp_Fragment = fragmentManager
//                .findFragmentByTag(Utils.SignUp_Fragment);
//        Fragment ForgotPassword_Fragment = fragmentManager
//                .findFragmentByTag(Utils.ForgotPassword_Fragment);
//
//        // Check if both are null or not
//        // If both are not null then replace login fragment else do backpressed
//        // task
//
//        if (SignUp_Fragment != null)
//            replaceLoginFragment();
//        else if (ForgotPassword_Fragment != null)
//            replaceLoginFragment();
//        else
//            super.onBackPressed();
    }





}
