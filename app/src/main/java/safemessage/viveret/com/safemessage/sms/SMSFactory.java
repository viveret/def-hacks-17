package safemessage.viveret.com.safemessage.sms;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import safemessage.viveret.com.safemessage.Config;
import safemessage.viveret.com.safemessage.fb.TextModerate;

/**
 * Created by viveret on 1/14/17.
 */

public class SMSFactory extends BroadcastReceiver {
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    private List<SMSData> myData;
    private List<SmsFactoryUpdatesListener> myListeners;

    public SMSFactory(Context c, SmsFactoryUpdatesListener mainListener) {
        myListeners = new ArrayList<SmsFactoryUpdatesListener>();
        registerListener(mainListener);

        myData = new ArrayList<SMSData>();

        final String SMS_ALL = "content://sms/inbox";
        Uri uri = Uri.parse(SMS_ALL);

        Cursor cursor = c.getContentResolver().query(uri, null, null, null, null);

        String[] projection = new String[]{"_id", "thread_id", "address", "person", "body", "date", "type"};
        Log.v(Config.LOGTAG, "LoadFinished");
        List<String> phoneNumbers = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            Log.v(Config.LOGTAG, phoneNumber);
            int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
            if ((!phoneNumbers.contains(phoneNumber)) && (type != 3) && (phoneNumber.length() >= 1)) {
                String name = null;
                String person = cursor.getString(cursor.getColumnIndexOrThrow("person"));
                String smsContent = cursor.getString(cursor.getColumnIndexOrThrow("body"));

                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("date"))));
                Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNumber);
                ContentResolver cr = c.getContentResolver();
                Cursor localCursor = cr.query(personUri,
                        new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                        null, null, null);//use phonenumber find contact name
                if (localCursor.getCount() != 0) {
                    localCursor.moveToFirst();
                    name = localCursor.getString(localCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                }
                Log.v(Config.LOGTAG, "person:" + person + "  name:" + name + "  phoneNumber:" + phoneNumber);
                localCursor.close();
                phoneNumbers.add(phoneNumber);

                //Justin's code censors the message
                 TextModerate censoredText = new TextModerate("shit wwww.piratesbay.com www.pornhub.com bad word",c);
                //TextModerate censoredText = new TextModerate(smsContent,c);
                //smsContent = censoredText.getCensoredText();

                SMSData sms = new SMSData(name, phoneNumber, smsContent, type, date);
                myData.add(sms);
            }
        }
    }

    public List<SMSData> getData() {
        return myData;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();

                myData.add(new SMSData(sender,
                        messages[0].getDisplayOriginatingAddress(), message,
                        -1, new Date(messages[0].getTimestampMillis())));
                // prevent any other broadcast receivers from receiving broadcast
                abortBroadcast();
                notifyObservers();
            }
        }
    }

    private void notifyObservers() {
        for (SmsFactoryUpdatesListener li : myListeners) {
            li.onSmsUpdated(this);
        }
    }

    public void registerListener(SmsFactoryUpdatesListener li) {
        myListeners.add(li);
        li.onSmsUpdated(this);
    }

    public void removeListener(SmsFactoryUpdatesListener li) {
        myListeners.remove(li);
    }

    public interface SmsFactoryUpdatesListener {
        void onSmsUpdated(SMSFactory newSet);
    }
}
