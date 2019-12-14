package com.example.basavara.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.basavara.R;

import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> division;
    Map<String,List<String>> city;

    public MyExpandableListAdapter(Context context, List<String> lang, Map<String, List<String>> topics) {
        this.context = context;
        this.division = lang;
        this.city = topics;
    }

    @Override
    public int getGroupCount() {
        return division.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return city.get(division.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return division.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return city.get(division.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String division = (String) getGroup(groupPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lauout_city,null);
        }

        TextView list_parent = convertView.findViewById(R.id.tvParent);
        list_parent.setText(division);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String city = (String) getChild(groupPosition,childPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_division,null);
        }

        TextView list_child = convertView.findViewById(R.id.tvChild);
        list_child.setText(city);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
