package com.example.firebasedemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatMsgViewHolder> {
    public ArrayList<ChatMsg> mDataset;
    public class ChatMsgViewHolder extends RecyclerView.ViewHolder{
        public TextView sender_name;
        public TextView timestamp;
        public TextView msg_content;
        public Button user_profile_icon;
        public ChatMsgViewHolder(View v) {
            super(v);
            sender_name = (TextView) v.findViewById(R.id.name);
            timestamp = (TextView) v.findViewById(R.id.timestamp);
            msg_content = (TextView) v.findViewById(R.id.msg_content);
            user_profile_icon = (Button) v.findViewById(R.id.profile);
        }

    }
    public ChatAdapter(ArrayList<ChatMsg> chat_msgs){
        mDataset = chat_msgs;
    }

    @Override
    public int getItemViewType(int position){
        if(mDataset.get(position).getType().toLowerCase().equals("me"))
            return 0;
        else
            return 1;
    }

    @Override
    public ChatMsgViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v;
        // create a new view
        if(viewType == 0) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.msg_item_sender, parent, false);
        }
        else{
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.msg_item_reciever, parent, false);
        }

        ChatMsgViewHolder vh = new ChatMsgViewHolder(v);
        return vh;

    }
    public void changeData(ArrayList<ChatMsg> msgs){
        this.mDataset = msgs;
        this.notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(ChatMsgViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String content = mDataset.get(position).getContent();
        holder.msg_content.setText(content);
        holder.sender_name.setText(mDataset.get(position).getSenderName());
        holder.timestamp.setText(mDataset.get(position).getTimeStamp());
        holder.user_profile_icon.setTag(position);
        holder.user_profile_icon.setText(mDataset.get(position).getSenderName().substring(0,1));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
