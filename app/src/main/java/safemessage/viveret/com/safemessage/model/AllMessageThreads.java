package safemessage.viveret.com.safemessage.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import safemessage.viveret.com.safemessage.Config;
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
        List<Integer> difThreads = new ArrayList<Integer>();
        for (SMSData msg : myCache.getData()) {
            if (!difThreads.contains(new Integer(msg.getThreadId()))) {
                difThreads.add(new Integer(msg.getThreadId()));
            }
        }

        for (Integer id : difThreads) {
            // IProfile tmpProfile = profileCache.getProfile(null, number);
            MessageThread tmp = getThread(id.intValue());
            if (tmp == null) {
                tmp = new MessageThread(myCache, profileCache, id.intValue());
                tmp.onSmsUpdated(myCache);
                myThreads.add(tmp);
                Log.v(Config.LOGTAG, "Adding thread " + id);
            }

            if (tmp.getMessages().size() == 0) {
                Log.v(Config.LOGTAG, "Removing thread " + id);
                myThreads.remove(tmp);
            }
        }
    }

    public MessageThread getThread(int id) {
        for (MessageThread mt : myThreads) {
            if (mt.getThreadId() == id)
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
