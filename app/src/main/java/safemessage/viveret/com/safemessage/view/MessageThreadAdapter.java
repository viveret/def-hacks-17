package safemessage.viveret.com.safemessage.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import safemessage.viveret.com.safemessage.R;
import safemessage.viveret.com.safemessage.model.AllMessageThreads;
import safemessage.viveret.com.safemessage.model.MessageThread;
import safemessage.viveret.com.safemessage.sms.SMSFactory;

/**
 * Created by viveret on 1/14/17.
 */

public class MessageThreadAdapter extends ArrayAdapter<MessageThread> implements SMSFactory.SmsFactoryUpdatesListener {

    private AllMessageThreads myThreads;

    public MessageThreadAdapter(Context c, AllMessageThreads data) {
        super(c, R.layout.profile_item, data.getThreads());
        myThreads = data;
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
        tvHeader.setText(dta.getLastMessage().getName());
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onSmsUpdated(SMSFactory newSet) {
        notifyDataSetChanged();
    }
}
