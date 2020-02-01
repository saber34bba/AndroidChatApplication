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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Acer on 2018-03-22.
 */
  // online fragment it will be friends list fragment
public class messageFragment extends Fragment {
    //private DatabaseReference mfriendDatabaseReference;
    private FirebaseAuth ath;
    View fview;
    private RecyclerView mrecyclerView;
    private List<Groups> lstFriendGroups;

    public messageFragment() {

    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        fview=inflater.inflate(R.layout.friend_online,container,false);

        mrecyclerView = (RecyclerView) fview.findViewById(R.id.Online_Recycler);
        messageFragment_RecyclerViewAdapter messageRecyclerAdapter = new messageFragment_RecyclerViewAdapter(getContext(), lstFriendGroups);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mrecyclerView.setAdapter(messageRecyclerAdapter);

        return fview;
    }







    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getFrinedsId();
        get_friend_chating_Key();
        lstFriendGroups=new ArrayList<>();

    }



    /* private void getFrinedsId() {
         String id=FirebaseAuth.getInstance().getCurrentUser().getUid();
         DatabaseReference userHistoryDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("messages");
         userHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists()){
                     for(DataSnapshot id : dataSnapshot.getChildren() )
                     {
                         Information(id.getKey());
                         get_profileKey(id.getKey());

                     }

                 }

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

     }*/
    String auth;
    private void get_friend_chating_Key()
    {
        auth=FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(auth).child("messages");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot id : dataSnapshot.getChildren() )
                    {
                        get_last_message(id.getKey());
                        //get_profile_key(id.getKey());

                        //databaseReference.child(dataSnapshot.getKey()); bah mandirouch 2fonction

                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void get_last_message(final String key) {

        final String primary_id=FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("All_Messages").child(key);
        Query lastQry=databaseReference.orderByKey().limitToLast(1);

        lastQry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
               /*String key= dataSnapshot.getValue().toString();
               String s=dataSnapshot.getKey().toString();*/

                    Iterator<DataSnapshot> it=dataSnapshot.getChildren().iterator();
                    if(it.hasNext())
                    {
                        DataSnapshot items=it.next();
                        String d= items.getKey();
                        String g= items.getValue().toString();

                        Iterator<DataSnapshot> tt= items.getChildren().iterator();
                        if(tt.hasNext())
                        {
                            DataSnapshot dd=tt.next();
                            String ds=dd.getKey();
                            if(!primary_id.equals(ds))
                            {

                            }
                            lastMessage_str=dd.getValue().toString();
                            //String dsd=lst_msg;
                            get_profile_key(key,lastMessage_str);

                        }
                    }

                    //oad_thisMessage(key,lst_msg);
                    //get_profile_key(key);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    //String lst_msg="";
   /* private void load_thisMessage(String key, final String message) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("All_Messages").child(key)
                .child(message);
             ref.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {

                     if(dataSnapshot.exists())
                     {
                         lastMessage_str=dataSnapshot.getValue().toString();
                         get_profile_key(message);

                     }

                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });

    }*/

    private  void get_profile_key(String id, final String msg)
    {


        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(auth).child("messages").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("friend id") != null) {

                        Information(map.get("friend id").toString(),msg);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    String email="",lastMessage_str="",name="";
    private void Information(final String key, final String msg) {
        DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("email") != null) {

                        email = map.get("email").toString();
                    }
                    if (map.get("name") != null) {

                        name = map.get("name").toString();
                    }
                    if(dataSnapshot.child("profileImage").exists()) {
                        if (dataSnapshot.child("profileImage") != null) {
                            lstFriendGroups.add(new Groups(email,name, msg, dataSnapshot.child("profileImage").getValue().toString(), key,false));

                        }
                    }
                    else {
                        lstFriendGroups.add(new Groups(email,name, lastMessage_str, null, key,false));

                    }



                    mrecyclerView = (RecyclerView) fview.findViewById(R.id.Online_Recycler);
                    messageFragment_RecyclerViewAdapter recyclerViewAdapter = new messageFragment_RecyclerViewAdapter(getContext(), lstFriendGroups);
                    mrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    mrecyclerView.setAdapter(recyclerViewAdapter);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
















}
