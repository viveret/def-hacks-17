package safemessage.viveret.com.safemessage.sms;

import android.util.Log;

import java.util.Date;

import safemessage.viveret.com.safemessage.Config;
import safemessage.viveret.com.safemessage.fb.IProfile;
import safemessage.viveret.com.safemessage.fb.ProfileFactory;

/**
 * Created by viveret on 1/14/17.
 */

public class SMSData {
    private String myName, myNumber, myMessage;
    private Date myDate;
    private int myType;

    public SMSData(String theName, String theNumber, String theMessage, int theType, Date theDate) {
        myName = theName;

        myNumber = theNumber.replaceAll("[^\\d.]", "");
        if (myNumber.length() == 10)
            myNumber = "1" + myNumber;


        myMessage = theMessage;
        myType = theType;
        myDate = theDate;

        Log.v(Config.LOGTAG, "Created new SMSData " + toString());
    }


    public String getName() {
        if (myName == null || myName.trim().length() == 0) {
            return myNumber;
        } else {
            return myName;
        }
    }

    public String getNumber() {
        return myNumber;
    }

    public String getBody() {
        return myMessage;
    }

    public IProfile getProfile(ProfileFactory profiles) {
        return profiles.getProfile(null, getNumber());
    }

    @Override
    public String toString() {
        return "[name=\"" + myName + "\", number=" + myNumber + ", body=\"" + myMessage + "\"]";
    }
}
