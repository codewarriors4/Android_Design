package com.codewarriors4.tiffin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v7.widget.PopupMenu;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;

import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.codewarriors4.tiffin.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class Homemaker_Profile extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int PHONELENGHT = 10;


    private  View view;
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
    @BindView(R.id.upload_btn)
    Button uploadLicenceButton;
    @BindView(R.id.submit_btn)
    Button submitProfileButton;
    boolean imageSelected;
    ImageView mImageView;
    String mCurrentPhotoPath;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homemaker_profile);
        ButterKnife.bind(this);
        mImageView = findViewById(R.id.licence_image);
        uploadLicenceButton.setOnCreateContextMenuListener(this);
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = getLayoutInflater().inflate(R.layout.login_layout, container, false);
    }



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
        //Toast.makeText(this, getFirstName + getLastName + getPhoneNumber + province , Toast.LENGTH_SHORT).show();
        Pattern p = Pattern.compile(Utils.postalRegEx);
        //Pattern phone = Pattern.compile(Utils.phoneRegEx);

       // Matcher phoneMatch = phone.matcher(getPhoneNumber.trim());
        Matcher m = p.matcher(zipCode.trim());
        //Toast.makeText(this, ""+ (getPhoneNumber.length() == PHONELENGHT) + " " +m.find() , Toast.LENGTH_SHORT).show();
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
        }else if(!imageSelected){
            new CustomToast().Show_Toast(this, view,
                    "Please Select Image");
        }
        else{
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
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
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            imageSelected = true;
        }
        else if(requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK){
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mImageView.setImageBitmap(bitmap);
                imageSelected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                dispatchTakePictureIntent();
                return true;
            case R.id.selectFromGallery:
                galleryAddPic();
                return true;
            default:
                return false;
        }
    }





}
