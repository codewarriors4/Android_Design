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
    @BindView(R.id.email_view)
    public TextView emailView;
    @BindView(R.id.address_view)
    public TextView addressView;
    @BindView(R.id.phone_no_view)
    public TextView phoneNoView;
    @BindView(R.id.package_name)
    public TextView packageName;
    @BindView(R.id.descri_view)
    public TextView descriView;
    @BindView(R.id.price_view)
    public TextView priceView;
    @BindView(R.id.start_date)
    public TextView startDateView;
    @BindView(R.id.end_date)
    public TextView endDateView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_full_view_layout);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        setTitle("Subscription Details");
        SubscribersListModel subscribersListModel = intent.getParcelableExtra("Example Item");
        
        initValue(subscribersListModel);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initValue(SubscribersListModel subscribersListModel)
    {
        fullName.setText(subscribersListModel.getUserName());
        emailView.setText(subscribersListModel.getUsereEmail());
        addressView.setText(subscribersListModel.getUserStreet());
        phoneNoView.setText(subscribersListModel.getPhoneNumber());
        packageName.setText(subscribersListModel.getPackageName());
        descriView.setText(subscribersListModel.getPackageDesc());
        priceView.setText(subscribersListModel.getPackageCost() + "CAD");
        packageName.setText(subscribersListModel.getPackageName());
        startDateView.setText(subscribersListModel.getStartDate());
        endDateView.setText(subscribersListModel.getEndDate());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
