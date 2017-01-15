package safemessage.viveret.com.safemessage.sms;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
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

    private List<SMSData> myMessages;

    public SMSFactory(Context c, SmsFactoryUpdatesListener mainListener) {
        myListeners = new ArrayList<SmsFactoryUpdatesListener>();
        registerListener(mainListener);

        myData = new ArrayList<SMSData>();
        myMessages = new ArrayList<SMSData>();

        refreshCache(c);
    }

    private void refreshCache(Context c) {
        myData.clear();
        myMessages.clear();
        getSentMessages(c);
        getInboxMessages(c);
    }

    private void getSentMessages(Context c) {
        final String SMS_ALL = "content://sms/sent";
        Uri uri = Uri.parse(SMS_ALL);

        Cursor cursor = c.getContentResolver().query(uri, null, null, null, null);

        String[] projection = new String[]{"_id", "thread_id", "body", "date", "type"};
        while (cursor.moveToNext()) {
            int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
            if (type != 3) {
                String smsContent = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("date"))));
                int threadId = cursor.getInt(cursor.getColumnIndexOrThrow("thread_id"));
                SMSData sms = new SMSData("user", "user", smsContent, type, date, threadId);
                myMessages.add(sms);
            }
        }
    }

    private void getInboxMessages(Context c) {
        final String SMS_ALL = "content://sms/inbox";
        Uri uri = Uri.parse(SMS_ALL);

        Cursor cursor = c.getContentResolver().query(uri, null, null, null, null);

        String[] projection = new String[]{"_id", "thread_id", "address", "person", "body", "date", "type"};
        List<String> phoneNumbers = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
            if ((!phoneNumbers.contains(phoneNumber)) && (type != 3) && (phoneNumber.length() >= 1)) {
                String name = null;
                String person = cursor.getString(cursor.getColumnIndexOrThrow("person"));
                String smsContent = cursor.getString(cursor.getColumnIndexOrThrow("body"));

                int threadId = cursor.getInt(cursor.getColumnIndexOrThrow("thread_id"));
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
                localCursor.close();
                phoneNumbers.add(phoneNumber);

                //Justin's code censors the message
                // TextModerate censoredText = new TextModerate("shit wwww.reddit.com www.pornhub.com bad word", c);
                TextModerate censoredText = new TextModerate(smsContent, c);


                Log.v(Config.LOGTAG, "THIS IS IT" + smsContent);


                smsContent = censoredText.getCensoredText();
                SMSData sms = new SMSData(name, phoneNumber, smsContent, type, date, threadId);
                myData.add(sms);

            }
        }
    }

    public List<SMSData> getData() {
        return myData;
    }

    public List<SMSData> getDataByThreadId(int threadId) {
        List<SMSData> tmp = new ArrayList<SMSData>();
        for (SMSData d : myData) {
            if (d.getThreadId() == threadId) {
                tmp.add(d);
            }
        }
        return tmp;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            refreshCache(context);
//            Bundle bundle = intent.getExtras();
//            if (bundle != null) {
//                // get sms objects
//                Object[] pdus = (Object[]) bundle.get("pdus");
//                if (pdus.length == 0) {
//                    return;
//                }
//                // large message might be broken into many
//                SmsMessage[] messages = new SmsMessage[pdus.length];
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < pdus.length; i++) {
//                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                    sb.append(messages[i].getMessageBody());
//                }
//                String sender = messages[0].getOriginatingAddress();
//                String message = sb.toString();
//
//                myData.add(new SMSData(sender,
//                        messages[0].getDisplayOriginatingAddress(), message,
//                        -1, new Date(messages[0].getTimestampMillis()), ""));
                // prevent any other broadcast receivers from receiving broadcast
                abortBroadcast();
            notifyListeners();
            //}
        }
    }

    private void notifyListeners() {
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
