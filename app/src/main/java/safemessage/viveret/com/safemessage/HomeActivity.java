package safemessage.viveret.com.safemessage;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import safemessage.viveret.com.safemessage.view.HomeFragment;

/**
 * Created by Amy on 1/14/2017.
 */

public class HomeActivity extends FragmentActivity
        implements HomeFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Fragment f = new HomeFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.content_frame, f);
        trans.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
