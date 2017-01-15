package safemessage.viveret.com.safemessage.model;

import java.util.ArrayList;
import java.util.List;

import safemessage.viveret.com.safemessage.fb.IProfile;
import safemessage.viveret.com.safemessage.sms.SMSData;
import safemessage.viveret.com.safemessage.sms.SMSFactory;

/**
 * Created by viveret on 1/14/17.
 */

public class MessageThread {
    private List<IProfile> myOthers;
    private List<SMSData> myMessages;

    public MessageThread(SMSFactory src, IProfile other) {
        myOthers = new ArrayList<IProfile>();
        myMessages = new ArrayList<SMSData>();

        myOthers.add(other);

        // Extract profiles and messages into a thread
        for (SMSData msg : src.getData()) {
            if (other.sentMessage(msg)) {
                myMessages.add(msg);
            }
        }
    }

    public List<IProfile> getOthers() {
        return myOthers;
    }

    public List<SMSData> getMessages() {
        return myMessages;
    }

    public SMSData getLastMessage() {
        if (myMessages.size() > 0) {
            return myMessages.get(myMessages.size() - 1);
        } else {
            return null;
        }
    }
}
