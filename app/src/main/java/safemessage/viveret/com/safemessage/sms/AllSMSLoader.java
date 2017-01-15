package safemessage.viveret.com.safemessage.sms;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import safemessage.viveret.com.safemessage.Config;

/**
 * Created by viveret on 1/14/17.
 */

class AllSMSLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;

    private AllSMSLoader(Context context) {
        this.context = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        final String SMS_ALL = "content://sms/";
        Uri uri = Uri.parse(SMS_ALL);
        String[] projection = new String[]{"_id", "thread_id", "address", "person", "body", "date", "type"};
        return new CursorLoader(context, uri, projection, null, null, "date desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.v(Config.LOGTAG, "LoadFinished");
        List<SMSData> sms_All = new ArrayList<SMSData>();
        List<String> phoneNumbers = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            Log.v(Config.LOGTAG, phoneNumber);
            int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
            if ((!phoneNumbers.contains(phoneNumber)) && (type != 3) && (phoneNumber.length()>=1)) {
                String name = null;
                String person = cursor.getString(cursor.getColumnIndexOrThrow("person"));
                String smsContent = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("date"))));
                Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNumber);
                ContentResolver cr = context.getContentResolver();
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
                SMSData sms = new SMSData(name, phoneNumber, smsContent, type, date);
                sms_All.add(sms);
            }
        }
        // myadapter.notifyDataSetChanged();
        //simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        //simpleCursorAdapter.swapCursor(null);
    }
}
