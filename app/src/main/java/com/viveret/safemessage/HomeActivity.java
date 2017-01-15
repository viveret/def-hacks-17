package com.viveret.safemessage;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.viveret.safemessage.fb.ProfileFactory;
import com.viveret.safemessage.model.AllMessageThreads;
import com.viveret.safemessage.model.MessageThread;
import com.viveret.safemessage.sms.SMSFactory;
import com.viveret.safemessage.view.ConversationFragment;
import com.viveret.safemessage.view.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Amy on 1/14/2017.
 */

public class HomeActivity extends Activity
        implements HomeFragment.OnFragmentInteractionListener, SMSFactory.SmsFactoryUpdatesListener,
        ConversationFragment.OnFragmentInteractionListener {

    private static final int SMS_LOADER_ID = 1;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private SMSFactory allSms;
    private ProfileFactory allProfiles;

    private Fragment myFrag, homeFrag, convFrag;

    private HashMap<String, List<String>> expandableList;
    private List<String> subList;
    private ExpandableListView expList;
    private ExpListAdapter adapter;
    private AllMessageThreads allThreads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        allSms = new SMSFactory(this, this);
        allProfiles = new ProfileFactory(this);
        allThreads = new AllMessageThreads(allSms, allProfiles);
        registerReceiver(allSms, new IntentFilter(SMSFactory.SMS_RECEIVED));

        //Initialize expList
        expList = (ExpandableListView) findViewById(R.id.expandable_list);

        //This is where we populate the blocked people list.
        List<String> blockedPeople = new ArrayList<String>();
        blockedPeople.add("Jessica");
        blockedPeople.add("Joker");
        //set who is blocked.
        DataProvider.setBlockedPeople(blockedPeople);
        //Make adapter to add the list.
        expandableList = DataProvider.getInfo();
        subList = new ArrayList<String>(expandableList.keySet());
        adapter = new ExpListAdapter(this, expandableList, subList);
        expList.setAdapter(adapter);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.mipmap.ic_launcher,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        myFrag = homeFrag = HomeFragment.newInstance(allThreads, allProfiles);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.content_frame, myFrag);
        trans.commit();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSmsUpdated(SMSFactory newSet) {

    }

    @Override
    public void onSelectMessageThread(MessageThread mt) {
        myFrag = convFrag = ConversationFragment.newInstance(mt);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.content_frame, myFrag);
        trans.commit();

        Log.v(Config.LOGTAG, "Changed to message thread " + mt.getLastMessage().getName());
    }

    @Override
    public void onReturn() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.content_frame, myFrag = homeFrag);
        trans.commit();
    }

    @Override
    public void onBackPressed() {
        if (myFrag != homeFrag) {
            onReturn();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(allSms);
    }
}
