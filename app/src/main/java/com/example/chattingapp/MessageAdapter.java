package com.example.chattingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private OnDeleteClickListener onDeleteClickListener;
    List<ModelClass> list;
    boolean status;
    int send;
    int receive;
    String userName;
    private Context context; // Add this member variable

    public MessageAdapter(List<ModelClass> list, String userName) {

        this.list = list;
        this.userName = userName;
        status = false;
        send = 1;
        receive = 2;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if(viewType==send){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_send, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ModelClass message = list.get(position);
        holder.textView.setText(list.get(position).getMessage());
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MessageViewHolder  extends RecyclerView.ViewHolder{

        TextView textView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            if(status){
                textView = itemView.findViewById(R.id.textViewSend);
            }
            else{
                textView = itemView.findViewById(R.id.textViewReceived);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getFrom().equals(userName)){
            status = true;
            return send;
        }else{
            status = false;
            return receive;
        }

    }

    public interface OnDeleteClickListener {
        void onDeleteClick(ModelClass message);
    }
}



//
//package com.bharath.El_chat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
//
//    List<ModelClass> list;
//    boolean status;
//    int send;
//    int receive;
//    String userName;
//
//    public MessageAdapter(List<ModelClass> list, String userName) {
//        this.list = list;
//        this.userName = userName;
//        status = false;
//        send = 1;
//        receive = 2;
//    }
//
//    @NonNull
//    @Override
//    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View view;
//        if(viewType==send){
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_send, parent, false);
//        }
//        else{
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_received, parent, false);
//        }
//        return new MessageViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
//        holder.textView.setText(list.get(position).getMessage());
//        ModelClass message = list.get(position);
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                String s = message.getId();
//
//                MyChatActivity m = new MyChatActivity();
//                m.deleteMessage(s);
//
//                return true;
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public class MessageViewHolder  extends RecyclerView.ViewHolder{
//
//        TextView textView;
//
//        public MessageViewHolder(@NonNull View itemView) {
//            super(itemView);
//            if(status){
//                textView = itemView.findViewById(R.id.textViewSend);
//            }
//            else{
//                textView = itemView.findViewById(R.id.textViewReceived);
//            }
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if(list.get(position).getFrom().equals(userName)){
//            status = true;
//            return send;
//        }else{
//            status = false;
//            return receive;
//        }
//
//    }
//}
//
