package safemessage.viveret.com.safemessage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Amy on 1/14/2017.
 */

public class HomeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

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
