package com.mouboukr.sofianeaoufi.chat;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.zip.Inflater;



public class OnlineFragment extends Fragment {

  private View fview;
    private FirebaseAuth auth;
    private RecyclerView mrecyclerView;
    private List<Groups> OnlineFriends_List;
    private String primary_id;
    friendsList_RecuclerViewAdapter friendsListRecuclerViewAdapter ;
    public OnlineFragment() {
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        fview = inflater.inflate(R.layout.friends_fragment, container, false);
        mrecyclerView = (RecyclerView) fview.findViewById(R.id.frindsGrouop_Recycler);
         friendsListRecuclerViewAdapter = new friendsList_RecuclerViewAdapter(getContext(), OnlineFriends_List);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mrecyclerView.setAdapter(friendsListRecuclerViewAdapter);




        return fview;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        OnlineFriends_List=new ArrayList<>();

        primary_id= FirebaseAuth.getInstance().getCurrentUser().getUid();

        getUsrsID();
    }

    List<String> list_keys = new ArrayList<String>();
    List<String> Second_list_keys = new ArrayList<String>();

    int i;
  private void getUsrsID()
  {

         String id=FirebaseAuth.getInstance().getCurrentUser().getUid();
      DatabaseReference database=FirebaseDatabase.getInstance().getReference().child("Users").child(primary_id).child("Friends List");
      database.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              if(dataSnapshot.exists())
              {
                  for(DataSnapshot id : dataSnapshot.getChildren() )
                  {
                      if(!id.getKey().equals(primary_id))
                      {
                          i++;

                          list_keys.add(id.getKey());
                          onlineOrNot(id.getKey());

                          if(i==(int) dataSnapshot.getChildrenCount())
                          {
                             // getAll_Info();
                          listenr();
                          }

                      }

                  }


              }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });

  }

    private void onlineOrNot(final String key) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Connect").child(key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    Information(key,true);
                else
                    return;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    private void listenr()
    {

        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Connect");
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(list_keys.contains(dataSnapshot.getKey()))
                {
                    checkif_deja_afficher(dataSnapshot.getKey());

                    //Information(dataSnapshot.getKey(),true);
                }
                    /*for(DataSnapshot dt: dataSnapshot.getChildren() )
                    {
                        if(!Second_list_keys.contains(dt.getKey()))
                       Second_list_keys.add(dt.getKey());

                        if(list_keys.contains(dt.getKey()))
                            Information(dt.getKey(),true);

                    }*/

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(list_keys.contains(dataSnapshot.getKey()))
                {
                    //list_keys.remove(id);





                    //OnlineFriends_List.remove(id);
                    int index ;
                    final String s=dataSnapshot.getKey();
                    DatabaseReference dbreference=FirebaseDatabase.getInstance().getReference().child("Users").child(s);
                    dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                if (dataSnapshot.child("profileImage").exists()) {
                                    if (dataSnapshot.child("profileImage") != null) {
                                        //lstFriendGroups.add(new Groups(email, name,dataSnapshot.child("profileImage").getValue().toString(),key));
                                        Groups g=new Groups(dataSnapshot.child("email").getValue().toString(), dataSnapshot.child("name").getValue().toString(), null, dataSnapshot.child("profileImage").getValue().toString(),s, true);
                                        removeItem(g);

                                    }

                                    // OnlineFriends_List.contains(dataSnapshot.child("email"),dataSnapshot.child("name").getValue().toString(),null,null,id,true)

                                }
                                else {

                                    Groups g=new Groups(dataSnapshot.child("email").getValue().toString(), dataSnapshot.child("name").getValue().toString(), null,null,s, true);
                                    removeItem(g);
                                }



                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkif_deja_afficher(final String keys) {
   DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Users").child(keys);
   db.addListenerForSingleValueEvent(new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot) {
           if(dataSnapshot.exists())
           {

               if(dataSnapshot.child("profileImage").exists())
               {
                   if (dataSnapshot.child("profileImage") != null) {
                       //lstFriendGroups.add(new Groups(email, name,dataSnapshot.child("profileImage").getValue().toString(),key));
                       Groups g = new Groups(email, name, null, dataSnapshot.child("profileImage").getValue().toString(), keys, true);

                       if (OnlineFriends_List.size() != 0)
                       {
                           ListIterator<Groups> iter = OnlineFriends_List.listIterator();

                       while (iter.hasNext()) {
                           if (iter.next().getPrimaryKey().equals(g.getPrimaryKey())) {
                               //  iter.remove();
                           } else {
                               Information(keys, true);
                           }
                           //friendsListRecuclerViewAdapter.notifyDataSetChanged();//OnlineFriends_List
                       }
                   }
                   else {
                           Information(keys, true);
                       }

                   }


               }
               else {
                   //lstFriendGroups.add(new Groups(email, name,null,key));
                   Groups g=new Groups(email,name,null,null,keys,true);

                   ListIterator<Groups> iter = OnlineFriends_List.listIterator();
                   while(iter.hasNext()){
                       if(iter.next().getPrimaryKey().equals(g.getPrimaryKey())){
                           //iter.remove();
                       }
                       else {
                           Information(keys,true);
                       }
                       friendsListRecuclerViewAdapter.notifyDataSetChanged();//OnlineFriends_List
                   }

               }


           }
       }

       @Override
       public void onCancelled(DatabaseError databaseError) {

       }
   });

  }


    private void getAll_Info()
 {

     final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Connect");//.child(id);
       databaseReference.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                   for(DataSnapshot ids : dataSnapshot.getChildren() )
                   {
                       if(list_keys.contains(ids))
                       {

                           Information(ids.getKey(),true);

                           //Information(id,true);
                           //new friendsList_RecuclerViewAdapter(getContext(),OnlineFriends_List).addItem();

                       }
                   }






                   //return;


           }

           @Override
           public void onChildChanged(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onChildRemoved(DataSnapshot dataSnapshot) {
              if(list_keys.contains(dataSnapshot.getKey()))
              {
                  //list_keys.remove(id);





                  //OnlineFriends_List.remove(id);
                 int index ;
                 final String s=dataSnapshot.getKey();
                 DatabaseReference dbreference=FirebaseDatabase.getInstance().getReference().child("Users").child(s);
                 dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         if (dataSnapshot.exists()) {

                             if (dataSnapshot.child("profileImage").exists()) {
                                 if (dataSnapshot.child("profileImage") != null) {
                                     //lstFriendGroups.add(new Groups(email, name,dataSnapshot.child("profileImage").getValue().toString(),key));
                                     Groups g=new Groups(dataSnapshot.child("email").getValue().toString(), dataSnapshot.child("name").getValue().toString(), null, dataSnapshot.child("profileImage").getValue().toString(),s, true);
                                      removeItem(g);

                                 }

                                // OnlineFriends_List.contains(dataSnapshot.child("email"),dataSnapshot.child("name").getValue().toString(),null,null,id,true)

                             }
                             else {

                                 Groups g=new Groups(dataSnapshot.child("email").getValue().toString(), dataSnapshot.child("name").getValue().toString(), null,null,s, true);
                                 removeItem(g);
                             }



                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });


              }

           }


           @Override
           public void onChildMoved(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

 }

    private void removeItem(Groups g) {
        ListIterator<Groups> iter = OnlineFriends_List.listIterator();
        while(iter.hasNext()){
            if(iter.next().getPrimaryKey().equals(g.getPrimaryKey())){
                iter.remove();
            }
            friendsListRecuclerViewAdapter.notifyDataSetChanged();//OnlineFriends_List
        }


  }


    String email="",name="";
    private void Information(final String key, final Boolean bo) {
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

                    if(dataSnapshot.child("profileImage").exists())
                    {
                        if (dataSnapshot.child("profileImage") != null)
                        {
                            //lstFriendGroups.add(new Groups(email, name,dataSnapshot.child("profileImage").getValue().toString(),key));
                            OnlineFriends_List.add(new Groups(email,name,null,dataSnapshot.child("profileImage").getValue().toString(),key,bo));

                        }


                    }
                    else {
                        //lstFriendGroups.add(new Groups(email, name,null,key));
                        OnlineFriends_List.add(new Groups(email,name,null,null,key,bo));


                    }
                    //lstFriendGroups.add(new Groups(email, "online",dataSnapshot.child("profileImage").getValue().toString(),ridekey));

                   /* mrecyclerView = (RecyclerView) fview.findViewById(R.id.frindsGrouop_Recycler);
                    friendsList_RecuclerViewAdapter friendsListRecuclerViewAdapter = new friendsList_RecuclerViewAdapter(getContext(), OnlineFriends_List);
                    mrecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    mrecyclerView.setAdapter(friendsListRecuclerViewAdapter);

                     */
                    friendsListRecuclerViewAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
