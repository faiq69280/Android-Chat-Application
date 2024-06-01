package com.example.firebasedemo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConvoViewHolder>{
    public ArrayList<ConversationItem> dataSet;
    public interface ConversationClickListener{
        public void onClick(ConversationItem c);
    }


    public ConversationClickListener listener;

    public void changeDataSet(ArrayList<ConversationItem> c){
            this.dataSet = c;
            notifyDataSetChanged();
    }
    public ConversationAdapter(ArrayList<ConversationItem> data, ConversationClickListener c){
        dataSet = data;
        listener = c;
    }
    public class ConvoViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public Button profile;
        public ConvoViewHolder(View v){
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            profile = (Button) v.findViewById(R.id.profile);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) v.getTag();
                    listener.onClick(dataSet.get(pos));
                }
            });
        }
    }

    public ConvoViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.convoitem, parent, false);

        ConvoViewHolder vh = new ConvoViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(ConvoViewHolder holder, int position) {
        String content = dataSet.get(position).getName();
        //int eol = content.indexOf("\n");
        holder.name.setText(content);
        holder.profile.setText(content.substring(0,1));
        holder.itemView.setTag(position);

    }
    public int getItemCount() {
        return dataSet.size();
    }






}
