package com.codewarriors4.tiffin.utils;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpHelper {
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

    public static String downloadFromFeed(RequestPackage requestPackage)
            throws Exception {

        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();

        OkHttpClient client = new OkHttpClient();

        Request.Builder requestBuilder = new Request.Builder()
                .url(address)
                .addHeader("Accept", "application/json");


        if (requestPackage.getMethod().equals("GET")
                ) {
            if(encodedParams.length() > 0){
                address = String.format("%s?%s", address, encodedParams);
            }


            for (String key : requestPackage.getHeaders().keySet()) {
                if(key.equals("Authorization")){
                    requestBuilder.header(key, requestPackage.getHeaders().get(key));
                    Log.i(key, requestPackage.getHeaders().get(key));
                }else{
                    requestBuilder.addHeader(key, requestPackage.getHeaders().get(key));
                    Log.i(key, requestPackage.getHeaders().get(key));
                }
            }


        }


        if(requestPackage.getMethod().equals("POST")){

            if(!requestPackage.getFiles().isEmpty()){
                MultipartBody.Builder mulBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                Map<String, String> requestPackageParams = requestPackage.getParams();
                Map<String, File> requestPackageFiles = requestPackage.getFiles();
                for (String key: requestPackageParams.keySet()) {
                    mulBuilder.addFormDataPart(key, requestPackageParams.get(key));
                }

                for (String key : requestPackageFiles.keySet()) {
                    mulBuilder.addFormDataPart("file",
                            requestPackageFiles.get(key).getName(),
                            RequestBody.create(MEDIA_TYPE_JPEG, requestPackageFiles.get(key)));
                }

                MultipartBody build = mulBuilder.build();
                requestBuilder.post(build);
                for (String key : requestPackage.getHeaders().keySet()) {
                    if(key.equals("Authorization")){
                        requestBuilder.header(key, requestPackage.getHeaders().get(key));
                        Log.i(key, requestPackage.getHeaders().get(key));
                    }else{
                        requestBuilder.addHeader(key, requestPackage.getHeaders().get(key));
                        Log.i(key, requestPackage.getHeaders().get(key));
                    }
                }

            }else{
                Map<String, String> params = requestPackage.getParams();

                FormBody.Builder formData = new FormBody.Builder();
                for (String key: params.keySet()) {
                    formData.add(key, params.get(key));
                }

//            RequestBody requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("somParam", "someValue")
//                    .build();

                RequestBody formBody = formData.build();
                requestBuilder.post(formBody);
                for (String key : requestPackage.getHeaders().keySet()) {
                    if(key.equals("Authorization")){
                        requestBuilder.header(key, requestPackage.getHeaders().get(key));
                        Log.i(key, requestPackage.getHeaders().get(key));
                    }else{
                        requestBuilder.addHeader(key, requestPackage.getHeaders().get(key));
                        Log.i(key, requestPackage.getHeaders().get(key));
                    }
                }
            }
//            MultipartBody.Builder builder = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM);
//            Map<String, String> params = requestPackage.getParams();
//            for (String key: params.keySet()) {
//                builder.addFormDataPart(key, params.get(key));
//            }
//
//            RequestBody requestBody = builder.build();
//            requestBuilder.method("POST", requestBody);


//                builder.addFormDataPart(key, params.get(key));
//            }
        }

        if(requestPackage.getMethod().equals("JSON")){
             MediaType mediaType
                    = MediaType.parse("application/json");
            String jsonData = requestPackage.getParams().get("jsonData");
            requestBuilder
                    .post(RequestBody.create(mediaType, jsonData));
        }

        Request request = requestBuilder.build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            Log.d("responseERROR", "downloadFromFeed: " + response.body().string());
            throw new Exception(getResponseBody(response.body().string()));
        }
    }

    private static String getResponseBody(String str){
        try {
            return new JSONObject(str).getString("message");
        } catch (JSONException e) {
            return "JSON ERROR";
        }
    }


}