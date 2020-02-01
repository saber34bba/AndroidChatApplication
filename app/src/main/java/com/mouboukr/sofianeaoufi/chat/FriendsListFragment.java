package com.mouboukr.sofianeaoufi.chat;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Acer on 2018-03-22.
 */

public class FriendsListFragment extends Fragment {
private DatabaseReference mfriendDatabaseReference;
private FirebaseAuth auth;
View fview;
private RecyclerView mrecyclerView;
private List<Groups> lstFriendGroups;

    public FriendsListFragment() {

    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            fview = inflater.inflate(R.layout.friends_fragment, container, false);
            mrecyclerView = (RecyclerView) fview.findViewById(R.id.frindsGrouop_Recycler);
            friendsList_RecuclerViewAdapter friendsListRecuclerViewAdapter = new friendsList_RecuclerViewAdapter(getContext(), lstFriendGroups);
            mrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mrecyclerView.setAdapter(friendsListRecuclerViewAdapter);


            return fview;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUsersId();


        lstFriendGroups=new ArrayList<>();

      primary_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    String primary_id;
    private void getUsersId() {
        String id= FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userHistoryDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Friends List");
        userHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot id : dataSnapshot.getChildren() )
                    {
                       if(!id.getKey().equals(primary_id))
                       {
                          Information(id.getKey());
                       }

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    String email="",name="",status="";
    private void Information(final String key) {
        DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("email") != null) {

                        email = map.get("email").toString();
                    }

                    if (map.get("name") != null){
                         name=map.get("name").toString();
                    }
                    if(map.get("status")!=null)
                    {
                        status=map.get("status").toString();
                    }

                    if(dataSnapshot.child("profileImage").exists())
                    {
                        if (dataSnapshot.child("profileImage") != null)
                        {
                            lstFriendGroups.add(new Groups(email,name,status,dataSnapshot.child("profileImage").getValue().toString(),key,false));

                        }


                    }
                    else {
                        lstFriendGroups.add(new Groups(email,name,status,null,key,false));


                    }

                    mrecyclerView = (RecyclerView) fview.findViewById(R.id.frindsGrouop_Recycler);
                    friendsList_RecuclerViewAdapter friendsListRecuclerViewAdapter = new friendsList_RecuclerViewAdapter(getContext(), lstFriendGroups);
                    mrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    mrecyclerView.setAdapter(friendsListRecuclerViewAdapter);
                    status="";


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();



    }
}
