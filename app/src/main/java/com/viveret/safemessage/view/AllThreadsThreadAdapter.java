package com.viveret.safemessage.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.viveret.safemessage.R;
import com.viveret.safemessage.fb.ProfileFactory;
import com.viveret.safemessage.model.msg.AllMessageThreads;
import com.viveret.safemessage.model.msg.MessageThread;


/**
 * Created by viveret on 1/14/17.
 */

public class AllThreadsThreadAdapter extends ArrayAdapter<MessageThread> implements AllMessageThreads.AllThreadsChangeListener {

    private AllMessageThreads myThreads;
    private ProfileFactory allProfiles;

    public AllThreadsThreadAdapter(Context c, AllMessageThreads data, ProfileFactory theProfiles) {
        super(c, R.layout.profile_item, data.getThreads());
        allProfiles = theProfiles;
        myThreads = data;
        myThreads.registerListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageThread dta = getItem(position);


        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_item, parent, false);
        }
        // Lookup view for data population
        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.avatar);
        TextView tvHeader = (TextView) convertView.findViewById(R.id.header);
        TextView tvBody = (TextView) convertView.findViewById(R.id.body);
        // Populate the data into the template view using the data object
        tvBody.setText(dta.getLastMessage().getBody());
        tvHeader.setText(dta.getLastMessage().getProfile(allProfiles).getName());

        String avatarUri = dta.getProfileFromMsg(dta.getLastMessage()).getProfilePicURL();
        if (avatarUri != null && avatarUri.trim().length() > 0) {
            ivAvatar.setImageURI(null);
            ivAvatar.setImageURI(Uri.parse(avatarUri));
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onAllThreadsChanged(AllMessageThreads cache) {
        myThreads = cache;
        notifyDataSetChanged();
    }
}
