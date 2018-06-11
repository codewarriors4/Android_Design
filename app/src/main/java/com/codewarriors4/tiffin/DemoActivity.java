package com.codewarriors4.tiffin;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.codewarriors4.tiffin.R;

public class DemoActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout);
        String response = getIntent().getStringExtra("response");
        Log.d("DemoActivityResponse", ""+response);
        TextView textView = (TextView)findViewById(R.id.textView4);
        textView.append(response);
    }
}
