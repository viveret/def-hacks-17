package safemessage.viveret.com.safemessage.fb;

/**
 * Created by viveret on 1/14/17.
 */

import android.util.JsonReader;

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
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }

        Profile p = new Profile();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if ("first_name".equals(name)) {
                    // p.setName()
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
