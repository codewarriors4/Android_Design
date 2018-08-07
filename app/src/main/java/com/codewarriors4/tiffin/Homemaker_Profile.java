package com.codewarriors4.tiffin;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;

import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;
import com.codewarriors4.tiffin.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.felix.imagezoom.ImageZoom;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class Homemaker_Profile extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int PHONELENGHT = 10;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;
    final int PERMISSION_REQUEST_CAMERA = 3;
    final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 4;

    private View view;
    @BindView(R.id.first_name)
    EditText firstNameView;
    @BindView(R.id.last_name)
    EditText lastNameView;
    @BindView(R.id.phone)
    EditText phoneView;
    @BindView(R.id.street_name)
    EditText streetName;
    @BindView(R.id.city)
    EditText city;
    @BindView(R.id.province_spinner)
    Spinner provinceSpinner;
    @BindView(R.id.country)
    EditText countryView;
    @BindView(R.id.zipcode)
    EditText zipcodeView;

    @BindView(R.id.hm_lic_exp_date)
    TextView exp_date_text;
    @BindView(R.id.hm_profile_datepicker)
    ImageButton license_picker;


    @BindView(R.id.upload_btn)
    Button uploadLicenceButton;
    @BindView(R.id.submit_btn)
    Button submitProfileButton;
    boolean imageSelected;
    ImageZoom mImageView;
    String mCurrentPhotoPath;

    Bitmap imageBitmap;
    File uploadLicence;

    private SessionUtli sessionUtli;
    private FrameLayout progress;
    private LinearLayout profileBody;
    JsonObject hmDetailsJSONObj;


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
//            String str = (String) intent
//                    .getStringExtra(HttpService.MY_SERVICE_PAYLOAD);


            RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
            if (respondPackage.getParams().containsKey(RespondPackage.SUCCESS)) {
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.SUCCESS));
                Toast.makeText(context, "Update Succesfully", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Log.d("JsonResponseData", "onReceive: "
                        + respondPackage.getParams().get(RespondPackage.FAILED));
                Toast.makeText(context, "Please Select Image", Toast.LENGTH_SHORT).show();
            }

        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        setTitle("HomeMaker Profile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homemaker_profile);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);
        profileBody = findViewById(R.id.profile_body);
        progress = findViewById(R.id.progress_overlay);
        //mImageView = findViewById(R.id.license_preview);
         mImageView = findViewById(R.id.license_preview);
        //mImageView.setImageBitmap(thumbnail);
//        mImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent();
////                intent.setAction(Intent.ACTION_VIEW);
////                intent.setDataAndType(Uri.parse(Homemaker_Profile.this.getCacheDir().toURI().toString() + "1" + ".jpg"), "image/*");
////                startActivity(intent);
////            }
//        });
        uploadLicenceButton.setOnCreateContextMenuListener(this);
        showDialogOnButtonClick();
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = getLayoutInflater().inflate(R.layout.login_layout, container, false);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        new MyAsynTask().execute("");

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void showDialogOnButtonClick() {
        license_picker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);

                    }
                }
        );


    }

    protected DatePickerDialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        } else {
            return null;
        }
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month;
            day_x = dayOfMonth;

            String date = String.valueOf(year_x) + "/" + String.valueOf(month_x + 1) + "/" + String.valueOf(day_x);

            exp_date_text.setText(date);
        }

};

    @OnClick(R.id.submit_btn)
    public void submit(View view){
        checkValidation();
    }

    private void checkValidation()
    {
        String getFirstName = firstNameView.getText().toString();
        String getLastName = lastNameView.getText().toString();
        String getPhoneNumber = phoneView.getText().toString();
        String getStreetName = streetName.getText().toString();
        String getCity = city.getText().toString();
        String province = (String)provinceSpinner.getSelectedItem();
        String getCountry = countryView.getText().toString();
        String zipCode = zipcodeView.getText().toString();
        String lic_exp_date_txt = exp_date_text.getText().toString();

        //Toast.makeText(this, getFirstName + getLastName + getPhoneNumber + province , Toast.LENGTH_SHORT).show();
        Pattern p = Pattern.compile(Utils.postalRegEx);
        //Pattern phone = Pattern.compile(Utils.phoneRegEx);

       // Matcher phoneMatch = phone.matcher(getPhoneNumber.trim());
        Matcher m = p.matcher(zipCode.trim());
        //Toast.makeText(this, ""+ (getPhoneNumber.length() == PHONELENGHT) + " " +m.find() , Toast.LENGTH_SHORT).show();
//        if(sessionUtli.getValue("isLicenceUploaded").equals("true"))
//            imageSelected = true;
        if(getFirstName.equals("") || getLastName.equals("") || getPhoneNumber.equals("")
                || getStreetName.equals("") || getCity.equals("") || province.equals("")
                || getCountry.equals("") || zipCode.equals("")){
            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");
        }else if(!m.find()){
            new CustomToast().Show_Toast(this, view,
                    "Invalid Zip Code");
        }else if(!(getPhoneNumber.length() == PHONELENGHT)){
            new CustomToast().Show_Toast(this, view,
                    "Invalid Phone");
        } else if(lic_exp_date_txt.equals("") || lic_exp_date_txt.equals("Pick License Expiry Date") ){
            new CustomToast().Show_Toast(this, view,
                    "Please enter valid expiry date");
        }
        else{
            submit();
        }

    }

    @OnClick(R.id.upload_btn)
    public void takePhoto(View view){
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.upload_licence, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            mImageView.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray())));
            uploadLicence = saveImageToFile(bytes);
            imageSelected = true;
            sessionUtli.setValue("isLicenceUploaded", "true");
        }
        else if(requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK){
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                uploadLicence = saveImageToFile(bytes);
                mImageView.setImageBitmap(bitmap);
                imageSelected = true;
                sessionUtli.setValue("isLicenceUploaded", "true");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public File saveImageToFile(ByteArrayOutputStream bytes)
    {
        File destination = new File(this.getCacheDir(),
                "1" + ".jpg");
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

    private void galleryAddPic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SELECT_IMAGE);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upload_licence, menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photoFromCamera:
                requestCameraPermission();
                return true;
            case R.id.selectFromGallery:
                requestGalleryPermission();
                return true;
            default:
                return false;
        }
    }

    public void submit(){
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.HOMEMAKERPROFILE);
        requestPackage.setMethod("POST");
        requestPackage.setParam("UserFname", firstNameView.getText().toString().trim());
        requestPackage.setParam("UserLname", lastNameView.getText().toString().trim());
        requestPackage.setParam("UserPhone", phoneView.getText().toString().trim());
        requestPackage.setParam("UserCountry", "Canada");
        requestPackage.setParam("UserProvince", "ON");
        requestPackage.setParam("UserCity", city.getText().toString().trim());
        requestPackage.setParam("UserZipCode", zipcodeView.getText().toString().trim());
        requestPackage.setParam("UserCompanyName", "Tiffin Demo");
        if(imageSelected)
            requestPackage.setFile("file", uploadLicence);
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        requestPackage.setParam("UserStreet", streetName.getText().toString());
        requestPackage.setParam("HMLicenseExpiryDate", exp_date_text.getText().toString());
        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra(HttpService.REQUEST_PACKAGE, requestPackage);

        startService(intent);
    }

    public String getUserInfo() throws Exception {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.HOMEMAKERPROFILEVIEW);
        requestPackage.setMethod("POST");
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        return HttpHelper.downloadFromFeed(requestPackage);
    }

    private class MyAsynTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                return getUserInfo();
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
            //  profileBody.setVisibility(View.GONE);
            //progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String aVoid) {
            try {
                Log.d("Testing data", "onPostExecute: " + aVoid);
                super.onPostExecute(aVoid);
             //   profileBody.setVisibility(View.VISIBLE);
              //  progress.setVisibility(View.GONE);

                 hmDetailsJSONObj = new Gson().fromJson(aVoid, JsonObject.class);

/*                HashMap<String, Object> hashMap = new Gson().fromJson(aVoid, HashMap.class);
                for (String key : hashMap.keySet()) {
                    Log.d("JSONVALUE", key + ": " + hashMap.get(key));
                }
                if(hashMap.get("UserZipCode") != null)
                    initValues(hmDetailsJSONObj);*/
                initValues(hmDetailsJSONObj);
            }

            catch(Exception e){
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG);
            }
        }

    }


    private void initValues(JsonObject hmdetails) throws JSONException {
            firstNameView.setText(hmdetails.get("UserFname").getAsString());
            lastNameView.setText(hmdetails.get("UserLname").getAsString());
        phoneView.setText(hmdetails.get("UserPhone").getAsString());
        streetName.setText(hmdetails.get("UserStreet").getAsString());
        city.setText(hmdetails.get("UserCity").getAsString());
        countryView.setText(hmdetails.get("UserCountry").getAsString());
        zipcodeView.setText(hmdetails.get("UserZipCode").getAsString());
        provinceSpinner.setSelection(getIndex(provinceSpinner, hmdetails.get("UserProvince").getAsString()));
        exp_date_text.setText(hmdetails.get("HMLicenseExpiryDate").getAsString());






    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestCameraPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            new AlertDialog.Builder(this)
                    .setTitle("Permision Needed")
                    .setMessage("This permission is needed to start activity")
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(Homemaker_Profile.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                                }
                            }).create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }

    }
    private void requestGalleryPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permision Needed")
                    .setMessage("This permission is needed to start activity")
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(Homemaker_Profile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_EXTERNAL_STORAGE);
                                }
                            }).create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_EXTERNAL_STORAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                                           int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CAMERA) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
        if (requestCode == PERMISSION_REQUEST_EXTERNAL_STORAGE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleryAddPic();
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
}
