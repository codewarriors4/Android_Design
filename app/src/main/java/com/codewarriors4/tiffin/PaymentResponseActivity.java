package com.codewarriors4.tiffin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PaymentResponseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        String reponse = intent.getStringExtra("isSuccess");
        if(reponse.contains("success")){
            setContentView(R.layout.payment_sucess_popup);
            Button btn =  (Button) findViewById(R.id.payment_btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initSuccessResponse(intent);
                }
            });
        }else{
            setContentView(R.layout.payment_error_popup);
            Button btn =  (Button) findViewById(R.id.payment_btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initFailResponse(intent);
                }
            });
        }


    }

    private void initFailResponse(Intent intent)
    {

    }

    private void initSuccessResponse(Intent intent)
    {

    }
}
