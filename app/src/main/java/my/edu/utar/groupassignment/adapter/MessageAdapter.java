package my.edu.utar.groupassignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import my.edu.utar.groupassignment.MessageModel;
import my.edu.utar.groupassignment.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{


    private Context context;
    private ArrayList<MessageModel> messageList;
    private Date messageDate;
    private String formatDate;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
    private static final int VIEW_TYPE_INCOMING = 0;
    private static final int VIEW_TYPE_OUTGOING = 1;

    public MessageAdapter(Context context){
        this.context = context;
        this.messageList = new ArrayList<>();
    }

    public void add(MessageModel messageModel){
        messageList.add(messageModel);
        notifyDataSetChanged();
    }

    public void clear(){
        messageList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_INCOMING) {
            // Inflate the layout for incoming messages
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inmessage_row, parent, false);
        } else {
            // Inflate the layout for outgoing messages
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outmessage_row, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        MessageModel messageModel = messageList.get(position);
        if (messageModel != null) {
            if(messageModel.getSenderId().equals(FirebaseAuth.getInstance().getUid())){
                holder.out_message.setText(messageModel.getMessage());
                messageDate = new Date(messageModel.getTimestamp());
                formatDate = sdf.format(messageDate);
                holder.out_dt.setText(formatDate);
            }
            else{
                holder.in_message.setText(messageModel.getMessage());
                messageDate = new Date(messageModel.getTimestamp());
                formatDate = sdf.format(messageDate);
                holder.in_dt.setText(formatDate);
            }
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView in_message, out_message, in_dt, out_dt;
        private ConstraintLayout in_main, out_main;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            in_message = itemView.findViewById(R.id.in_message);
            in_main = itemView.findViewById(R.id.in_messageLayout);
            in_dt = itemView.findViewById(R.id.in_dt);
            out_message = itemView.findViewById(R.id.out_message);
            out_main = itemView.findViewById(R.id.out_messageLayout);
            out_dt = itemView.findViewById(R.id.out_dt);
        }
    }

    public void setFilteredList(ArrayList<MessageModel> filteredList){
        this.messageList = filteredList;
        notifyDataSetChanged();
    }

    public int getItemViewType(int position) {
        MessageModel messageModel = messageList.get(position);
        if (messageModel != null) {
            // Determine if it's an incoming or outgoing message
            if (messageModel.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
                return VIEW_TYPE_OUTGOING;
            } else {
                return VIEW_TYPE_INCOMING;
            }
        }
        return super.getItemViewType(position);
    }
}
