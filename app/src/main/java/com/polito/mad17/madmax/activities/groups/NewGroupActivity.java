package com.polito.mad17.madmax.activities.groups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polito.mad17.madmax.R;
import com.polito.mad17.madmax.activities.MainActivity;
import com.polito.mad17.madmax.activities.OnItemClickInterface;
import com.polito.mad17.madmax.activities.users.FriendsViewAdapter;
import com.polito.mad17.madmax.activities.users.HashMapFriendsAdapter;
import com.polito.mad17.madmax.activities.users.NewMemberActivity;
import com.polito.mad17.madmax.entities.Group;
import com.polito.mad17.madmax.entities.User;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.polito.mad17.madmax.activities.MainActivity.currentUser;

public class NewGroupActivity extends AppCompatActivity implements FriendsViewAdapter.ListItemClickListener {

    private static final String TAG = "NewGroupActivity";
    private EditText nameGroup;
    private EditText descriptionGroup;
    private ImageView imageGroup;
    private String imageString = null;
    private int PICK_IMAGE_REQUEST = 1; // to use for selecting the group image
    private ListView lv;
    private DatabaseReference mDatabase;
    String tempGroupID;
    public static HashMap<String, User> newmembers = new HashMap<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FriendsViewAdapter friendsViewAdapter;
    private OnItemClickInterface onClickFriendInterface;
    private User userAdded;
    private HashMapFriendsAdapter adapter;
    //private String myselfID;

    public static HashMap<String, Group> groups = currentUser.getUserGroups();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        lv = (ListView) findViewById(R.id.members);

        nameGroup = (EditText) findViewById(R.id.et_name_group);
        descriptionGroup = (EditText) findViewById(R.id.et_description_group);
        imageGroup = (ImageView) findViewById(R.id.group_image);
//        imageGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // allow to choose the group image
//                Intent intent = new Intent();
//                // Show only images, no videos or anything else
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                // Always show the chooser (if there are multiple options available)
//                startActivityForResult(Intent.createChooser(intent,"Select picture"), PICK_IMAGE_REQUEST);
//            }
//        });

        Intent intent = getIntent();
        //myselfID = intent.getStringExtra("UID");
        Bundle b = getIntent().getExtras();
        if (b != null)
            userAdded = b.getParcelable("userAdded");
        if (userAdded != null)
            newmembers.put(userAdded.getID(), userAdded);

        //Button to add a new member
        Button newMemberButton = (Button) findViewById(R.id.addmember);
        newMemberButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Context context = NewGroupActivity.this;
                Class destinationActivity = NewMemberActivity.class;
                Intent intent = new Intent(context, destinationActivity);
                intent.putExtra("UID", currentUser.getID());
                //intent.putExtra("groupID", tempGroupID);
                //startActivity(intent);
                startActivityForResult(intent, 1);


            }
        });

        adapter = new HashMapFriendsAdapter(newmembers);
        lv.setAdapter(adapter);


        /*
        recyclerView = (RecyclerView) findViewById(R.id.members);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        friendsViewAdapter = new FriendsViewAdapter(this);
        recyclerView.setAdapter(friendsViewAdapter);

        friendsViewAdapter.setMembersData(newmembers, MainActivity.myself);
        */


        Log.d(TAG, "Arrivato alla fine della OnCreate");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }


    //When i click SAVE
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        if (itemThatWasClickedId == R.id.action_save) {

            //display message if text field is empty
            if(TextUtils.isEmpty(nameGroup.getText().toString())){
                nameGroup.setError(getString(R.string.required));
                return false;
            }
            //Integer newID = groups.size();
            String name = nameGroup.getText().toString();
            String description = descriptionGroup.getText().toString();

            Group newGroup = new Group("0", name, "noImage", description, 1);  //id is useless

            //add new group to database
            String newgroup_id = mDatabase.child("groups").push().getKey();
            mDatabase.child("groups").child(newgroup_id).setValue(newGroup);
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            mDatabase.child("groups").child(newgroup_id).child("timestamp").setValue(timeStamp);
            mDatabase.child("groups").child(newgroup_id).child("numberMembers").setValue(newmembers.size());

            //add users to new group
            for(Map.Entry<String, User> user : newmembers.entrySet())
            {
                joinGroupFirebase(user.getValue().getID(), newgroup_id);
            }


            Intent intent = new Intent(NewGroupActivity.this, MainActivity.class);
            intent.putExtra("UID", currentUser.getID());

//          Log.d("DEBUG", "groups.size() before " + groups.size());

            /*
            if(imageString!=null)
                newGroup = new Group(newID.toString(), name, imageString, description);
            else
                newGroup = new Group(newID.toString(), name, String.valueOf(R.mipmap.group), description);

            groups.put(newGroup.getID(), newGroup);
            */


            //remove group from temporary
            //mDatabase.child("temporarygroups").child(tempGroupID).removeValue();

            newmembers.clear();

            Toast.makeText(getBaseContext(), "Saved group", Toast.LENGTH_SHORT).show();

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void joinGroupFirebase (final String userID, String groupID)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Aggiungo gruppo alla lista gruppi dello user
        mDatabase.child("users").child(userID).child("groups").push();
        mDatabase.child("users").child(userID).child("groups").child(groupID).setValue("true");
        //Aggiungo user (con sottocampi admin e timestamp) alla lista membri del gruppo
        mDatabase.child("groups").child(groupID).child("members").push();
        mDatabase.child("groups").child(groupID).child("members").child(userID).push();
        if(userID.equals(currentUser.getID())) {
            mDatabase.child("groups").child(groupID).child("members").child(userID).child("admin").setValue("true");
        }
        else {
            mDatabase.child("groups").child(groupID).child("members").child(userID).child("admin").setValue("false");
        }
        mDatabase.child("groups").child(groupID).child("members").child(userID).push();
        mDatabase.child("groups").child(groupID).child("members").child(userID).child("timestamp").setValue("time");

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    public void onListItemClick(String friendID) {
        //Log.d("clickedItemIndex " + friendID);
        System.out.println("clickedItemIndex " + friendID);
        onClickFriendInterface.itemClicked(getClass().getSimpleName(), friendID);
    }
}
