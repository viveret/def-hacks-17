package com.viveret.safemessage.fb;

import com.viveret.safemessage.model.msg.sms.SMSData;

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
