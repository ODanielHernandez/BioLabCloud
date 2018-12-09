package com.example.fragmento.biolabcloud;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CustomViewHolder> {

    class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public CustomViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.textMessage);
        }
    }

    List<ReponseMessage> ResponseMessageList;


    public MessageAdapter(List<ReponseMessage> ReponseMessageList) {
        this.ResponseMessageList = ReponseMessageList;
    }

    @Override
    public int getItemViewType(int position) {
        if(ResponseMessageList.get(position).isMe()){
            return R.layout.me_bubble;
        }else{
            return R.layout.bot_bubble;
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.CustomViewHolder holder, int position) {
        holder.textView.setText(ResponseMessageList.get(position).getTextMessage());
    }

    @Override
    public int getItemCount() {
        return ResponseMessageList.size();
    }
}
