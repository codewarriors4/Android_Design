package com.codewarriors4.tiffin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.codewarriors4.tiffin.utils.Constants;

public class PushNotificationManager {


    private Context mCtx;
    private static PushNotificationManager mInstance;


    private PushNotificationManager (Context context){
        mCtx = context;
    }


    public static synchronized PushNotificationManager getmInstance(Context context){
        if(mInstance == null){
            mInstance = new PushNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID).setSmallIcon(R.drawable.ic_app_ico).setContentTitle(title).setContentText(body);

        Intent intent = new Intent(mCtx, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotifactionManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if(mNotifactionManager!= null){
            mNotifactionManager.notify(1,mBuilder.build());
        }


    }
}
