package safemessage.viveret.com.safemessage.sms;

import java.util.Date;

/**
 * Created by viveret on 1/14/17.
 */

public class SMSData {
    private String myName, myNumber, myMessage;
    private Date myDate;
    private int myType;

    public SMSData(String theName, String theNumber, String theMessage, int theType, Date theDate) {
        myName = theName;
        myNumber = theNumber;
        myMessage = theMessage;
        myType = theType;
        myDate = theDate;

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
}
