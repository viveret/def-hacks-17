package safemessage.viveret.com.safemessage.fb;

/**
 * Created by viveret on 1/14/17.
 */

import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import safemessage.viveret.com.safemessage.Config;

public class ProfileFactory {
    private static final String fbEndPoint = "https://graph.facebook.com/v2.6/<USER_ID>?access_token=PAGE_ACCESS_TOKEN";

    private Map<String, IProfile> myProfileCache;

    private String myUserId;


    public IProfile getUsersProfile() {
        return null;
    }

    public IProfile getProfile(final String uid) {
        IProfile ret = null;

        if (myProfileCache.containsKey(uid))
            ret = myProfileCache.get(uid);
        else {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(fbEndPoint);
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");


                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                ret = JSON_ToProfile(in);
            } catch (MalformedURLException e) {
                Log.e(Config.LOGTAG, e.toString());
            } catch (ProtocolException e) {
                Log.e(Config.LOGTAG, e.toString());
            } catch (IOException e) {
                Log.e(Config.LOGTAG, e.toString());
            } finally {
                urlConnection.disconnect();
            }
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
}
