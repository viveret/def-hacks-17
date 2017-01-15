package safemessage.viveret.com.safemessage.model;

import java.util.ArrayList;
import java.util.List;

import safemessage.viveret.com.safemessage.fb.IProfile;
import safemessage.viveret.com.safemessage.fb.ProfileFactory;
import safemessage.viveret.com.safemessage.sms.SMSData;
import safemessage.viveret.com.safemessage.sms.SMSFactory;

/**
 * Created by viveret on 1/14/17.
 */

public class AllMessageThreads implements SMSFactory.SmsFactoryUpdatesListener {
    private List<MessageThread> myThreads;
    private SMSFactory myCache;
    private ProfileFactory profileCache;

    private List<AllThreadsChangeListener> myListeners;

    public AllMessageThreads(SMSFactory allMsgs, ProfileFactory profiles) {
        myListeners = new ArrayList<AllThreadsChangeListener>();
        myThreads = new ArrayList<MessageThread>();
        myCache = allMsgs;
        profileCache = profiles;
        recalculateConversations();
        allMsgs.registerListener(this);
    }

    public List<MessageThread> getThreads() {
        return myThreads;
    }

    @Override
    public void onSmsUpdated(SMSFactory newSet) {
        myCache = newSet;
        for (SMSFactory.SmsFactoryUpdatesListener li : myThreads) {
            li.onSmsUpdated(newSet);
        }
        recalculateConversations();
        notifyListeners();
    }

    private void recalculateConversations() {
        List<String> distinctNumbers = new ArrayList<String>();
        for (SMSData msg : myCache.getData()) {
            if (msg.getNumber() != null && !distinctNumbers.contains(msg.getNumber())) {
                distinctNumbers.add(msg.getNumber());
            }
        }

        for (String number : distinctNumbers) {
            IProfile tmpProfile = profileCache.getProfile(null, number);
            MessageThread tmp = getThread(tmpProfile);
            if (tmp == null) {
                tmp = new MessageThread(myCache, tmpProfile);
                tmp.onSmsUpdated(myCache);
                myThreads.add(tmp);
            }

            if (tmp.getMessages().size() == 0) {
                myThreads.remove(tmp);
            }
        }
    }

    public MessageThread getThread(IProfile other) {
        for (MessageThread mt : myThreads) {
            if (mt.getOthers().size() > 0 && mt.getOthers().get(0).equals(other))
                return mt;
        }

        return null;
    }


    public void registerListener(AllThreadsChangeListener li) {
        myListeners.add(li);
        li.onAllThreadsChanged(this);
    }

    public void removeListener(AllThreadsChangeListener li) {
        myListeners.remove(li);
    }

    private void notifyListeners() {
        for (AllThreadsChangeListener li : myListeners) {
            li.onAllThreadsChanged(this);
        }
    }

    /**
     * Created by viveret on 1/15/17.
     */

    public interface AllThreadsChangeListener {
        void onAllThreadsChanged(AllMessageThreads cache);
    }
}
