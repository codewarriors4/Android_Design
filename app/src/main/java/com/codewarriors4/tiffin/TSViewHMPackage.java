package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TSViewHMPackage extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int PHONELENGHT = 10;


    private  View view;
    @BindView(R.id.hm_display_pack_title)
    TextView packageName;
    @BindView(R.id.hm_display_pack_desc)
    TextView packDesc;
    @BindView(R.id.hm_display_pack_cost)
    TextView packCost;
    @BindView(R.id.pack_cost_value)
    TextView summaryPackCost;
    @BindView(R.id.taxes_value)
    TextView summaryPackTaxesValue;
    @BindView(R.id.total_cost_value)
    TextView summaryTotalCostValue;

    @BindView(R.id.subscribe_btn)
    Button subscribe;

    @BindView(R.id.image_preview)
    ImageView mImageView;

    String mCurrentPhotoPath;
    Bitmap bitmap;

    File uploadLicence;




    private SessionUtli sessionUtli;
    private FrameLayout progress;
    private LinearLayout profileBody;
    private JsonObject hmPackageDetails;







    protected void onCreate(Bundle savedInstanceState) {
        setTitle("View Package Details");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ts_view_hm_package);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);
        profileBody = findViewById(R.id.profile_body);
        progress = findViewById(R.id.progress_overlay);
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = getLayoutInflater().inflate(R.layout.login_layout, container, false);
        new MyAsynTask().execute("");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }




    public String getPackageInfo() throws Exception {
        Intent i = getIntent();
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.HMGETPACKAGEDETAIL);
        requestPackage.setMethod("POST");
        requestPackage.setParam("HMPId", i.getStringExtra("package_id"));
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        return HttpHelper.downloadFromFeed(requestPackage);
    }

    private class MyAsynTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                return getPackageInfo();
            } catch (Exception e) {
                return e.getMessage();
            }


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPreExecute() {
            profileBody.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String aVoid) {
            try {
                Log.d("Testing data", "onPostExecute: " + aVoid);
                super.onPostExecute(aVoid);
                profileBody.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                JsonObject jsonObject = new Gson().fromJson(aVoid, JsonObject.class);
                hmPackageDetails = jsonObject.get("home_maker_packages")       // get the 'user' JsonElement
                        .getAsJsonObject(); // get it as a JsonObject

                //System.out.println(name);

                // HashMap<String, Object> hashMap = new Gson().fromJson(aVoid, HashMap.class);
           /*     for (String key : hashMap.keySet()) {
                    Log.d("JSONVALUE", key + ": " + hashMap.get(key));
                }*/

                Log.d("JSONVALUE", "test");

                //if(hashMap.get("UserZipCode") != null)
                initValues(hmPackageDetails);
            }

            catch(Exception e){
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG);
            }
        }

    }


    private void initValues(final JsonObject hmPackageDetails)
    {

            packageName.setText((String)hmPackageDetails.get("HMPName").getAsString());
            packDesc.setText((String)hmPackageDetails.get("HMPDesc").getAsString());
            packCost.setText((String)hmPackageDetails.get("HMPCost").getAsString() + " CAD");
            summaryPackCost.setText((String)hmPackageDetails.get("HMPCost").getAsString() + " CAD");
            summaryPackTaxesValue.setText((String)hmPackageDetails.get("hst").getAsString() + " CAD");
            summaryTotalCostValue.setText((String)hmPackageDetails.get("total").getAsString() + " CAD");

            final String HMPId = (String)hmPackageDetails.get("HMPId").getAsString();
            final String HMPCost = (String)hmPackageDetails.get("HMPCost").getAsString();
            final String hst = (String)hmPackageDetails.get("hst").getAsString();
            final String total = (String)hmPackageDetails.get("total").getAsString();

        try {
            byte[] decodedString = Base64.decode(hmPackageDetails.get("prod_encoded_img").getAsString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            mImageView.setImageBitmap(decodedByte);
            uploadLicence = saveImageToFile(bytes);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(TSViewHMPackage.this, ImagePreviewActivity.class);
                    i.putExtra("bitmap_img", uploadLicence.getAbsolutePath());
                    startActivity(i);
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Please Upload Licence Image", Toast.LENGTH_LONG);
        }



        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent old = getIntent();
                Intent i = new Intent(TSViewHMPackage.this, TSSubPayment.class);
                i.putExtra("package_id", HMPId);
                i.putExtra("package_cost", HMPCost);
                i.putExtra("package_hst", hst);
                i.putExtra("package_total", total);
                i.putExtra("HomeMakerId",old.getStringExtra("HomeMakerId"));
                startActivity(i);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public File saveImageToFile(ByteArrayOutputStream bytes)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File destination = new File(this.getCacheDir(),
                imageFileName + ".jpg");
        Log.d("imageURI", "saveImageToFile: " + destination.getAbsolutePath());
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destination;

    }






}
