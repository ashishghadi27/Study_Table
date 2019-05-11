package com.studlarsinc.root.studytable.BroadcastListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.studlarsinc.root.studytable.Interfaces.SmsListener;

public class SmsReceiver extends BroadcastReceiver {

  private static SmsListener mListener;
  String abcd, xyz;

  public static void bindListener(SmsListener listener) {
    mListener = listener;
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    Bundle data = intent.getExtras();
    Object[] pdus = (Object[]) data.get("pdus");
    for (int i = 0; i < pdus.length; i++) {
      SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
      String sender = smsMessage.getDisplayOriginatingAddress();
      try {
        String messageBody = smsMessage.getMessageBody();
        abcd = messageBody.replaceAll("[^0-9]", "");
        Log.v("OTP",abcd+"");
        if(abcd.length() == 6){
          mListener.messageReceived(abcd);
        }

      }catch (Exception e){
        Log.v("ERROR", "SERIOUS");
      }


    }
  }
}