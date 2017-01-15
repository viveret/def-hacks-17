package safemessage.viveret.com.safemessage.fb;

/**
 * Created by viveret on 1/14/17.
 */

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import safemessage.viveret.com.safemessage.Config;

public class ProfileFactory {
    private static final String fbEndPoint = "https://graph.facebook.com/v2.6/<USER_ID>?access_token=PAGE_ACCESS_TOKEN";

    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME

            };

    private Context context;

    private Map<String, IProfile> myProfileCache;

    private String myUserId;

    public ProfileFactory(Context theContext) {
        context = theContext;
        myProfileCache = new HashMap<String, IProfile>();
    }


    public IProfile getUsersProfile() {
        return null;
    }

    public IProfile getProfile(final String uid) {
        IProfile ret = null;

        if (myProfileCache.containsKey(uid))
            ret = myProfileCache.get(uid);
        else {
            ret = getProfileFromContacts(uid);

            if (ret == null) {
                Log.d(Config.LOGTAG, "Creating profile for " + uid);
                Profile tmp = new Profile();
                tmp.setUserId(uid);
                ret = tmp;
                myProfileCache.put(uid, tmp);
            }
//            HttpURLConnection urlConnection = null;
//            try {
//                URL url = new URL(fbEndPoint);
//                urlConnection = (HttpURLConnection) url.openConnection();
//
//                urlConnection.setRequestMethod("GET");
//                urlConnection.setRequestProperty("Accept", "application/json");
//
//
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                ret = JSON_ToProfile(in);
//            } catch (MalformedURLException e) {
//                Log.e(Config.LOGTAG, e.toString());
//            } catch (ProtocolException e) {
//                Log.e(Config.LOGTAG, e.toString());
//            } catch (IOException e) {
//                Log.e(Config.LOGTAG, e.toString());
//            } finally {
//                urlConnection.disconnect();
//            }
        }
        return ret;
    }

    private IProfile JSON_ToProfile(final InputStream in) {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.e(Config.LOGTAG, e.toString());
            return null;
        }

        Profile p = new Profile();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if ("name".equals(name)) {
                    p.setName(reader.nextString());
                } else if ("profile_pic".equals(name)) {
                    p.setProfilePicUrl(reader.nextString());
                } else if ("locale".equals(name)) {
                    p.setLocale(reader.nextString());
                } else if ("timezone".equals(name)) {
                    p.setTimeZone(reader.nextInt());
                } else if ("gender".equals(name)) {
                    p.setGender(reader.nextString());
                }
            }
        } catch (IOException e) {
            Log.e(Config.LOGTAG, e.toString());
            return null;
        }

        return p;
    }

    public boolean contains(String uid) {
        return myProfileCache.containsKey(uid);
    }

    private IProfile getProfileFromContacts(String number) {
        boolean foundContact = false;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = number;
        String contactId = name;
        String avatarURI = null;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[]{
                BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI
        }, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
                number = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.PhoneLookup.NUMBER));
                avatarURI = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI));

                foundContact = true;
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        if (foundContact) {
            Profile ret = new Profile();
            ret.setName(name);
            ret.setUserId(contactId);
            ret.setProfilePicUrl(avatarURI);
            ret.setNumber(number);

            return ret;
        } else {
            return null;
        }
    }
}
