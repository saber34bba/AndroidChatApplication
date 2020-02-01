package com.mouboukr.sofianeaoufi.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private TabLayout mtableLayout;
    private ViewPagerAdapter mviewPagerAdapter;
    private ViewPager mviewPager;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.main_toolbar_id);
        toolbar.setTitle("KAlemni");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        auth.getInstance();
        mtableLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        mviewPager = (ViewPager) findViewById(R.id.viewPager_ID);
        mviewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        //ADD  fragment
        mviewPagerAdapter.AddFragment(new OnlineFragment(), "Online");
        mviewPagerAdapter.AddFragment(new FriendsListFragment(), "My Friend");
        mviewPagerAdapter.AddFragment(new messageFragment(), "Chat");
        //set Icon to tabLayout

        mviewPager.setAdapter(mviewPagerAdapter);
        mtableLayout.setupWithViewPager(mviewPager);
        mtableLayout.pageScroll(2);

        // tab begin from 0
        mtableLayout.getTabAt(2).setIcon(R.drawable.ic_chat_asset);
        mtableLayout.getTabAt(1).setIcon(R.drawable.ic_action_group);

        /*ActionBar actionBar=getSupportActionBar();
        actionBar.setElevation(1);
        */
        //listen();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser id = FirebaseAuth.getInstance().getCurrentUser();
        //String id=auth.getCurrentUser().getUid(); //get currunt user that authentificat with succes
        if (id == null) {
            Intent i = new Intent(MainActivity.this, FirstActivity.class);
            startActivity(i);
            finish();
        }
        else {
            String primaryID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Connect").child(primaryID);
            db.setValue(true);

        }
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_logout_button) {
            String primaryID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (primaryID != null) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Connect").child(primaryID);
                db.removeValue();
                t=true;
            }
                FirebaseAuth.getInstance().signOut();
            //Intent i = new Intent(MainActivity.this, FirstActivity.class);
           // startActivity(i);
            finish();
        }

        if (item.getItemId() == R.id.main_myProfile_button) {
            Intent i=new Intent(MainActivity.this,UserProfileActivity.class);
            startActivity(i);

        }

        if (item.getItemId() == R.id.main_allUser_button) {
            Intent i=new Intent(MainActivity.this,SeachrForPeopleActivity.class);
            startActivity(i);
        }
        if(item.getItemId()==R.id.main_friendshipRequest)
        {
            Intent i=new Intent(MainActivity.this,FriendshipRequestsActivity.class);
            startActivity(i);

        }

        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        listen();
    }



Boolean t=false;
    @Override
    protected void onPause() {
        super.onPause();

        if(t==false) {
            String primaryID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (primaryID != null) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Connect").child(primaryID);
                db.removeValue();

            }
        }
    }




    List<String> list = new ArrayList<String>();
  private void listen()
  {

      String id=FirebaseAuth.getInstance().getCurrentUser().getUid();

    DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("messages");
    db.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists())
            {
                int t=0;
                for(DataSnapshot dt:dataSnapshot.getChildren())
                {
                     t++;
                    if (!list.contains(dt.getKey()))
                    {
                        list.add(dt.getKey());
                    }
                   if(t==(int)dataSnapshot.getChildrenCount())
                   {
                      //onchildadded ll message ida kan not exist in list wa add it else do nothing
                        listenForChildMessage();
                   }
                }


            }



        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });






  }

    private void listenForChildMessage()
    {
        final DatabaseReference db_user=FirebaseDatabase.getInstance().getReference().child("Users");
        final String id=FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("All_Messages");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           if(dataSnapshot.exists())
           {
               String ss= dataSnapshot.getKey();
               ss="";
               //if(db_user.child(id).child("messages").child(s)  id kan new id exist in messages we show message



             if(list.contains(dataSnapshot.getKey()))
             {
               //get last message and show it

             }

           }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists())
                {
                    String d=s;

                    String sss=dataSnapshot.getKey();
                     sss="";
                    //get this message
                    get_last_message(dataSnapshot.getKey());

                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

String lastMessage_str="";
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



    private  void get_profile_key(String id, final String msg)
    {
        String auth=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(auth).child("messages").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("friend id") != null) {

                        //Information(map.get("friend id").toString(),msg);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }









}