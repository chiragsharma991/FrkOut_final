package com.codeginger.frkout;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.app.Notification;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.HashMap;

// Created by Pratik Mehta

public class MSGService extends IntentService
{
    NotificationCompat.Builder notification;
    NotificationManager manager;

    String GCM_CURRENT_ACTIVE;
    UserSessionManager usm;

    public MSGService()
    {
        super("MSGService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        usm = new UserSessionManager(getApplication());
        HashMap<String, String> app =  usm.getUserDetails();
        GCM_CURRENT_ACTIVE = app.get(usm.KEY_GCM_CURRENT_ACTIVE);

        if (!extras.isEmpty())
        {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                Log.e("L2C","Error");
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
            {
                Log.e("L2C","Error");
            }
            else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                if(extras.getString("mode").equals("chat"))
                {
                    if(GCM_CURRENT_ACTIVE != null)
                    {
                        if(!GCM_CURRENT_ACTIVE.equals(extras.getString("fromu")))
                        {
                            sendChatNotification(extras.getString("msg"), extras.getString("fromu"), extras.getString("name"), extras.getString("imgName"));
                        }
                        Log.i("TAG", "Received: " + extras.getString("msg"));
                    }
                }
                else if(extras.getString("mode").equals("broadcast"))
                {
                    sendBrodcastNotification(extras.getString("title"), extras.getString("description"), extras.getString("type"), extras.getString("filename"));
                }

            }
        }
        MSGReceiver.completeWakefulIntent(intent);
    }

    private void sendChatNotification(String msg, String UserId, String name, String imgName)
    {
        Bundle args = new Bundle();
        args.putString("UserId", UserId);
        args.putString("name", name);
        args.putString("msg", msg);
        args.putString("imgName", imgName);
        Intent chat = new Intent(this, ChatActivity.class);
        chat.putExtra("INFO", args);
        notification = new NotificationCompat.Builder(this);
        notification.setContentTitle(name);
        notification.setContentText(msg);
        notification.setTicker("New Message !");
        notification.setSmallIcon(R.mipmap.ic_launcher1);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1000, chat, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.setContentIntent(contentIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        notification.setDefaults(defaults);
        notification.setAutoCancel(true);
        manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }


    private void sendBrodcastNotification(String title, String description, String type, String filename)
    {
        Intent ibroadcast = new Intent(this, BroadcastActivityDisplay.class);
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putString("type", type);
        args.putString("filename", filename);
        ibroadcast.putExtra("BROADCAST", args);

        notification = new NotificationCompat.Builder(this);
        notification.setContentTitle(title);
        notification.setContentText(description);
        notification.setTicker("New Message !");
        notification.setSmallIcon(R.mipmap.ic_launcher1);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1000, ibroadcast, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.setContentIntent(contentIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;
        notification.setDefaults(defaults);
        notification.setAutoCancel(true);
        manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }



}