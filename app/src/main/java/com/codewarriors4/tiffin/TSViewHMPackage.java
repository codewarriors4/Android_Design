package com.codewarriors4.tiffin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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




    private SessionUtli sessionUtli;
    private FrameLayout progress;
    private LinearLayout profileBody;
    private JsonObject hmPackageDetails;


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
        setTitle("View Package Details");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ts_view_hm_package);
        sessionUtli = SessionUtli.getSession(getSharedPreferences(Constants.SHAREDPREFERNCE, MODE_PRIVATE));
        ButterKnife.bind(this);
        profileBody = findViewById(R.id.profile_body);
        progress = findViewById(R.id.progress_overlay);
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = getLayoutInflater().inflate(R.layout.login_layout, container, false);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(HttpService.MY_SERVICE_MESSAGE));
        new MyAsynTask().execute("");
    }


/*

    @OnClick(R.id.menu_submit_btn)
    public void submit(View view){
        checkValidation();
    }

    private void checkValidation()
    {
        String getPackName = packageName.getText().toString();
        String getPackDesc = packDesc.getText().toString();
        String getPackCost = packCost.getText().toString();

        if(getPackName.equals("") || getPackDesc.equals("") || getPackCost.equals("")){
            new CustomToast().Show_Toast(this, view,
                    "All fields are required.");
        }
        else{
            submit();
        }

    }
*/




/*    @Override
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
    }*/

/*    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
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
    }*/

/*    public void submit(){
        Intent i = getIntent();

        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.HMUPDATEMENU);
        requestPackage.setMethod("POST");
        requestPackage.setParam("HMPId", i.getStringExtra("package_id"));
        requestPackage.setParam("HMPName", packageName.getText().toString().trim());
        requestPackage.setParam("HMPDesc", packDesc.getText().toString().trim());
        requestPackage.setParam("HMPCost", packCost.getText().toString().trim());
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra(HttpService.REQUEST_PACKAGE, requestPackage);

        startService(intent);
    }*/

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

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TSViewHMPackage.this, TSSubPayment.class);
                i.putExtra("package_id", (String)hmPackageDetails.get("HMPId").getAsString());
                i.putExtra("package_cost", (String)hmPackageDetails.get("HMPCost").getAsString());
                i.putExtra("package_hst", (String)hmPackageDetails.get("hst").getAsString());
                i.putExtra("package_total", (String)hmPackageDetails.get("total").getAsString());
                startActivity(i);
            }
        });



    }

/*    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }*/
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }





}
