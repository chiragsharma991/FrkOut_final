package com.codeginger.frkout;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;

// Created by Pratik Mehta

public class MSGReceiver  extends WakefulBroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle extras = intent.getExtras();
        if(extras != null)
        {
            if (extras.getString("mode").equals("chat"))
            {
                Intent msgrcv = new Intent("Msg");
                msgrcv.putExtra("msg", extras.getString("msg"));
                msgrcv.putExtra("mode", extras.getString("mode"));
                msgrcv.putExtra("fromu", extras.getString("fromu"));
                msgrcv.putExtra("fromname", extras.getString("name"));
                msgrcv.putExtra("imgName", extras.getString("imgName"));
                LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
            }
            else if (extras.getString("mode").equals("broadcast"))
            {
                Intent msgrcv = new Intent("Broadcast");
                msgrcv.putExtra("msg", extras.getString("msg"));
                msgrcv.putExtra("mode", extras.getString("mode"));
                msgrcv.putExtra("title", extras.getString("title"));
                msgrcv.putExtra("description", extras.getString("description"));
                msgrcv.putExtra("type", extras.getString("type"));
                msgrcv.putExtra("filename", extras.getString("filename"));
            }
        }
        ComponentName comp = new ComponentName(context.getPackageName(),MSGService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}