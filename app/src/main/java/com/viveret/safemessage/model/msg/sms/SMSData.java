package com.viveret.safemessage.model.msg.sms;

import android.util.Log;

import com.viveret.safemessage.Config;
import com.viveret.safemessage.fb.IProfile;
import com.viveret.safemessage.fb.ProfileFactory;
import com.viveret.safemessage.model.msg.IMessage;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by viveret on 1/14/17.
 */

public class SMSData implements IMessage {
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
    public int compareTo(IMessage o) {
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

    @Override
    public String getTextContent() {
        return getBody();
    }

    @Override
    public String getTextTitle() {
        return null;
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
        if (o instanceof IMessage) {
            return myDate.equals(((IMessage) o).getDate());
        } else {
            return false;
        }
    }

    public static class DateComparator implements Comparator<IMessage> {
        @Override
        public int compare(IMessage o1, IMessage o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    }
}
