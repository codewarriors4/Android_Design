package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomemakerViewProfile extends AppCompatActivity  {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int PHONELENGHT = 10;
    public ProgressBar progressBar;

    private  View view;
    @BindView(R.id.hm_name)
    TextView homemakerName;
    @BindView(R.id.hm_street)
    TextView homemakerStreet;
    @BindView(R.id.hm_city)
    TextView homemakerCity;
    @BindView(R.id.hm_province)
    TextView homemakerProvince;
    @BindView(R.id.hm_postal)
    TextView ho0memakerPostal;
    @BindView(R.id.hm_phone)
    TextView homemakerPhone;
    @BindView(R.id.hm_email)
    TextView homemakerEmail;

    boolean imageSelected;
    ImageView mImageView;
    String mCurrentPhotoPath;

    Bitmap imageBitmap;
    File uploadLicence;

    private SessionUtli sessionUtli;
    private FrameLayout progress;
    private LinearLayout profileBody;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
//            String str = (String) intent
//                    .getStringExtra(HttpService.MY_SERVICE_PAYLOAD);
            
             
                    RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
                    if(respondPackage.getParams().containsKey(RespondPackage.SUCCESS)){
                        Log.d("JsonResponseData", "onReceive: "
                                + respondPackage.getParams().get(RespondPackage.SUCCESS));
                        Toast.makeText(context, "Update Succesfully", Toast.LENGTH_SHORT).show();

                    }else{
                        Log.d("JsonResponseData", "onReceive: "
                                + respondPackage.getParams().get(RespondPackage.FAILED));
                        Toast.makeText(context, "Please Select Image", Toast.LENGTH_SHORT).show();
                    }
                    
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        setTitle("HomeMaker Profile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homemaker_view_profile);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);
        profileBody = findViewById(R.id.profileBody);
        progress = findViewById(R.id.progress_overlay);
       // mImageView = findViewById(R.id.licence_image);
       // uploadLicenceButton.setOnCreateContextMenuListener(this);
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = getLayoutInflater().inflate(R.layout.homemaker_view_profile, container, false);
        new MyAsynTask().execute("");
        /*progressBar = (ProgressBar) findViewById(R.id.ts_view_hm_progress);
        progressBar.setVisibility(View.GONE);
        profileBody.setVisibility(View.VISIBLE);*/

    }


    private void initValues(HashMap<String, Object> hashMap)
    {
        homemakerName.append((String)hashMap.get("UserFname") + (String)hashMap.get("UserLname"));
        homemakerStreet.append((String)hashMap.get("hm_street"));
        homemakerCity.append((String)hashMap.get("hm_city"));
        homemakerProvince.append((String)hashMap.get("hm_province"));
        ho0memakerPostal.append((String)hashMap.get("hm_postal"));
        homemakerPhone.append((String)hashMap.get("hm_phone"));
        homemakerEmail.append((String)hashMap.get("hm_email"));
    }


    public String getHMDetails() throws Exception {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.TSVIEWHMPROFILE + "/81");
        requestPackage.setMethod("POST");
        //requestPackage.setParam("id", "21");
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        return HttpHelper.downloadFromFeed(requestPackage);
    }


    private class MyAsynTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                return getHMDetails();
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
                super.onPostExecute(aVoid);
                profileBody.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                HashMap<String, Object> hashMap = new Gson().fromJson(aVoid, HashMap.class);
                for (String key : hashMap.keySet()) {
                    Log.d("JSONVALUE", key + ": " + hashMap.get(key));
                }
                if(hashMap.get("UserZipCode") != null)
                    initValues(hashMap);
            }

            catch(Exception e){
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG);
            }
        }

    }




   /* @OnClick(R.id.submit_btn)
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
        }
//        }else if(!(imageSelected)){
//            new CustomToast().Show_Toast(this, view,
//                    "Please Select Image");
//        }
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
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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
                System.currentTimeMillis() + ".jpg");
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
        intent.setType("image*//*");
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
                dispatchTakePictureIntent();
                return true;
            case R.id.selectFromGallery:
                galleryAddPic();
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
        requestPackage.setParam("UserCity", city.getText().toString());
        requestPackage.setParam("UserZipCode", zipcodeView.getText().toString());
        requestPackage.setParam("UserCompanyName", "Tiffin Demo");
        if(imageSelected)
            requestPackage.setFile("file", uploadLicence);
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        requestPackage.setParam("UserStreet", streetName.getText().toString());
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
            profileBody.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String aVoid) {
            try {
                super.onPostExecute(aVoid);
                profileBody.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                HashMap<String, Object> hashMap = new Gson().fromJson(aVoid, HashMap.class);
                for (String key : hashMap.keySet()) {
                    Log.d("JSONVALUE", key + ": " + hashMap.get(key));
                }
                if(hashMap.get("UserZipCode") != null)
                    initValues(hashMap);
            }

            catch(Exception e){
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG);
            }
        }

    }


    private void initValues(HashMap<String, Object> hashMap)
    {
            firstNameView.append((String)hashMap.get("UserFname"));
            lastNameView.append((String)hashMap.get("UserLname"));
            phoneView.append((String)hashMap.get("UserPhone"));
            streetName.append((String)hashMap.get("UserStreet"));
            city.append((String)hashMap.get("UserCity"));
            countryView.append((String)hashMap.get("UserCountry"));
            zipcodeView.append((String)hashMap.get("UserZipCode"));
            provinceSpinner.setSelection(getIndex(provinceSpinner, (String)hashMap.get("UserProvince") ));
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

*/



}
