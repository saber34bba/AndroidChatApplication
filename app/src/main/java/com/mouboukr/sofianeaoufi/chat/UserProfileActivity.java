package com.mouboukr.sofianeaoufi.chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {
    ImageView Image;
    TextView name,friends_number,friendShip_request_number,email,send_number_txt;
    private Uri Uuri;
    private Button save_btn;
    String user_id;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask storageTask;
    private static final int request=1;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       progress=new ProgressDialog(this);
         progress.setMessage("be patient until finish loading information");
          progress.setCancelable(false);
          progress.show();


        setContentView(R.layout.activity_user_profile);
        Image=(ImageView)findViewById(R.id.userImageView);
        save_btn=(Button)findViewById(R.id.save);
         name=(TextView)findViewById(R.id.user_name_txtView);
         email=(TextView)findViewById(R.id.email_txtView);
       friends_number=(TextView)findViewById(R.id.friendNumber_txtView);
       friendShip_request_number=(TextView)findViewById(R.id.friendRequestNumber_txtView) ;
       send_number_txt=(TextView)findViewById(R.id.frd_send_txtNumber);

        Toolbar toolbar=(Toolbar)findViewById(R.id.user_profile_toolbar_id);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_return_arrow);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });






         user_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        getFriendShipRequestNumber();
        get_friendShip_sendNumber();
        getFriendsNumber();
         listener();
    }




    public void click(View view) {
        Intent i= new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_PICK);
        startActivityForResult(i,1);
    }




    private Uri resuUri;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode== Activity.RESULT_OK){
            final Uri imageUri=data.getData();
            resuUri=imageUri;
            Image.setImageURI(resuUri);
            /*if(resuUri != null){

                StorageReference mstorageReference= FirebaseStorage.getInstance().getReference().child("Image").child(mDriver_primary_key);
                Bitmap mbitmap=null;
                try {
                    mbitmap= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resuUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream mbyteArrayOutputStream=new ByteArrayOutputStream();
                mbitmap.compress(Bitmap.CompressFormat.JPEG,20,mbyteArrayOutputStream);
                byte[]mdata = mbyteArrayOutputStream.toByteArray();
                UploadTask muploadTask =mstorageReference.putBytes(mdata);
muploadTask.addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        finish();
        return;
    }
});

                muploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri dowUri=taskSnapshot.getDownloadUrl();

                        Map newImage=new HashMap();
                        newImage.put("profileImage",dowUri.toString());
                        DatabaseReference dbr= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(mDriver_primary_key);
                          dbr.updateChildren(newImage);
                          finish();
                          return;

                    }
                });

            }else {
                finish();
            }*/
        }


    }




    private void get_friendShip_sendNumber()
    {
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("friendship Requsest to");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                  int i=  (int) dataSnapshot.getChildrenCount();
                   send_number_txt.setText(String.valueOf(i));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



    public void saveInfo(View view) {
        if(resuUri != null){

            StorageReference mstorageReference= FirebaseStorage.getInstance().getReference().child("Image").child(user_id);
            Bitmap mbitmap=null;

            try {
                mbitmap= MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resuUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream mbyteArrayOutputStream=new ByteArrayOutputStream();
            mbitmap.compress(Bitmap.CompressFormat.JPEG,20,mbyteArrayOutputStream);
            byte[]mdata = mbyteArrayOutputStream.toByteArray();
            UploadTask muploadTask =mstorageReference.putBytes(mdata);
            muploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                    return;

                    //delete finich and use a snackBar
                }
            });

            muploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri dowUri=taskSnapshot.getDownloadUrl();

                    java.util.Map newImage=new HashMap();
                    newImage.put("profileImage",dowUri.toString());
                    DatabaseReference dbr= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                    dbr.updateChildren(newImage);
                    finish();
                    return;

                }
            });

        }else {
            finish();
            // delete finish and add snackBar
        }
    }



 private void listener()
 {
     DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
     reference.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists())
               {
                   Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                   if (map.get("profileImage") != null) {
                       String image=map.get("profileImage").toString();
                       Glide.with(getApplication()).load(image).apply(RequestOptions.circleCropTransform()).into(Image);

                   }
                   if (map.get("name") != null){

                       name.setText(map.get("name").toString());

                   }
                   if(map.get("email")!=null)
                       email.setText(map.get("email").toString());



               }
       progress.dismiss();
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });

 }


 private void getFriendsNumber()
 {

     final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Friends List");
     databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
               String str= String.valueOf(dataSnapshot.getChildrenCount());
               friends_number.setText(str);
             //if number=1 setText(number+"friend")
             //else setText(number+"friends")



         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });

 }


 private void getFriendShipRequestNumber()
 {
     DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("friendship Requsest from");
     databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
             String str= String.valueOf(dataSnapshot.getChildrenCount());
             friendShip_request_number.setText(str);


         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });



 }






}
