package com.codewarriors4.tiffin;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.codewarriors4.tiffin.adapters.HomeMakerListAdapter;
import com.codewarriors4.tiffin.adapters.SubscribersListAdapter;
import com.codewarriors4.tiffin.models.SubscribersListModel;
import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class SubscribersTab_Fragment extends android.support.v4.app.Fragment
{
    private String str;
    public static final String KEY = "key_args";
    private View inflate;
    private RecyclerView recyclerView;
    private SessionUtli sessionUtli;
    private Context context;
    private String listString;
    private boolean isLoaded = false;


    public SubscribersTab_Fragment(){

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
//        this.context = (Activity) getContext();
        this.context = getContext();
        Activity act = (Activity) this.context;
        sessionUtli = SessionUtli.getSession(act.getSharedPreferences(Constants.SHAREDPREFERNCE, Activity.MODE_PRIVATE));
        super.onCreate(savedInstanceState);
        Log.d("Fragment_cycle", "onCreate: " +str);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.subscribers_tab_fragment, container, false);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.subscribers_Recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        String string = arguments.getString("Hello");

            if(!isLoaded){
                new SubscribersList().execute("");
                isLoaded = true;
            }

    }

    private void initString() {

    }

    private String getSubscribersList(){
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setEndPoint(Constants.BASE_URL + Constants.GETSUBSCRIBERS);
        requestPackage.setMethod("POST");
        requestPackage.setHeader("Authorization", "Bearer " +sessionUtli.getValue("access_token"));
        requestPackage.setHeader("Accept", "application/json; q=0.5");
        try {
            return HttpHelper.downloadFromFeed(requestPackage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



    private class SubscribersList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String homeMakerList = getSubscribersList();
                return homeMakerList;
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


        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            initValues(aVoid);
        }

    }

    private void initValues(String subscribersListStr)
    {
        try {

            JSONArray subscribersList =  new JSONArray(subscribersListStr);
            ArrayList<SubscribersListModel> lists = new ArrayList<>();
            try {
                for(int i = 0; i < subscribersList.length() ; i++){

                    JSONObject subscriber = subscribersList.getJSONObject(i);
                    String email = subscriber.getString("email");
                    String street = subscriber.getString("UserStreet");
                    String phoneNumber = subscriber.getString("UserPhone");
                    lists.add(new SubscribersListModel(email, street, phoneNumber));
                }

                SubscribersListAdapter subscribersListAdapter = new SubscribersListAdapter(context, lists);
                recyclerView.setAdapter(subscribersListAdapter);

        } catch (JSONException e) {


        }

            //homeMakerProgressList.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
