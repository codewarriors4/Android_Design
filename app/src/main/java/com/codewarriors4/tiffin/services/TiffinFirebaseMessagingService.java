package com.codewarriors4.tiffin.services;

import com.codewarriors4.tiffin.PushNotificationManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class TiffinFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body =remoteMessage.getNotification().getBody();

        PushNotificationManager.getmInstance(getApplicationContext()).displayNotification(title, body );


    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
