package com.mouboukr.sofianeaoufi.chat;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class ShowProfileSelectedActivity extends AppCompatActivity {
private     String Currunt_profile_ID="";
private Button sendRequest_friendship,decline_btn;
private FirebaseAuth mauth;
private String this_userId="";
private Boolean isSend=false;
 private    DatabaseReference mdatabaseReference;
private TextView already_friend,profile_name;
private int friend_or_not=0;
    ImageView Image;
    ProgressDialog pdiag;
    TextView email,friendNumber;
    RelativeLayout relativeLayout;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile_selected);
        sendRequest_friendship=(Button)findViewById(R.id.ShowProfil_sendRequetButton);
         decline_btn=(Button)findViewById(R.id.Decline);
         profile_name=(TextView)findViewById(R.id.profile_nameTextView);
           Image=(ImageView)findViewById(R.id.other_profile_image);
        relativeLayout=(RelativeLayout)findViewById(R.id.rl_pricipale);

          email=(TextView)findViewById(R.id.emailFriend_txt);
           friendNumber=(TextView)findViewById(R.id.friendNumber_txtVw);

pdiag=new ProgressDialog(this);
pdiag.setMessage("be patient");
pdiag.setTitle("Loading profile information");
pdiag.show();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Currunt_profile_ID= null;
            } else {
                Currunt_profile_ID= extras.getString("key");
            }
        } else {
            Currunt_profile_ID= (String) savedInstanceState.getSerializable("key");
        }
        already_friend=(TextView)findViewById(R.id.already_friend);

        mauth=FirebaseAuth.getInstance();
    this_userId=mauth.getCurrentUser().getUid();






    searchIfFriendOrNOT(this_userId);
    search_if_requestDeja_envoyer(this_userId);
    search_if_request(this_userId);
    profile_fetch_information();
    mdatabaseReference  = FirebaseDatabase.getInstance().getReference();
    }




    String name,em;
int nb;
    private void profile_fetch_information()
    {

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(Currunt_profile_ID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {

                        name = map.get("name").toString();
                    }
                    em=dataSnapshot.child("email").getValue().toString();
                    nb= (int) dataSnapshot.child("Friends List").getChildrenCount();
                    if (dataSnapshot.child("profileImage").exists()) {
                        if (dataSnapshot.child("profileImage") != null) {

                            String image=map.get("profileImage").toString();
                            Glide.with(getApplication()).load(image).apply(RequestOptions.circleCropTransform()).into(Image);
                        }


                    }

                profile_name.setText(name);
               email.setText(em);
               friendNumber.setText(String.valueOf(nb));
                relativeLayout.setVisibility(View.VISIBLE);
                pdiag.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




    private void searchIfFriendOrNOT(String ID)
    {
        DatabaseReference DatabaseFriends_list= FirebaseDatabase.getInstance().getReference().child("Users").child(ID).child("Friends List").child(Currunt_profile_ID);
        DatabaseFriends_list.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    sendRequest_friendship.setText("Cancel friendship");
                    sendRequest_friendship.setBackgroundColor(Color.RED);
                    already_friend.setVisibility(View.VISIBLE);
                    friend_or_not=2;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void search_if_requestDeja_envoyer(String ID)
    {
        DatabaseReference DatabaseFriends_list= FirebaseDatabase.getInstance().getReference().child("Users").child(ID).child("friendship Requsest to").child(Currunt_profile_ID);
        DatabaseFriends_list.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    //sendRequest_friendship.setText("Cancel Request");
                    decline_btn.setVisibility(View.VISIBLE);
                    decline_btn.setText("ignorer FriendShip Request");
                    already_friend.setText("Waiting for reply"); //request was sent
                    already_friend.setVisibility(View.VISIBLE);
                    friend_or_not=1;
                    sendRequest_friendship.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
//2=friend ; 1=request sent ;3=this profile

    private void search_if_request(String ID)
    {
        DatabaseReference DatabaseFriends_list= FirebaseDatabase.getInstance().getReference().child("Users").child(ID).child("friendship Requsest from").child(Currunt_profile_ID);
        DatabaseFriends_list.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    sendRequest_friendship.setText("Accept FriendShip request");
                    decline_btn.setVisibility(View.VISIBLE);
                    friend_or_not=3;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




    public void send(View view) {

      if(friend_or_not==0)
      {

          DatabaseReference db_to = FirebaseDatabase.getInstance().getReference().child("Users").child(this_userId);
          db_to.child("friendship Requsest to").child(Currunt_profile_ID).setValue(true);
          DatabaseReference db_from = FirebaseDatabase.getInstance().getReference().child("Users").child(Currunt_profile_ID);
          db_from.child("friendship Requsest from").child(this_userId).setValue(true);
          sendRequest_friendship.setVisibility(View.GONE);
          decline_btn.setVisibility(View.VISIBLE);



      }

     /* if(friend_or_not==1)
      {
          DatabaseReference db_to = FirebaseDatabase.getInstance().getReference().child("Users").child(this_userId).child("friendship Requsest to").child(Currunt_profile_ID);
          db_to.removeValue();

          DatabaseReference db_from = FirebaseDatabase.getInstance().getReference().child("Users").child(Currunt_profile_ID).child("friendship Requsest from").child(this_userId);
          db_from.removeValue();

      }*/

      if(friend_or_not==2)
      {
          DatabaseReference db_to = FirebaseDatabase.getInstance().getReference().child("Users").child(this_userId).child("Friends List").child(Currunt_profile_ID);
          db_to.removeValue();
          DatabaseReference db_from = FirebaseDatabase.getInstance().getReference().child("Users").child(Currunt_profile_ID).child("Friends List").child(this_userId);
          db_from.removeValue();
          sendRequest_friendship.setText("Send FriendShip Request");

      }

      if(friend_or_not==3)
      {
          DatabaseReference db_to = FirebaseDatabase.getInstance().getReference().child("Users").child(this_userId).child("Friends List").child(Currunt_profile_ID);
          db_to.setValue(true);
          DatabaseReference db_from = FirebaseDatabase.getInstance().getReference().child("Users").child(Currunt_profile_ID).child("Friends List").child(this_userId);
          db_from.setValue(true);

          DatabaseReference db_too = FirebaseDatabase.getInstance().getReference().child("Users").child(Currunt_profile_ID).child("friendship Requsest to").child(this_userId);
          db_too.removeValue();
          DatabaseReference db_froom = FirebaseDatabase.getInstance().getReference().child("Users").child(this_userId).child("friendship Requsest from").child(Currunt_profile_ID);
          db_froom.removeValue();



      }






    }

    public void declineRequest(View view) {
        DatabaseReference db_too = FirebaseDatabase.getInstance().getReference().child("Users").child(this_userId).child("friendship Requsest to").child(Currunt_profile_ID);
        db_too.removeValue();

        DatabaseReference db_froom = FirebaseDatabase.getInstance().getReference().child("Users").child(Currunt_profile_ID).child("friendship Requsest from").child(this_userId);
        db_froom.removeValue();
        decline_btn.setVisibility(View.GONE);
        sendRequest_friendship.setVisibility(View.VISIBLE);
    }
}
