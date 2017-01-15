package safemessage.viveret.com.safemessage.model;

import java.util.ArrayList;
import java.util.List;

import safemessage.viveret.com.safemessage.fb.ProfileFactory;
import safemessage.viveret.com.safemessage.sms.SMSData;
import safemessage.viveret.com.safemessage.sms.SMSFactory;

/**
 * Created by viveret on 1/14/17.
 */

public class AllMessageThreads {
    private List<MessageThread> myThreads;

    public AllMessageThreads(SMSFactory allMsgs, ProfileFactory profiles) {
        List<String> distinctNames = new ArrayList<String>();
        for (SMSData msg : allMsgs.getData()) {
            if (msg.getName() != null && !distinctNames.contains(msg.getName())) {
                distinctNames.add(msg.getName());
            }
        }

        myThreads = new ArrayList<MessageThread>();
        for (String name : distinctNames) {
            MessageThread tmp = new MessageThread(allMsgs, profiles.getProfile(name));
            if (tmp.getMessages().size() > 0) {
                myThreads.add(tmp);
            }
        }
    }

    public List<MessageThread> getThreads() {
        return myThreads;
    }
}
