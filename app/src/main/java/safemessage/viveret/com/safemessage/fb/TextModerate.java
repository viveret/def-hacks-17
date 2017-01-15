package safemessage.viveret.com.safemessage.fb;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import safemessage.viveret.com.safemessage.Config;

/**
 * Created by Justin of America on 1/14/2017.
 */

public class TextModerate implements ITextModerate {



    private String myOriginalText;
    private String myCensoredText;
    private int myInstancesOfProfanity;
    private Context myContext;

    public TextModerate(String theString, Context theContext){
        myOriginalText = theString;
        myCensoredText = myOriginalText;
        myContext = theContext;

        moderateText();
    }

    public void reachURL(Context theContext, String theText, FutureCallback<JsonObject> call ) {
        final JsonObject json = new JsonObject();

        Ion.with(theContext)
                .load(cmEndPoint)
                //.setJsonObjectBody(json)
                .addHeader("Content-Type", "text/plain")
                .addHeader("Ocp-Apim-Subscription-Key", SUBSCRIPTION_KEY)
                .setBodyParameter("urls", "true")
                //.setBodyParameter("urls","true")



                //.setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(call);

    }

    private void moderateText() {

        FutureCallback  callback = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

               Log.v(Config.LOGTAG, "Microsoft Cognitive Services contacted");
                Log.v(Config.LOGTAG, result.toString());
                JsonElement terms;
                JsonArray termArray = new JsonArray();
                if (result.get("Terms")!= null){
                    terms = result.get("Terms");
                    if (!terms.isJsonNull()) {
                        termArray = terms.getAsJsonArray();
                    }
                }
                 myInstancesOfProfanity = termArray.size() / 2; //Gives the actual number of profane words
                Log.v(Config.LOGTAG, String.valueOf(myInstancesOfProfanity));
                StringBuilder sb = new StringBuilder();
                int currentIndex = 0;
                for (int i = 0; i < termArray.size(); i = i + 2) {
                    //May be a source of errors
                    JsonObject jsonObject = termArray.get(i).getAsJsonObject();
                    int endIndex = jsonObject.get("Index").getAsInt();
                    String profanity = jsonObject.get("Term").toString();
                    String newSection = myOriginalText.substring(currentIndex, endIndex);
                    sb.append(newSection);
                    currentIndex = currentIndex + profanity.length() - 1;
                }
                String newSection = myOriginalText.substring(currentIndex);
                sb.append(newSection);
                myCensoredText = sb.toString();
                if (myInstancesOfProfanity > 0) {

                    Log.v(Config.LOGTAG, myCensoredText);
                }
            }
        };

        reachURL(myContext, myOriginalText, callback);
    }

    public String getCensoredText() { return myCensoredText;}
}

