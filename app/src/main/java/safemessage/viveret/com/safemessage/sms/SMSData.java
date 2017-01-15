package safemessage.viveret.com.safemessage.sms;

import android.util.Log;

import java.util.Comparator;
import java.util.Date;

import safemessage.viveret.com.safemessage.Config;
import safemessage.viveret.com.safemessage.fb.IProfile;
import safemessage.viveret.com.safemessage.fb.ProfileFactory;

/**
 * Created by viveret on 1/14/17.
 */

public class SMSData implements Comparable<SMSData> {
    private String myName, myNumber, myMessage;
    private Date myDate;
    private int myType, myThreadId;
    public SMSData(String theName, String theNumber, String theMessage, int theType, Date theDate, int theThreadId) {
        myName = theName;

        myNumber = theNumber.replaceAll("[^\\d.]", "");
        if (myNumber.length() == 10)
            myNumber = "1" + myNumber;


        myMessage = theMessage;
        myType = theType;
        myDate = theDate;
        myThreadId = theThreadId;

        Log.v(Config.LOGTAG, "Created new SMSData " + toString());
    }

    @Override
    public int compareTo(SMSData o) {
        return myDate.compareTo(o.getDate());
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

    public Date getDate() {
        return myDate;
    }

    public int getThreadId() {
        return myThreadId;
    }

    public IProfile getProfile(ProfileFactory profiles) {
        return profiles.getProfile(null, getNumber());
    }

    @Override
    public String toString() {
        return "[name=\"" + myName + "\", number=" + myNumber + ", body=\"" + myMessage + "\", " +
                "threadId=" + myThreadId + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SMSData) {
            return myDate.equals(((SMSData) o).getDate());
        } else {
            return false;
        }
    }

    public static class DateComparator implements Comparator<SMSData> {
        @Override
        public int compare(SMSData o1, SMSData o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    }
}
