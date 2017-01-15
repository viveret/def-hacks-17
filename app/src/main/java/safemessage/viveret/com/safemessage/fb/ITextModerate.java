package safemessage.viveret.com.safemessage.fb;

/**
 * Created by Justin of America on 1/14/2017.
 */

/**
 * Calls Microsoft Cognitive text moderator to check a string for profanity.
 * Set Boolean to true to sensor profanity or false to r
 */
public interface ITextModerate {

    String SUBSCRIPTION_KEY = "c11266dd41fe4ffea441dd2d90b67dfe";
    String cmEndPoint = "https://westus.api.cognitive.microsoft.com/contentmoderator/moderate/v1.0/ProcessText/Screen/?language=eng&autocorrect=false&urls=true";
    int PROFANITY_ERROR = 0;
    int ILLEGAL_URL_ERROR = 1;


}
