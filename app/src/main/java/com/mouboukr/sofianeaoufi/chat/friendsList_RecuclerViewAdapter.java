package com.mouboukr.sofianeaoufi.chat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Acer on 2018-03-22.
 */

public class friendsList_RecuclerViewAdapter extends RecyclerView.Adapter<friendsList_RecuclerViewAdapter.MyViewHolder> {
    Context mcontext;
    List<Groups> mData;
    Dialog mdialog;
    ProgressDialog progressDialog;

    public friendsList_RecuclerViewAdapter(Context mcontext, List<Groups> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
    }
    int nb=0 ;

    @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            View v;
            v= LayoutInflater.from(mcontext).inflate(R.layout.item_friends_group,parent,false);
            final MyViewHolder viewHolder=new MyViewHolder(v);


        mdialog=new Dialog(mcontext);
        mdialog.setContentView(R.layout.dialog_friends_profile);
        //mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       mdialog.getWindow().setBackgroundDrawableResource(R.drawable.start_menu_background);

     viewHolder.linla.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             nb=0;
             progressDialog=new ProgressDialog(mcontext);

             progressDialog.setMessage("be patient please Loading...");
             progressDialog.show();

             final TextView number=(TextView)mdialog.findViewById(R.id.frNbr_txtView);

             DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(mData.get(viewHolder.getAdapterPosition()).getPrimaryKey()).child("Friends List");
             databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     if(dataSnapshot.exists()) {
                         nb = (int) dataSnapshot.getChildrenCount();
                         return;

                     }
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });


             TextView tname=(TextView)mdialog.findViewById(R.id.dialog_user_profileName_textView);
              number.setText(String.valueOf(nb));

             ImageView imageView=(ImageView)mdialog.findViewById(R.id.dialog_imageView);
             if(mData.get(viewHolder.getAdapterPosition()).getPhoto()!=null)
                 Glide.with(mcontext).load(mData.get(viewHolder.getAdapterPosition()).getPhoto()).apply(RequestOptions.circleCropTransform()).into(imageView);
             else
                 imageView.setImageResource(R.drawable.person_bkg);

             tname.setText(mData.get(viewHolder.getAdapterPosition()).getEmail());
             mdialog.setTitle("About "+tname.getText().toString());



             final Button btn=(Button)mdialog.findViewById(R.id.dialog_friend_Send_Requst_Button);
             final Button send_msg_btn=(Button)mdialog.findViewById(R.id.sendMessage_btn);
        progressDialog.dismiss();
             send_msg_btn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     String profileID=mData.get(viewHolder.getAdapterPosition()).getPrimaryKey();
                     //btn.setText((CharSequence) mData.get(viewHolder.getAdapterPosition()).getPrimaryKey());
                     Intent myIntent = new Intent(mcontext, ChatActivity.class);
                     myIntent.putExtra("key",profileID);
                     mcontext.startActivity(myIntent);
                     mdialog.dismiss();                 }
             });

             mdialog.show();




         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String profileID=mData.get(viewHolder.getAdapterPosition()).getPrimaryKey();
                 //btn.setText((CharSequence) mData.get(viewHolder.getAdapterPosition()).getPrimaryKey());
                 Intent myIntent = new Intent(mcontext, ShowProfileSelectedActivity.class);
                 myIntent.putExtra("key",profileID);
                 mcontext.startActivity(myIntent);
                 mdialog.dismiss();

             }
         });


         }
     });




        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(mData.get(position).getEmail()!=null)
        {

        }

        holder.txt_Name.setText(mData.get(position).getName());
        if(mData.get(position).getStatu()==null)
        {
            holder.txt_Status.setText("");
        }
        else//8juin
        holder.txt_Status.setText(mData.get(position).getStatu());
        //holder.img_groupRecyclerClass.setImageResource(mData.get(position).getPhoto());
        if(mData.get(position).getPhoto()!=null)
            Glide.with(mcontext).load(mData.get(position).getPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.img_groupRecyclerClass);
        if(mData.get(position).getPhoto()==null)
            holder.img_groupRecyclerClass.setImageResource(R.drawable.person_bkg);

        if(mData.get(position).getaBoolean()!=null)
        {
            if(mData.get(position).getaBoolean()==true)
            {
                holder.online.setVisibility(View.VISIBLE);
            }
            else
                holder.online.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    //static class
    public static class MyViewHolder extends RecyclerView.ViewHolder{
            private LinearLayout linla;
            private TextView txt_Name;
            private TextView txt_Status;
            private ImageView img_groupRecyclerClass;
            private  ImageView online;
             private TextView number;

        public MyViewHolder(View itemView) {

            super(itemView);
            linla=(LinearLayout)itemView.findViewById(R.id.itemFriends_group_linearLayout);
          txt_Name=(TextView)itemView.findViewById(R.id.name_friendGroup_id);
          txt_Status=(TextView)itemView.findViewById(R.id.status_friendGroup_id);
          img_groupRecyclerClass=(ImageView)itemView.findViewById(R.id.img_friendsGroup_id);
          online=(ImageView)itemView.findViewById(R.id.onlineImage);
         number=(TextView)itemView.findViewById(R.id.frNbr_txtView);

        }
    }






     public void addItem()
    {

      mData.add(new Groups());
      notifyItemChanged(getItemCount());

    }



}
