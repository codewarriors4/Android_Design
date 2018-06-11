package com.codewarriors4.tiffin;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.codewarriors4.tiffin.utils.Utils;

public class MainActivity extends AppCompatActivity{

    private static android.support.v4.app.FragmentManager fragmentManager;
    Button loginActionButton;
    Button signupActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginActionButton = findViewById(R.id.button);
        signupActionButton = findViewById(R.id.button1);
        setOnclickHandler();
        //fragmentManager = getSupportFragmentManager();

        // If savedinstnacestate is null then replace login fragment
//        if (savedInstanceState == null) {
//            fragmentManager
//                    .beginTransaction()
//                    .replace(R.id.frameContainer, new Login_Fragment(),
//                            Utils.Login_Fragment).commit();
//        }

        // On close icon click finish activity
//        findViewById(R.id.close_activity).setOnClickListener(
//                new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//                        finish();
//
//                    }
//                });

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
