package com.polito.mad17.madmax.activities.expenses;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.polito.mad17.madmax.R;
import com.polito.mad17.madmax.activities.MainActivity;
import com.polito.mad17.madmax.activities.OnItemClickInterface;
import com.polito.mad17.madmax.entities.Expense;
import com.polito.mad17.madmax.utilities.FirebaseUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class PendingExpensesFragment extends Fragment implements PendingExpenseViewAdapter.ListItemClickListener {

    private static final String TAG = PendingExpensesFragment.class.getSimpleName();
    private FirebaseDatabase firebaseDatabase = MainActivity.getDatabase();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private TreeMap<String, Expense> pendingExpensesMap = new TreeMap<>(Collections.reverseOrder());

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PendingExpenseViewAdapter pendingExpenseViewAdapter;

    private OnItemClickInterface onClickGroupInterface;

    public void setInterface(OnItemClickInterface onItemClickInterface) {
        onClickGroupInterface = onItemClickInterface;
    }

    public PendingExpensesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*getSupportFragmentManager().beginTransaction()
                .add(detailFragment, "detail")
                // Add this transaction to the back stack
                .addToBackStack()
                .commit();*/



        setInterface((OnItemClickInterface) getActivity());

        View view = inflater.inflate(R.layout.skeleton_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_skeleton);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.vertical_divider);
        verticalDecoration.setDrawable(verticalDivider);
        recyclerView.addItemDecoration(verticalDecoration);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        pendingExpenseViewAdapter = new PendingExpenseViewAdapter(this, pendingExpensesMap, getActivity());
        recyclerView.setAdapter(pendingExpenseViewAdapter);


        //Ascolto le pending expenses dello user
        databaseReference.child("users").child(MainActivity.getCurrentUser().getID()).child("proposedExpenses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Per ogni pending expense dello user
                for (DataSnapshot pendingExpenseSnap : dataSnapshot.getChildren())
                {
                    //Se la pending expense non è stata eliminata (NELLO USER)
                    if (pendingExpenseSnap.getValue(Boolean.class))
                    {
                        FirebaseUtils.getInstance().getPendingExpense(pendingExpenseSnap.getKey(), pendingExpensesMap, pendingExpenseViewAdapter);
                        pendingExpenseViewAdapter.update(pendingExpensesMap);
                        pendingExpenseViewAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        //tolgo la spesa da quelle che verranno stampate, così la vedo sparire realtime
                        pendingExpensesMap.remove(pendingExpenseSnap.getKey());
                        pendingExpenseViewAdapter.update(pendingExpensesMap);
                        pendingExpenseViewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
    public void onListItemClick(String groupID) {
        Log.d(TAG, "clickedItemIndex " + groupID);
        onClickGroupInterface.itemClicked(getClass().getSimpleName(), groupID);
    }
}

