package safemessage.viveret.com.safemessage.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import safemessage.viveret.com.safemessage.fb.IProfile;
import safemessage.viveret.com.safemessage.fb.ProfileFactory;
import safemessage.viveret.com.safemessage.sms.SMSData;
import safemessage.viveret.com.safemessage.sms.SMSFactory;

/**
 * Created by viveret on 1/14/17.
 */

public class MessageThread implements SMSFactory.SmsFactoryUpdatesListener {
    private ProfileFactory allProfiles;


    private List<IProfile> myOthers;
    private List<SMSData> myMessages;

    private List<MessageThreadChangedListener> myListeners;

    private int myThreadId;

    public MessageThread(SMSFactory src, ProfileFactory theProfiles, int theThreadId) {
        myOthers = new ArrayList<IProfile>();
        myMessages = new ArrayList<SMSData>();
        myListeners = new ArrayList<MessageThreadChangedListener>();

        allProfiles = theProfiles;
        myThreadId = theThreadId;
    }

    public int getThreadId() {
        return myThreadId;
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

    public IProfile getProfileFromMsg(SMSData msg) {
        for (IProfile p : myOthers) {
            if (p.sentMessage(msg)) {
                return p;
            }
        }

        return null;
    }

    public void registerListener(MessageThreadChangedListener li) {
        myListeners.add(li);
        li.onMessageThreadChanged(this);
    }

    public void removeListener(MessageThreadChangedListener li) {
        myListeners.remove(li);
    }

    @Override
    public void onSmsUpdated(SMSFactory src) {
        myMessages.clear();
        myOthers.clear();

        // Extract profiles and messages into a thread
        for (SMSData msg : src.getData()) {
            if (msg.getThreadId() == myThreadId) {
                myMessages.add(msg);
                IProfile tmp = msg.getProfile(allProfiles);
                if (!myOthers.contains(tmp)) {
                    myOthers.add(tmp);
                }
            }
        }

        Collections.sort(myMessages);

        notifyListeners();

        //Log.v(Config.LOGTAG, "Messages in thread: " + Arrays.toString(myMessages.toArray()));
    }

    private void notifyListeners() {
        for (MessageThreadChangedListener li : myListeners) {
            li.onMessageThreadChanged(this);
        }
    }

    public interface MessageThreadChangedListener {
        void onMessageThreadChanged(MessageThread mt);
    }
}
