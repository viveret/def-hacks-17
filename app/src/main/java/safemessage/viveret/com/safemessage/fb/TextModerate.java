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


    private double adultProbability = .5;
    private double malwareProbability = .5;
    private double phishingProbability = .5;
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
        Ion.with(theContext)
                .load(cmEndPoint)

                .addHeader("Content-Type", "text/plain")
                .addHeader("Ocp-Apim-Subscription-Key", SUBSCRIPTION_KEY)


                .setStringBody(theText)
                .asJsonObject()
                .setCallback(call);

    }

    private void moderateText() {

        FutureCallback  callback = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

               Log.v(Config.LOGTAG, "Microsoft Cognitive Services contacted");
                Log.v(Config.LOGTAG, result.toString());
                moderateProfanity(result);
                moderateURL(result);
            }
        };

        reachURL(myContext, myOriginalText, callback);
    }

    private void moderateURL(JsonObject result) {
        JsonElement urls;
        JsonArray urlArray = new JsonArray();
        if (result.get("Urls")!= null){
            urls = result.get("Terms");
            if (!urls.isJsonNull()) {
                urlArray = urls.getAsJsonArray();
            }
        }

        for (int i = 0; i < urlArray.size(); i++) {
            JsonObject jsonObject = urlArray.get(i).getAsJsonObject();
            jsonObject = jsonObject.getAsJsonObject("Categories");
           // urlArray = jsonObject.getAsJsonArray();//being used for categories
           // jsonObject = urlArray.
            if (adultProbability < jsonObject.get("Adult").getAsDouble() ||
                    malwareProbability <  jsonObject.get("Malware").getAsDouble() ||
                    phishingProbability < jsonObject.get("Phishing").getAsDouble()) {
                Log.v(Config.LOGTAG, jsonObject.get("URL").getAsString());
                myCensoredText.replaceFirst(jsonObject.get("URL").getAsString(), "LINK REMOVED");
            }
        }


    }


    private void moderateProfanity(JsonObject result) {
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
        int endIndex = 0;

        for (int i = 0; i < termArray.size(); i = i + 2) {
            //May be a source of errors

            JsonObject jsonObject = termArray.get(i).getAsJsonObject();
            endIndex = jsonObject.get("Index").getAsInt();
            String profanity = jsonObject.get("Term").toString();
            String newSection = myOriginalText.substring(currentIndex, endIndex );
            sb.append(newSection);
            for (int j = 0; j <  profanity.length() - 2; j++) {
                sb.append("*");
            }

            currentIndex = endIndex + profanity.length() -2;
        }
        String newSection = myOriginalText.substring(currentIndex);
        sb.append(newSection);
        if (myInstancesOfProfanity > 0) {
            myCensoredText = sb.toString();
            Log.v(Config.LOGTAG, myCensoredText);
        }
    }

    public String getCensoredText() { return myCensoredText;}
}

