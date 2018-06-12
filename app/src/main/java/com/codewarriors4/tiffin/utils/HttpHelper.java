package com.codewarriors4.tiffin.utils;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
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

    public static String downloadFromFeed(RequestPackage requestPackage)
            throws IOException {

        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();

        if (requestPackage.getMethod().equals("GET") &&
                encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }

        OkHttpClient client = new OkHttpClient();

        Request.Builder requestBuilder = new Request.Builder()
                .url(address)
                .addHeader("Accept", "application/json");

        if(requestPackage.getMethod().equals("POST")){
//            MultipartBody.Builder builder = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM);
//            Map<String, String> params = requestPackage.getParams();
//            for (String key: params.keySet()) {
//                builder.addFormDataPart(key, params.get(key));
//            }
//
//            RequestBody requestBody = builder.build();
//            requestBuilder.method("POST", requestBody);
            Map<String, String> params = requestPackage.getParams();
            FormBody.Builder formData = new FormBody.Builder();
            for (String key: params.keySet()) {
                formData.add(key, params.get(key));
            }

            RequestBody formBody = formData.build();
            requestBuilder.post(formBody);
            for (String key : requestPackage.getHeaders().keySet()) {
                requestBuilder.addHeader(key, requestPackage.getHeaders().get(key));
            }

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
            throw new IOException("Error \t" +
                    getResponseBody(response.body().string()));
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