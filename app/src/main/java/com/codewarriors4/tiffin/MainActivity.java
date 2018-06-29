package com.codewarriors4.tiffin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codewarriors4.tiffin.utils.Constants;

public class MainActivity extends AppCompatActivity{


    Button loginActionButton;
    Button signupActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dashboard);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE);

        //super.onCreate(savedInstanceState);
        if(!sharedPreferences.contains("access_token")){
            setContentView(R.layout.activity_main);
            loginActionButton = findViewById(R.id.button);
            signupActionButton = findViewById(R.id.button1);
            setOnclickHandler();
        }else{
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
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
