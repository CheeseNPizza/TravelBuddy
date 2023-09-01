package my.edu.utar.groupassignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import my.edu.utar.groupassignment.HelperClass;
import my.edu.utar.groupassignment.MessageModel;
import my.edu.utar.groupassignment.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{


    private Context context;
    private ArrayList<MessageModel> messageList;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        MessageModel messageModel = messageList.get(position);
        if (messageModel != null) {
            holder.message.setText(messageModel.getMessage());
        }
        if (messageModel.getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.send));
        }
        else{
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.receive));
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView message;
        private LinearLayout main;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            main = itemView.findViewById(R.id.messageLayout);
        }
    }

    public void setFilteredList(ArrayList<MessageModel> filteredList){
        this.messageList = filteredList;
        notifyDataSetChanged();
    }
}
