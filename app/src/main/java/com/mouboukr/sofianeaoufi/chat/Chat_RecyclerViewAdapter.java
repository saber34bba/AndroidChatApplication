package com.mouboukr.sofianeaoufi.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;



public class Chat_RecyclerViewAdapter extends RecyclerView.Adapter<Chat_RecyclerViewAdapter.MyViewHolder>{

    Context mcontext;
    List<ChatModel> mData;

    public Chat_RecyclerViewAdapter(Context mcontext, List<ChatModel> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
    }

    @Override
    public Chat_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v;
        v= LayoutInflater.from(mcontext).inflate(R.layout.mychat_layout,parent,false);
        final Chat_RecyclerViewAdapter.MyViewHolder viewHolder=new Chat_RecyclerViewAdapter.MyViewHolder(v);







        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Chat_RecyclerViewAdapter.MyViewHolder holder, int position) {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String received_ID=mData.get(position).getId();
        if(received_ID.equals(id))
        {
            holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
            holder.rightLayout.setVisibility(LinearLayout.GONE);

            holder.rightMsgTextView.setText(mData.get(position).getText());
           holder.rightMsgTextView.setTextColor(Color.WHITE);
            holder.imageView.setVisibility(View.GONE);
            /*holder.txt_Name.setTextColor(Color.WHITE);
            holder.txt_Name.setBackgroundColor(Color.BLUE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.txt_Name.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.txt_Name.setLayoutParams(params);
            holder.txt_Name.setText(mData.get(position).getText());
            holder.txt_Name.setTextSize(18);*/





        }
        else {
           /* holder.txt_Name.setBackgroundColor(Color.GRAY);
            holder.txt_Name.setTextColor(Color.WHITE);
            holder.txt_Name.setTextSize(18);
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)holder.txt_Name.getLayoutParams();
            param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.txt_Name.setLayoutParams(param);
            holder.txt_Name.setText(mData.get(position).getText());

*/
            holder.rightLayout.setVisibility(LinearLayout.VISIBLE);
            holder.rightMsgLayout.setVisibility(LinearLayout.GONE);

            holder.leftMsgTextView.setText(mData.get(position).getText());
            holder.leftMsgTextView.setTextColor(Color.WHITE);
            holder.imageView.setVisibility(View.VISIBLE);


            if(mData.get(position).getPhoto()!=null)
            {
                Glide.with(mcontext).load(mData.get(position).getPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.imageView);
            }
            else
            holder.imageView.setImageResource(R.drawable.person_bkg);


        }


        //holder.txt_Name.setText(mData.get(position).getText());
        //holder.img_groupRecyclerClass.setImageResource(mData.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    //static class
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_Name;
        private ImageView img_groupRecyclerClass;
        //LinearLayout leftMsgLayout;
        RelativeLayout rightLayout;
        LinearLayout rightMsgLayout;
        TextView leftMsgTextView;
        TextView rightMsgTextView;
        ImageView imageView;




        public MyViewHolder(View itemView) {

            super(itemView);
            //txt_Name=(TextView)itemView.findViewById(R.id.message_received_id);
            rightLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLay);
            rightMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_right_msg_layout);
            leftMsgTextView = (TextView) itemView.findViewById(R.id.chat_left_msg_text_view);
            rightMsgTextView = (TextView) itemView.findViewById(R.id.chat_right_msg_text_view);
            //img_groupRecyclerClass=(ImageView)itemView.findViewById(R.id.chat_friend_imageView);
             imageView=(ImageView)itemView.findViewById(R.id.imageView);

        }
    }



}
