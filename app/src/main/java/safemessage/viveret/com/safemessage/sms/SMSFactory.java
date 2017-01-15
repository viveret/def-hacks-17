package safemessage.viveret.com.safemessage.sms;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viveret on 1/14/17.
 */

public class SMSFactory {
    public List<SMSData> getMessages(Context c) {
        List<SMSData> ret = new ArrayList<SMSData>();
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cur = c.getContentResolver().query(uri, null, null ,null,null);
        // c.startManagingCursor(cur);

        // Read the sms data and store it in the list
        if(cur.moveToFirst()) {
            for(int i=0; i < cur.getCount(); i++) {
                // SMSData sms = new SMSData();
                // sms.setBody(c.getString(cur.getColumnIndexOrThrow("body")).toString());
                // sms.setNumber(c.getString(cur.getColumnIndexOrThrow("address")).toString());
                // ret.add(sms);

                //  c.moveToNext();
            }
        }
        // c.close();
        return ret;
    }
}
