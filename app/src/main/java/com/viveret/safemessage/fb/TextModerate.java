package com.viveret.safemessage.fb;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.viveret.safemessage.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Created by Justin of America on 1/14/2017.
 */

public class TextModerate implements ITextModerate {

    private double sentimentValue = .1;
    private double adultProbability = .5;
    private double malwareProbability = .5;
    private double phishingProbability = .5;
    private String myOriginalText;
    private String myCensoredText;
    private int myInstancesOfProfanity;
    private Context myContext;
    private CountDownLatch myLatch;

    public TextModerate(String theString, Context theContext) {
        myOriginalText = theString;
        myCensoredText = myOriginalText;
        myContext = theContext;

        moderateText();
    }

    public void reachURL(Context theContext, String theText) {


        try {
            JsonObject tmp = Ion.with(theContext)
                    .load(cmEndPoint)

                    .addHeader("Content-Type", "text/plain")
                    .addHeader("Ocp-Apim-Subscription-Key", SUBSCRIPTION_KEY)


                    .setStringBody(theText)
                    .asJsonObject().get();
            Log.v(Config.LOGTAG, "Microsoft Cognitive Services contacted");
            Log.v(Config.LOGTAG, tmp.toString());
            moderateProfanity(tmp);
            moderateURL(tmp);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            JsonObject tmp = Ion.with(theContext)
                    .load(cmtxtanalyticsEndPoint)

                    .addHeader("Content-Type", "text/json")
                    .addHeader("Ocp-Apim-Subscription-Key", SUBSCRIPTION_KEY_TEXT_ANALYTICS)


                    .setStringBody("{\"documents\": [ {\"language\": \"en\",\"id\": \"string\", \"text\": \"" + theText + "\"}]}")
                    .asJsonObject().get();
            Log.v(Config.LOGTAG, "Microsoft text analytic Services contacted");
            Log.v(Config.LOGTAG, tmp.toString());
            moderateSentiment(tmp);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    private void moderateSentiment(JsonObject result) {
        JsonElement doccuments;
        JsonArray docArray = new JsonArray();
        if (result.get("documents") != null) {
            doccuments = result.get("documents");
            if (!doccuments.isJsonNull()) {
                docArray = doccuments.getAsJsonArray();
            }
        }
        for (int i = 0; i < docArray.size(); i++) {
            JsonObject jsonObject = docArray.get(i).getAsJsonObject();
            if (sentimentValue > jsonObject.get("score").getAsDouble()) {
                Log.v(Config.LOGTAG, jsonObject.get("score") + "");
                myCensoredText = "MESSAGE BLOCKED";
            }
        }
    }

    private void moderateText() {
        reachURL(myContext, myOriginalText);
    }

    private void moderateURL(JsonObject result) {
        JsonElement urls;
        JsonArray urlArray = new JsonArray();
        if (result.get("Urls") != null) {
            urls = result.get("Urls");
            if (!urls.isJsonNull()) {
                urlArray = urls.getAsJsonArray();
                Log.v(Config.LOGTAG, urlArray.size() + "");
            }
        }

        for (int i = 0; i < urlArray.size(); i++) {
            JsonObject jsonObject = urlArray.get(i).getAsJsonObject();
            JsonObject jsonCategory = jsonObject.getAsJsonObject("Categories");
            //Log.v(Config.LOGTAG, jsonCategory.get("Adult").getAsDouble() + "");
            if (adultProbability < jsonCategory.get("Adult").getAsDouble() ||
                    malwareProbability < jsonCategory.get("Malware").getAsDouble() ||
                    phishingProbability < jsonCategory.get("Phishing").getAsDouble()) {
                String url = jsonObject.get("URL").toString();
                url = url.replaceAll("\"", "");
                myCensoredText = myCensoredText.replaceFirst(url, "LINK REMOVED");
                Log.v(Config.LOGTAG, myCensoredText);
            }
        }


    }


    private void moderateProfanity(JsonObject result) {
        JsonElement terms;
        JsonArray termArray = new JsonArray();
        if (result.get("Terms") != null) {
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
            String newSection = myOriginalText.substring(currentIndex, endIndex);
            sb.append(newSection);
            for (int j = 0; j < profanity.length() - 2; j++) {
                sb.append("*");
            }

            currentIndex = endIndex + profanity.length() - 2;
        }
        String newSection = myOriginalText.substring(currentIndex);
        sb.append(newSection);
        if (myInstancesOfProfanity > 0) {
            myCensoredText = sb.toString();
            Log.v(Config.LOGTAG, myCensoredText);
        }
    }

    public String getCensoredText() {
        return myCensoredText;
    }
}

