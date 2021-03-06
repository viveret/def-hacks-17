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
import com.viveret.safemessage.model.MessageThread;
import com.viveret.safemessage.sms.SMSData;

/**
 * Created by viveret on 1/14/17.
 */

public class ConversationAdapter extends ArrayAdapter<SMSData> implements MessageThread.MessageThreadChangedListener {

    private MessageThread mt;

    public ConversationAdapter(Context c, MessageThread mt) {
        super(c, R.layout.profile_item, mt.getMessages());
        mt.registerListener(this);
        this.mt = mt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SMSData dta = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_item, parent, false);
        }
        // Lookup view for data population
        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.avatar);
        TextView tvHeader = (TextView) convertView.findViewById(R.id.header);
        TextView tvBody = (TextView) convertView.findViewById(R.id.body);
        // Populate the data into the template view using the data object
        tvBody.setText(dta.getBody());
        tvHeader.setText(dta.getName());

        String avatarUri = mt.getProfileFromMsg(dta).getProfilePicURL();
        if (avatarUri != null && avatarUri.trim().length() > 0) {
            ivAvatar.setImageURI(null);
            ivAvatar.setImageURI(Uri.parse(avatarUri));
        }
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onMessageThreadChanged(MessageThread mt) {
        notifyDataSetChanged();
    }
}
