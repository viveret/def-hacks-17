package safemessage.viveret.com.safemessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Amy on 1/14/2017.
 */
/*
This class populates the navigation drawer.
 */
public class DataProvider {

    private static List<String> blockedPeople = new ArrayList<String>();

    public static void setBlockedPeople(List<String> theBlockedPeople) {
        //Deep copy
        for (String people : theBlockedPeople) {
            blockedPeople.add(people);
        }
    }

    public static HashMap<String, List<String>> getInfo() {
        HashMap<String, List<String>> listDetails = new HashMap<String, List<String>>();

        listDetails.put("Blocked List", blockedPeople);

        List<String> buttons = new ArrayList<String>();
        buttons.add("Customize");
        //buttons.add("Moderate");
        //buttons.add("Strict");
        listDetails.put("Safe Message", buttons);

        List<String> about = new ArrayList<String>();
        about.add("Written by:" + "\n" +
                "Viveret Steele, Justin Washburn, and Amy Irving." + "\n" +
                "January 15th, 2017");
        listDetails.put("About", about);

        return listDetails;
    }

}
