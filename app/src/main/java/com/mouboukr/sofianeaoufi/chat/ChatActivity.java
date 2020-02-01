package com.mouboukr.sofianeaoufi.chat;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private Button send_btn;
    private EditText message;
    private String primary_Id,Friend_key;
    private RecyclerView recyclerView;
    private List<ChatModel>chatModels=new ArrayList<>();
    Chat_RecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mcontext=this;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Friend_key= null;
            } else {
                Friend_key= extras.getString("key");
            }
        } else {
            Friend_key= (String) savedInstanceState.getSerializable("key");
        }
        message=(EditText)findViewById(R.id.write_message_edt);
        send_btn=(Button)findViewById(R.id.send_message_button);
        //RecyclerView
        recyclerView=(RecyclerView)findViewById(R.id.chat_recycler_view);

        adapter = new Chat_RecyclerViewAdapter(this, chatModels);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        primary_Id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Messages();
        //test_friendMessages();
        Boolean t=false;
        loadImage();
        check_message_id();
    }



    private  void check_message_id()
    {
        DatabaseReference user_db= FirebaseDatabase.getInstance().getReference().child("Users").child(primary_Id).child("messages");
        user_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot id : dataSnapshot.getChildren()) {
                        test(id.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    Boolean t=false;
    String message_id;

    String imageid;
    private void loadImage()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(Friend_key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("profileImage").exists())
                    {
                        if (dataSnapshot.child("profileImage") != null)
                        {
                            imageid=dataSnapshot.child("profileImage").getValue().toString();
                        }

                    }
                    else {
                        imageid=null;
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void test(final String ids)
    {
        DatabaseReference user_db= FirebaseDatabase.getInstance().getReference().child("Users").child(Friend_key).child("messages");
        user_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if(t==true)
                        return;
                    for (DataSnapshot id : dataSnapshot.getChildren()) {
                        if(id.getKey().equals(ids)) {
                            message_id=id.getKey();
                            t = true;
                            MyMessages();
                            return;
                        }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void sendMessage(View view) {
        message.getText().toString();
       /* if(TextUtils.isEmpty((CharSequence) message))
        {

        }
        else
        {*/
           /* DatabaseReference user_db= FirebaseDatabase.getInstance().getReference().child("Users").child(primary_Id).child("messagesTo").child(Friend_key);
            String text_id=user_db.push().getKey();
            user_db.child(text_id).child("Text").setValue(message.getText().toString());
            DatabaseReference Friend_db= FirebaseDatabase.getInstance().getReference().child("Users").child(Friend_key).child("messages_From").child(primary_Id);

             Friend_db.child(text_id).child("Text").setValue(message.getText().toString());
             message.setText("");*/

        if(t==true)
        {
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("All_Messages").child(message_id);
            String msg_id=databaseReference.push().getKey();
            databaseReference.child(msg_id).child(primary_Id).setValue(message.getText().toString());
            message.setText("");

        }
        else {
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(primary_Id).child("messages");
            String text_id=ref.push().getKey();
            //ref.child(text_id).setValue(true);
            //ref.child("messageForeignKey").setValue(text_id);
            ref.child(text_id).child("friend id").setValue(Friend_key);

            DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Users").child(Friend_key).child("messages");
            //db.child(text_id).setValue(true);
            //db.child("messageForeignKey").setValue(text_id);

            db.child(text_id).child("friend id").setValue(primary_Id);

            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("All_Messages").child(text_id);
            String msg_id=databaseReference.push().getKey();
            databaseReference.child(msg_id).child(primary_Id).setValue("  "+message.getText().toString());
            message_id=text_id;
            t=true;
            message.setText("");

            //DatabaseReference dataBase=FirebaseDatabase.getInstance().getReference().child("Users").child("new message");

        }





        /*DatabaseReference user_db= FirebaseDatabase.getInstance().getReference().child("Users").child(primary_Id).child("messages");
        String text_id=user_db.push().getKey();
        user_db.child(primary_Id).child(text_id).setValue(message.getText().toString());
        DatabaseReference Friend_db= FirebaseDatabase.getInstance().getReference().child("Users").child(Friend_key).child("messages");

        Friend_db.child(primary_Id).child(text_id).setValue(message.getText().toString());
        message.setText("");*/


        //}

    }


   /* private void Messages()
    {
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Users").child(primary_Id).child("messages");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    MyMessages();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }*/

    Boolean finish=false;
    ArrayList<String>arrayList=new ArrayList<String>();
    private void MyMessages()
    {
        //you must verify if child messageTO exist first
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("All_Messages").child(message_id);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                  /*if(arrayList.contains(dataSnapshot.getKey()))
                      return;*/
                 /* else {
                      for (DataSnapshot idd : dataSnapshot.getChildren()) {
                          arrayList.add(idd.getKey());
                          load_Text(idd.getKey());
                      }

                  }*/
                    arrayList.add(dataSnapshot.getKey());
                    load_Text(dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
              /*if(dataSnapshot.exists())
                  load_Text( dataSnapshot.getKey());*/
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





    private void load_Text(String message)
    {

        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("All_Messages").child(message_id)
                .child(message);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                 /*ChatModel chatModel=dataSnapshot.getValue(ChatModel.class);
                 chatModels.add(chatModel);
                 adapter.notifyDataSetChanged();
                *//** delete it*/
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get(primary_Id) != null) {

                        // String msg = map.get(primary_Id).toString();
                        chatModels.add(new ChatModel(map.get(primary_Id).toString(),null,/*R.drawable.ic_groupfrinds,*/primary_Id));

                    }
                    else {

                        chatModels.add(new ChatModel(map.get(Friend_key).toString(),imageid,/*R.drawable.ic_groupfrinds,*/Friend_key));

                    }

                    //chatModels.add(new ChatModel("",/*R.drawable.ic_groupfrinds,*/primary_Id));
                }

               /* adapter=new Chat_RecyclerViewAdapter(ChatActivity.this,chatModels);
                recyclerView.setAdapter(adapter);*/
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chatModels.size()-1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    /*** FRIEND MESSAGE*/
    /*private void test_friendMessages()
    {
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Users").child(primary_Id).child("messages");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    friend_Messages();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }*/

    //Boolean finish=false;
    ArrayList<String>arrayList_friend=new ArrayList<String>();
    private void friend_Messages()
    {
        //you must verify if child messageTO exist first
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(primary_Id).child("messages").child(Friend_key);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    if(arrayList_friend.contains(dataSnapshot.getKey()))
                        return;
                    else {
                        arrayList_friend.add(dataSnapshot.getKey());
                        load_friend_Text(dataSnapshot.getKey());

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
              /*if(dataSnapshot.exists())
                  load_Text( dataSnapshot.getKey());*/
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





    private void load_friend_Text(String message)
    {

        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(primary_Id).child("messages_From")
                .child(Friend_key).child(message);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    chatModels.add(new ChatModel(dataSnapshot.child("Text").getValue().toString(),imageid,/*R.drawable.ic_groupfrinds,*/Friend_key));
                }

               /* adapter=new Chat_RecyclerViewAdapter(ChatActivity.this,chatModels);
                recyclerView.scrollToPosition(chatModels.size()-1);
                recyclerView.setAdapter(adapter);*/

                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chatModels.size()-1);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /** firend message*/

    Context mcontext;

}
