package com.codewarriors4.tiffin;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codewarriors4.tiffin.services.HttpService;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.RespondPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomemakerCreatePackages extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    static final int PHONELENGHT = 10;
    static final int DIALOG_ID = 0;
    final int PERMISSION_REQUEST_CAMERA = 3;
    final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 4;


    private  View view;
    @BindView(R.id.package_name)
    EditText packageName;
    @BindView(R.id.pack_desc)
    EditText packDesc;
    @BindView(R.id.pack_cost)
    EditText packCost;

    @BindView(R.id.menu_submit_btn)
    Button submitPackagesBtn;

    @BindView(R.id.image_upload_btn)
    ImageButton imageUploadBtn;
    private SessionUtli sessionUtli;
    private FrameLayout progress;
    private LinearLayout profileBody;

    @BindView(R.id.image_preview)
    ImageView mImageView;

    String mCurrentPhotoPath;
    Bitmap bitmap;

    Bitmap imageBitmap;
    File uploadLicence;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
//            String str = (String) intent
//                    .getStringExtra(HttpService.MY_SERVICE_PAYLOAD);
            
             
                    RespondPackage respondPackage = (RespondPackage) intent.getParcelableExtra(HttpService.MY_SERVICE_PAYLOAD);
                    if(respondPackage.getParams().containsKey(RespondPackage.SUCCESS)){
                        Log.d("JsonResponseData", "onReceive: "
                                + respondPackage.getParams().get(RespondPackage.SUCCESS));
                        Toast.makeText(context, "Update Succesfull", Toast.LENGTH_SHORT).show();

                    }else{
                        Log.d("JsonResponseData", "onReceive: "
                                + respondPackage.getParams().get(RespondPackage.FAILED));
                        Toast.makeText(context, "Please Select Image", Toast.LENGTH_SHORT).show();
                    }
                    
        }
    };
    private boolean imageSelected;


    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Manage Package Details");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homemaker_create_update_package);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);
        profileBody = findViewById(R.id.profile_body);
        progress = findViewById(R.id.progress_overlay);
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = getLayoutInflater().inflate(R.layout.login_layout, container, false);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        //new MyAsynTask().execute("");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


    }

    @OnClick(R.id.image_upload_btn)
    public void takePhoto(View view){
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.upload_licence, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upload_licence, menu);
    }



    @OnClick(R.id.menu_submit_btn)
    public void submit(View view){
        checkValidation();
    }

    private void checkValidation()
    {
        String getPackName = packageName.getText().toString();
        String getPackDesc = packDesc.getText().toString();
        String getPackCost = packCost.getText().toString();

       // final String regExp = "^\\d{1,6}(\\.\\d{2})?$";

        final String regExp = "^(?!^0\\.00$)(([1-9][\\d]{0,6})|([0]))\\.[\\d]{2}$";


        final Pattern pattern = Pattern.compile(regExp);

// This can be repeated in a loop with different inputs:
        Matcher matcher = pattern.matcher(getPackCost);
        if(!matcher.matches()){

            new CustomToast().Show_Toast(this, view,
                    "Please provide 3 digits before and 2 digits after decimal");

        }else if(Double.parseDouble(getPackCost) < 1){

            new CustomToast().Show_Toast(this, view,
                    "Please enter package cost greater than 1");

        } else if(getPackName.equals("") || getPackDesc.equals("") || getPackCost.equals("")){
            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");
        }
        else{
            submit();
        }

    }

    public void submit(){
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.HMCREATEMENU);
        requestPackage.setMethod("POST");
        requestPackage.setParam("HMPName", packageName.getText().toString().trim());
        requestPackage.setParam("HMPDesc", packDesc.getText().toString().trim());
        requestPackage.setParam("HMPCost", packCost.getText().toString().trim());
        if(imageSelected)
            requestPackage.setFile("file", uploadLicence);
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra(HttpService.REQUEST_PACKAGE, requestPackage);

        startService(intent);
    }

    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


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

    private void requestCameraPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            new AlertDialog.Builder(this)
                    .setTitle("Permision Needed")
                    .setMessage("This permission is needed to start activity")
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(HomemakerCreatePackages.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
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
                                    ActivityCompat.requestPermissions(HomemakerCreatePackages.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_EXTERNAL_STORAGE);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Photo Capture Error", Toast.LENGTH_LONG);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.codewarriors4.tiffin.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void galleryAddPic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SELECT_IMAGE);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//            mImageView.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray())));
//            uploadLicence = saveImageToFile(bytes);
//            imageSelected = true;
//            sessionUtli.setValue("isLicenceUploaded", "true");
            File file = new File(mCurrentPhotoPath);
            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), Uri.fromFile(file));
                if (bitmap != null) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                    //bitmap = rotateImage(this.bitmap, ExifInterface.ORIENTATION_ROTATE_180);
                    rotateImage(bitmap);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    //mImageView.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray())));
                    uploadLicence = saveImageToFile(bytes);
                    mImageView.setImageBitmap(bitmap);
                    mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(HomemakerCreatePackages.this, ImagePreviewActivity.class);
                            i.putExtra("bitmap_img", uploadLicence.getAbsolutePath());
                            startActivity(i);
                        }
                    });

                    imageSelected = true;
                    sessionUtli.setValue("isLicenceUploaded", "true");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK){
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                uploadLicence = saveImageToFile(bytes);
                mImageView.setImageBitmap(bitmap);
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HomemakerCreatePackages.this, ImagePreviewActivity.class);
                        i.putExtra("bitmap_img", uploadLicence.getAbsolutePath());
                        startActivity(i);
                    }
                });
                imageSelected = true;
                sessionUtli.setValue("isLicenceUploaded", "true");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private void rotateImage(Bitmap bitmap){
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mCurrentPhotoPath);
        }catch (Exception e){

        }

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_NORMAL:
                //return bitmap;
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        this.bitmap = rotatedBitmap;
    }

}
