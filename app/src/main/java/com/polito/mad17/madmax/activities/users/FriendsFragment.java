package com.polito.mad17.madmax.activities.users;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polito.mad17.madmax.R;
import com.polito.mad17.madmax.activities.OnItemClickInterface;
import com.polito.mad17.madmax.entities.User;

import java.util.HashMap;

public class FriendsFragment extends Fragment implements FriendsViewAdapter.ListItemClickListener {

    private static final String TAG = FriendsFragment.class.getSimpleName();
    private DatabaseReference mDatabase;
    private HashMap<String, User> friends = new HashMap<>();
    private ListView lv;
    private HashMapFriendsAdapter adapter;


    private OnItemClickInterface onClickFriendInterface;

    public void setInterface(OnItemClickInterface onItemClickInterface) {
        onClickFriendInterface = onItemClickInterface;
    }

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FriendsViewAdapter friendsViewAdapter;

    public FriendsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.skeleton_listview, container, false);
        lv = (ListView) view.findViewById(R.id.rv_skeleton);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        //todo myselfID deve essere preso dalla MainActivty, non deve essere definito qui!!
        String myselfID = "-KjTCeDmpYY7gEOlYuSo";


        setInterface((OnItemClickInterface) getActivity());


        mDatabase.child("users").child(myselfID).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot: dataSnapshot.getChildren())
                {
                    getFriend(friendSnapshot.getKey());
                }

                adapter = new HashMapFriendsAdapter(friends);
                lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_skeleton);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        friendsViewAdapter = new FriendsViewAdapter(this);
        recyclerView.setAdapter(friendsViewAdapter);

        friendsViewAdapter.setFriendsData(MainActivity.myself.getUserFriends(), MainActivity.myself);*/

        Log.d(TAG, "dopo setAdapter");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onListItemClick(String friendID) {
        Log.d(TAG, "clickedItemIndex " + friendID);
        onClickFriendInterface.itemClicked(getClass().getSimpleName(), friendID);
    }

    public void getFriend(final String id)
    {
        mDatabase.child("users").child(id).addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User u = new User();
                u.setName(dataSnapshot.child("name").getValue(String.class));
                u.setSurname(dataSnapshot.child("surname").getValue(String.class));
                friends.put(id, u);
                adapter.update(friends);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}