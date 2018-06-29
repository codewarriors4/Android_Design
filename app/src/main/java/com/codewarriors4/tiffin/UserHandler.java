package com.codewarriors4.tiffin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.codewarriors4.tiffin.utils.Constants;
import com.codewarriors4.tiffin.utils.HttpHelper;
import com.codewarriors4.tiffin.utils.RequestPackage;
import com.codewarriors4.tiffin.utils.SessionUtli;

public class UserHandler
{
    private SessionUtli session;
    private Context context;
    public void startActivity(SessionUtli session, Context context){
        this.session = session;
        this.context = context;
        new getInformationTask().execute(this.session.getValue("access_token"));


    }

    private class getInformationTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(Constants.BASE_URL + Constants.USER);
            requestPackage.setHeader("Authorization", "Bearer " + strings[0]);
            requestPackage.setHeader("Accept", "application/json; q=0.5");
            requestPackage.setMethod("POST");
            try {
                String s = HttpHelper.downloadFromFeed(requestPackage);
                return s;

            } catch (Exception e) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            session.setValues(s);
            if(session.getValue("UserType").equals("0.0")){

                Intent demoIntent = new Intent(context, TiffinSeekerDashboardActivity.class);
                demoIntent.putExtra("isNewLogin", true);
                context.startActivity(demoIntent);
                ((Activity) context).finish();
            }

            else if(session.getValue("UserType").equals("1.0")){

            }

            else {

            }
        }
    }
}
