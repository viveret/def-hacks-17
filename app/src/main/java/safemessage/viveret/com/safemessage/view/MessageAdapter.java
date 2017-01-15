package safemessage.viveret.com.safemessage.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import safemessage.viveret.com.safemessage.R;
import safemessage.viveret.com.safemessage.sms.SMSData;
import safemessage.viveret.com.safemessage.sms.SMSFactory;

/**
 * Created by viveret on 1/14/17.
 */

public class MessageAdapter extends ArrayAdapter<SMSData> implements SMSFactory.SmsFactoryUpdatesListener {

    public MessageAdapter(Context c, SMSFactory data) {
        super(c, R.layout.profile_item, data.getData());
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
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onSmsUpdated(List<SMSData> newSet) {
        notifyDataSetChanged();
    }
}
