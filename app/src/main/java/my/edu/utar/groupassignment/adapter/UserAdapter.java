package my.edu.utar.groupassignment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import my.edu.utar.groupassignment.ChatActivity;
import my.edu.utar.groupassignment.DataClass;
import my.edu.utar.groupassignment.HelperClass;
import my.edu.utar.groupassignment.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{


    private Context context;
    private ArrayList<HelperClass> userList;

    public UserAdapter(Context context, ArrayList<HelperClass> dataList){
        this.context = context;
        this.userList = dataList;
    }

    public void add(HelperClass helperClass){
        userList.add(helperClass);
        notifyDataSetChanged();
    }

    public void clear(){
        userList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        HelperClass userModel = userList.get(position);
        if (userModel != null) {
            holder.textName.setText(userModel.getName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id",userModel.getUserID());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView textName;
        private CircleImageView profileImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.userName);
            profileImg = itemView.findViewById(R.id.userPhoto);
        }
    }

    public void setFilteredList(ArrayList<HelperClass> filteredList){
        this.userList = filteredList;
        notifyDataSetChanged();
    }
}
