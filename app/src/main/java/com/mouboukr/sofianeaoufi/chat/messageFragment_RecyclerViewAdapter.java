package com.mouboukr.sofianeaoufi.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;

import java.util.List;



public class messageFragment_RecyclerViewAdapter extends RecyclerView.Adapter<messageFragment_RecyclerViewAdapter.MyViewHolder> {
    Context mcontext;
    List<Groups> mData;
    Dialog mdialog;

    public messageFragment_RecyclerViewAdapter(Context mcontext, List<Groups> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
    }

    @Override
    public messageFragment_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v;
        v= LayoutInflater.from(mcontext).inflate(R.layout.item_friends_group,parent,false);
        final messageFragment_RecyclerViewAdapter.MyViewHolder viewHolder=new messageFragment_RecyclerViewAdapter.MyViewHolder(v);


        mdialog=new Dialog(mcontext);
        mdialog.setContentView(R.layout.dialog_friends_profile);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));


        viewHolder.linla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String profileID=mData.get(viewHolder.getAdapterPosition()).getPrimaryKey();
                //btn.setText((CharSequence) mData.get(viewHolder.getAdapterPosition()).getPrimaryKey());
                //Intent myIntent = new Intent(mcontext, ShowProfileSelectedActivity.class);
                Intent myIntent = new Intent(mcontext, ChatActivity.class);
                myIntent.putExtra("key",profileID);
                mcontext.startActivity(myIntent);






            }
        });




        return viewHolder;
    }

    @Override
    public void onBindViewHolder(messageFragment_RecyclerViewAdapter.MyViewHolder holder, int position) {


        holder.txt_Name.setText(mData.get(position).getName());
        holder.txt_Status.setText(mData.get(position).getStatu());
        //holder.img_groupRecyclerClass.setImageResource(mData.get(position).getPhoto());
        if(mData.get(position).getPhoto()!=null)
            Glide.with(mcontext).load(mData.get(position).getPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.img_groupRecyclerClass);
       else {
            holder.img_groupRecyclerClass.setImageResource(R.drawable.person_bkg);

        }
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


        public MyViewHolder(View itemView) {

            super(itemView);
            online=(ImageView)itemView.findViewById(R.id.onlineImage);
            linla=(LinearLayout)itemView.findViewById(R.id.itemFriends_group_linearLayout);
            txt_Name=(TextView)itemView.findViewById(R.id.name_friendGroup_id);
            txt_Status=(TextView)itemView.findViewById(R.id.status_friendGroup_id);
            img_groupRecyclerClass=(ImageView)itemView.findViewById(R.id.img_friendsGroup_id);


        }
    }
}
