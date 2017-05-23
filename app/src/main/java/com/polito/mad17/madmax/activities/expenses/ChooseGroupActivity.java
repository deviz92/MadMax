package com.polito.mad17.madmax.activities.expenses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polito.mad17.madmax.R;
import com.polito.mad17.madmax.activities.MainActivity;
import com.polito.mad17.madmax.activities.OnItemClickInterface;
import com.polito.mad17.madmax.activities.OnItemLongClickInterface;
import com.polito.mad17.madmax.activities.groups.GroupsViewAdapter;
import com.polito.mad17.madmax.entities.Group;

import java.util.HashMap;

public class ChooseGroupActivity extends AppCompatActivity implements GroupsViewAdapter.ListItemClickListener {

    private FirebaseDatabase firebaseDatabase = MainActivity.getDatabase();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private static final String TAG = ChooseGroupActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private GroupsViewAdapter groupsViewAdapter;
    public static HashMap<String, Group> groups = new HashMap<>();
    private OnItemClickInterface onClickGroupInterface;


    //todo RIFARE USANDO GroupsFragment

    public void setInterface(OnItemClickInterface onItemClickInterface) {
        onClickGroupInterface = onItemClickInterface;

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_group);

        recyclerView = (RecyclerView) findViewById(R.id.rv_skeleton);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        groupsViewAdapter = new GroupsViewAdapter(this, groups);
        recyclerView.setAdapter(groupsViewAdapter);

        //Ascolto i gruppi dello user
        databaseReference.child("users").child(MainActivity.getCurrentUser().getID()).child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Per ogni gruppo dello user
                for (DataSnapshot groupSnapshot: dataSnapshot.getChildren())
                {
                    //Se il gruppo è true, ossia è ancora tra quelli dello user
                    if (groupSnapshot.getValue(Boolean.class))
                        getGroup(groupSnapshot.getKey());
                    else
                    {
                        //tolgo il gruppo da quelli che verranno stampati, così lo vedo sparire realtime
                        groups.remove(groupSnapshot.getKey());
                        groupsViewAdapter.update(groups);
                        groupsViewAdapter.notifyDataSetChanged();

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, databaseError.toException());
            }
        });




    }

    @Override
    public void onListItemClick(String groupID) {
        Log.d(TAG, "clickedItemIndex " + groupID);
        //onClickGroupInterface.itemClicked(getClass().getSimpleName(), groupID);
        Intent myIntent = new Intent(ChooseGroupActivity.this, NewExpenseActivity.class);
        myIntent.putExtra("userID", MainActivity.getCurrentUser().getID());
        myIntent.putExtra("groupID", groupID);
        myIntent.putExtra("callingActivity", "ChooseGroupActivity");
        myIntent.putExtra("groupName", groups.get(groupID).getName());
        startActivity(myIntent);
    }

    public void getGroup(final String id)
    {
        databaseReference.child("groups").child(id).addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Group g = new Group();
                g.setName(dataSnapshot.child("name").getValue(String.class));
                groups.put(id, g);

                groupsViewAdapter.update(groups);
                groupsViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}