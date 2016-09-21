package com.codeginger.frkout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsBroadcastReceiver extends BroadcastReceiver
{
    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent)
    {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null)
        {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";

            for (int i = 0; i < sms.length; ++i)
            {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                if(smsMessage.getOriginatingAddress().equals("IM-MYTEXT"))
                {
                    String smsBody = smsMessage.getMessageBody().toString();
                    String[] separated = smsBody.split("Use ");
                    smsMessageStr = separated[1].substring(0,4);
                    OTPActivity inst = OTPActivity.instance();
                    inst.fillOTP(Integer.parseInt(smsMessageStr));
                }
            }

        }
    }
}