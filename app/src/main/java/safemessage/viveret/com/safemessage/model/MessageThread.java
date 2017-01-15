package safemessage.viveret.com.safemessage.model;

import java.util.ArrayList;
import java.util.List;

import safemessage.viveret.com.safemessage.fb.IProfile;
import safemessage.viveret.com.safemessage.sms.SMSData;
import safemessage.viveret.com.safemessage.sms.SMSFactory;

/**
 * Created by viveret on 1/14/17.
 */

public class MessageThread implements SMSFactory.SmsFactoryUpdatesListener {
    private List<IProfile> myOthers;
    private List<SMSData> myMessages;

    private List<MessageThreadChangedListener> myListeners;

    public MessageThread(SMSFactory src, IProfile other) {
        myOthers = new ArrayList<IProfile>();
        myMessages = new ArrayList<SMSData>();
        myListeners = new ArrayList<MessageThreadChangedListener>();

        myOthers.add(other);
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
        IProfile other = (myOthers.size() > 0 ? myOthers.get(0) : null);
        // Extract profiles and messages into a thread
        if (other != null) {
            for (SMSData msg : src.getData()) {
                if (other.sentMessage(msg)) {
                    myMessages.add(msg);
                }
            }
        }

        notifyListeners();
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
