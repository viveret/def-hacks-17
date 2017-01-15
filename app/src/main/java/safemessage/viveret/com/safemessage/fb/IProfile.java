package safemessage.viveret.com.safemessage.fb;

/**
 * Created by viveret on 1/14/17.
 */

public interface IProfile {

    String getUserId();

    String getName();

    String getGender();

    String getProfilePicURL();

    String getLocale();

    int getTimeZone();
}
