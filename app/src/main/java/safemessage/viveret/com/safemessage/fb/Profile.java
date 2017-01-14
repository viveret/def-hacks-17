package safemessage.viveret.com.safemessage.fb;

/**
 * Created by viveret on 1/14/17.
 */

public class Profile implements IProfile {
    private String myUserId, myFirstName, myLastName, myGender, myProfilePicUrl, myLocale;
    private int myTimeZone;

    public Profile() {
        myUserId = myFirstName = myLastName = myGender = myProfilePicUrl = myLocale = "";
        myTimeZone = 0;
    }

    @Override
    public String getUserId() {
        return "user id";
    }

    @Override
    public String getFirstName() {
        return "first name";
    }

    @Override
    public String getLastName() {
        return "last name";
    }

    @Override
    public String getWholeName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public String getGender() {
        return "gender";
    }

    @Override
    public String getProfilePicURL() {
        return "profile pic url";
    }

    @Override
    public String getLocale() {
        return "locale";
    }

    @Override
    public int getTimeZone() {
        return 0;
    }
}
