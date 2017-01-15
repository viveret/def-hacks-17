package safemessage.viveret.com.safemessage.view;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import safemessage.viveret.com.safemessage.R;

/**
 * Created by Amy on 1/14/2017.
 */

public class HomeActivity extends AppCompatActivity {

    /*
    This method expands and hides the "block list" drawer located in fragment_home.
     */
    public void showDrawer() {
        ListView myListView = (ListView) findViewById(R.id.left_drawer);
        if (myListView.isShown()) {
            myListView.setVisibility(View.INVISIBLE);
            return;
        }
        myListView.setVisibility(View.VISIBLE);
    }


}
