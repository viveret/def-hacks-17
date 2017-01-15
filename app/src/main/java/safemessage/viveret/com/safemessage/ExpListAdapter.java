package safemessage.viveret.com.safemessage;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Amy on 1/14/2017.
 */

public class ExpListAdapter extends BaseExpandableListAdapter {
    private static int nextId = 1;
    private Context ctx;
    private HashMap<String, List<String>> listCategory;
    private List<String> list;

    public ExpListAdapter(Context ctx, HashMap<String, List<String>> theListCategory, List<String> theList) {
        this.ctx = ctx;
        this.listCategory = theListCategory;
        this.list = theList;

    }

    @Override
    public Object getChild(int parent, int child) {
        return listCategory.get(list.get(parent)).get(child);
    }

    @Override
    public long getChildId(int parent, int child) {
        // TODO Auto-generated method stub
        return child;
    }

    @Override
    public View getChildView(int parent, int child, boolean lastChild, View convertview,
                             ViewGroup parentview) {
        String child_title = (String) getChild(parent, child);

        if (convertview == null) {
            LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertview = inflator.inflate(R.layout.child_layout, parentview, false);
        }
        final TextView child_textview = (TextView) convertview.findViewById(R.id.child_txt);
        // Intent intent = new Intent(this, SettingsActivity.class);

        child_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currenttext = child_textview.getText().toString();
                if (currenttext.equals("Customize")) {

                    //Log.v(Config.LOGTAG, currenttext);

                }
            }
        });
        //Give this child a specific Tag
//        String id = "@+id/child_button" + Integer.toString(nextId);
//        child_textview.setTag(id);
//        id += 1;

        child_textview.setText(child_title);
        return convertview;
    }

    @Override
    public int getChildrenCount(int arg0) {

        return listCategory.get(list.get(arg0)).size();
    }

    @Override
    public Object getGroup(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public long getGroupId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getGroupView(int parent, boolean isExpanded, View convertview, ViewGroup parentview) {
        // TODO Auto-generated method stub
        String group_title = (String) getGroup(parent);
        if (convertview == null) {
            LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertview = inflator.inflate(R.layout.parent_layout, parentview, false);
        }
        TextView parent_textview = (TextView) convertview.findViewById(R.id.parent_txt);
        parent_textview.setTypeface(null, Typeface.BOLD);
        parent_textview.setText(group_title);
        return convertview;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

}