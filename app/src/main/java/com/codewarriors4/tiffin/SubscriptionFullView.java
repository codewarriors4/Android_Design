package com.codewarriors4.tiffin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.codewarriors4.tiffin.models.SubscribersListModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionFullView extends AppCompatActivity
{
    @BindView(R.id.full_name)
    public TextView fullName;
    @BindView(R.id.contact_inform)
    public TextView contact_inform;
    @BindView(R.id.package_cost)
    public TextView packageCost;
    @BindView(R.id.package_name)
    public TextView packageName;
    @BindView(R.id.package_desc)
    public TextView packageDesc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_full_view_layout);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        SubscribersListModel subscribersListModel = intent.getParcelableExtra("Example Item");
        
        initValue(subscribersListModel);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initValue(SubscribersListModel subscribersListModel)
    {
        fullName.setText("Full Name: "+subscribersListModel.getUserName());
        contact_inform.setText("User Detail: "+subscribersListModel.getUserStreet() + "\n" + subscribersListModel.getPhoneNumber());
        packageCost.setText("Package Cost: "+subscribersListModel.getPackageCost() + "CAD");
        packageName.setText("Package Name: "+subscribersListModel.getPackageName());
        packageDesc.setText("Package Desc: "+subscribersListModel.getPackageDesc());


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
