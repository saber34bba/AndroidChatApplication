package com.mouboukr.sofianeaoufi.chat;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendshipRequestsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    friendsList_RecuclerViewAdapter  adapter;
    private List<Groups> lstFriendGroups;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendship_requests);



        recyclerView=(RecyclerView)findViewById(R.id.friendship_requestsRcyclerView);
        userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        lstFriendGroups=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        search_friendshipRequsts();

    }

    private  void search_friendshipRequsts()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("friendship Requsest from");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {

                    for (DataSnapshot id : dataSnapshot.getChildren()) {

                        loadUsers(id.getKey());


                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    String name,email;
    private void loadUsers(final String key) {
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("email") != null) {

                        email = map.get("email").toString();
                    }

                    if (map.get("name") != null){
                        name=map.get("name").toString();
                    }

                    if(dataSnapshot.child("profileImage").exists())
                    {
                        if (dataSnapshot.child("profileImage") != null)
                        {
                            //lstFriendGroups.add(new Groups(email, name,dataSnapshot.child("profileImage").getValue().toString(),key));
                            lstFriendGroups.add(new Groups(email,name,null,dataSnapshot.child("profileImage").getValue().toString(),key,false));

                        }


                    }
                    else {
                        //lstFriendGroups.add(new Groups(email, name,null,key));
                        lstFriendGroups.add(new Groups(email,name,null,null,key,false));


                    }

                    adapter=new friendsList_RecuclerViewAdapter(FriendshipRequestsActivity.this,lstFriendGroups);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
