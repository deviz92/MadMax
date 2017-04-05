package com.polito.mad17.madmax.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.polito.mad17.madmax.Entities.Group;
import com.polito.mad17.madmax.R;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity {

    private ArrayList<Group> groups = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        Button friendsbutton = (Button) findViewById(R.id.friendsbutton);

        friendsbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Context context = GroupsActivity.this;
                Class destinationActivity = FriendsActivity.class;
                Intent intent = new Intent(context, destinationActivity);
                startActivity(intent);

            }
        });



        listView = (ListView) findViewById(R.id.lv_list_groups);

        for(int i = 1; i <= 20; i++)
        {
            Group group = new Group(String.valueOf(i), "Group" + i, "imgGroup" + i);
            // Log.d("DEBUG", group.toString());
            groups.add(group);
        }

        ListAdapter listAdapter = new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return groups.size();
            }

            @Override
            public Object getItem(int position) {
                return groups.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.group_item, parent, false);
                }

                Group group = groups.get(position);

                // ImageView groupImage = (ImageView) convertView.findViewById(R.id.img_group);
                // groupImage.setImageResource(group.getImage());

                TextView groupName = (TextView) convertView.findViewById(R.id.tv_group_name);
                groupName.setText(group.getName());

                TextView numberNotifications = (TextView) convertView.findViewById(R.id.tv_group_num_notifications);
                numberNotifications.setText(group.getNumberNotifications().toString());

                return convertView;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };

        listView.setAdapter(listAdapter);
    }

    public void onClickOpenGroup(View view) {

        TextView groupName = (TextView) view;
        Log.d("DEBUG", groupName.getText().toString());

        Intent myIntent = new Intent(GroupsActivity.this, GroupDetailsActivity.class);
        myIntent.putExtra("groupName", groupName.getText().toString()); //Optional parameters
        GroupsActivity.this.startActivity(myIntent);
    }
}