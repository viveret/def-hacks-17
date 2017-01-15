package safemessage.viveret.com.safemessage.fb;

import safemessage.viveret.com.safemessage.sms.SMSData;

/**
 * Created by viveret on 1/14/17.
 */

public interface IProfile {

    String getUserId();

    String getNumber();

    String getName();

    String getGender();

    String getProfilePicURL();

    String getLocale();

    int getTimeZone();

    boolean sentMessage(SMSData msg);
}
