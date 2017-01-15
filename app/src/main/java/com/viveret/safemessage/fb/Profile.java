package com.viveret.safemessage.fb;

import com.viveret.safemessage.sms.SMSData;

/**
 * Created by viveret on 1/14/17.
 */

public class Profile implements IProfile {
    private String myUserId, myName, myGender, myProfilePicUrl, myLocale, myNumber;
    private int myTimeZone;

    public Profile() {
        myUserId = myName = myGender = myProfilePicUrl = myLocale = myNumber = "";
        myTimeZone = 0;
    }

    @Override
    public String getUserId() {
        if (myUserId == null || myUserId.trim().length() == 0) {
            return getNumber();
        } else {
            return myUserId;
        }
    }

    public void setUserId(String v) {
        myUserId = v;
    }

    @Override
    public String getNumber() {
        return myNumber;
    }

    public void setNumber(String v) {
        myNumber = v.replaceAll("[^\\d.]", "");
        if (myNumber.length() == 10)
            myNumber = "1" + myNumber;
    }

    @Override
    public String getName() {
        if (myName == null || myName.trim().length() == 0) {
            return myNumber;
        } else {
            return myName;
        }
    }

    public void setName(String v) {
        myName = v;
    }

    @Override
    public String getGender() {
        return myGender;
    }

    public void setGender(String v) {
        myGender = v;
    }

    @Override
    public String getProfilePicURL() {
        return myProfilePicUrl;
    }

    public void setProfilePicUrl(String v) {
        myProfilePicUrl = v;
    }

    @Override
    public String getLocale() {
        return myLocale;
    }

    public void setLocale(String v) {
        myLocale = v;
    }

    @Override
    public int getTimeZone() {
        return myTimeZone;
    }

    public void setTimeZone(int v) {
        myTimeZone = v;
    }

    @Override
    public boolean sentMessage(SMSData msg) {
        String id = getNumber();
        if (id == null)
            return false;
        else
            return id.equals(msg.getNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IProfile) {
            IProfile tmp = (IProfile) o;
            return getNumber().equals(tmp.getNumber());
        } else
            return false;
    }

    @Override
    public String toString() {
        return "[name=\"" + myName + "\", number=" + myNumber + "]";
    }
}
