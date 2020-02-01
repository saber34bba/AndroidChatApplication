package com.mouboukr.sofianeaoufi.chat;

import android.support.design.widget.Snackbar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SeachrForPeopleActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText editText;
    Button btn;
  friendsList_RecuclerViewAdapter  adapter;
    String userID;
    private List<Groups> lstFriendGroups;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seachr_for_people);

        editText=(EditText)findViewById(R.id.search_edtTxt);
        btn=(Button) findViewById(R.id.search_btn);
        recyclerView=(RecyclerView)findViewById(R.id.search_RecyclerView);
        userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
            lstFriendGroups=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }
    private Boolean isSearch=false;

String searchFor;


   // private List<ChatModel> chatModels=new ArrayList<>();
        int number=0;
        String name,email,status;
    private void loadUsers(final String id) {
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    number++;

                    if(dataSnapshot.child("name").getValue().equals(searchFor))
                    {
                        if(dataSnapshot.child("status").exists())
                           status= dataSnapshot.child("status").getValue().toString();
                        name= dataSnapshot.child("name").getValue().toString();
                      email=dataSnapshot.child("email").getValue().toString();
                        if(dataSnapshot.child("profileImage").exists())
                        {
                            if (dataSnapshot.child("profileImage") != null)
                            {
                                lstFriendGroups.add(new Groups(email,name,status,dataSnapshot.child("profileImage").getValue().toString(),id,false));

                            }

                        }
                        else {
                            lstFriendGroups.add(new Groups(email,name,status,null,id,false));

                        }
                    }


                   /* if(dataSnapshot.child("profileImage").exists())
                    {
                        if (dataSnapshot.child("profileImage") != null)
                        {
                            lstFriendGroups.add(new Groups(email,name,status,dataSnapshot.child("profileImage").getValue().toString(),id,false));

                        }

                    }
                    else {
                        lstFriendGroups.add(new Groups(email,name,status,null,id,false));

                    }*/

                }


                /*if(ChildNumber==number) {
                    View view = findViewById(R.id.search_lay);


                    final Snackbar snack = Snackbar.make(view, "No account with " + searchFor + " infomation ", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    return;
                                }
                            });
                    snack.show();
                }*/



                adapter=new friendsList_RecuclerViewAdapter(SeachrForPeopleActivity.this,lstFriendGroups);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void search(View view)
    {
        number=0;
        if(lstFriendGroups.size()>0)
        {
            lstFriendGroups.clear();
            recyclerView.removeAllViewsInLayout();

        }
        if(TextUtils.isEmpty(editText.getText().toString().trim()))
        {

            return;
        }
        else
        {

            searchFor =editText.getText().toString();

            final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        ChildNumber= (int) dataSnapshot.getChildrenCount();

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
    }
int ChildNumber;
   int n=0;

}
