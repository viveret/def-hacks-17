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

    public static HashMap<String, List<String>> getInfo() {
        HashMap<String, List<String>> listDetails = new HashMap<String, List<String>>();
        //Each list being dispayed in navigation Drawer.
        List<String> BlockedList = new ArrayList<String>();
        BlockedList.add("Jane");
        BlockedList.add("Jake");

        listDetails.put("Blocked List", BlockedList);

        return listDetails;
    }

}
