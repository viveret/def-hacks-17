package safemessage.viveret.com.safemessage.fb;

/**
 * Created by viveret on 1/14/17.
 */

public class fbContext {
    private String myAccessToken;
    private String myUserId;

    public fbContext(String theAccessToken, String theUserId) {
        myAccessToken = theAccessToken;
        myUserId = theUserId;
    }

    public String getAccessToken() {
        return myAccessToken;
    }

    public String getUserId() {
        return myUserId;
    }
}
