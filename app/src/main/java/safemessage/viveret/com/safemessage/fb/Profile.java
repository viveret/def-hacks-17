package safemessage.viveret.com.safemessage.fb;

/**
 * Created by viveret on 1/14/17.
 */

public class Profile implements IProfile {
    private String myUserId, myName, myGender, myProfilePicUrl, myLocale;
    private int myTimeZone;

    public Profile() {
        myUserId = myName = myGender = myProfilePicUrl = myLocale = "";
        myTimeZone = 0;
    }

    @Override
    public String getUserId() {
        return "user id";
    }

    public void setUserId(String v) {
        myUserId = v;
    }

    @Override
    public String getName() {
        return "first name";
    }

    public void setName(String v) {
        myName = v;
    }

    @Override
    public String getGender() {
        return "gender";
    }

    public void setGender(String v) {
        myGender = v;
    }

    @Override
    public String getProfilePicURL() {
        return "profile pic url";
    }

    public void setProfilePicUrl(String v) {
        myProfilePicUrl = v;
    }

    @Override
    public String getLocale() {
        return "locale";
    }

    public void setLocale(String v) {
        myLocale = v;
    }

    @Override
    public int getTimeZone() {
        return 0;
    }

    public void setTimeZone(int v) {
        myTimeZone = v;
    }
}
