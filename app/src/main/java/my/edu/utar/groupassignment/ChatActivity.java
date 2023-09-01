package my.edu.utar.groupassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import my.edu.utar.groupassignment.adapter.MessageAdapter;
import my.edu.utar.groupassignment.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    String receiverId;
    DatabaseReference dbReferenceS, dbReferenceR;
    String senderRoom, receiverRoom;
    MessageAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        receiverId = getIntent().getStringExtra("id");

        senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
        receiverRoom = receiverId + FirebaseAuth.getInstance().getUid();

        msgAdapter = new MessageAdapter(this);
        binding.msgRecycler.setAdapter(msgAdapter);
        binding.msgRecycler.setLayoutManager(new LinearLayoutManager(this));

        dbReferenceS = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        dbReferenceR = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);

        dbReferenceS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgAdapter.clear();
                for(DataSnapshot datasnap:snapshot.getChildren()){
                    MessageModel msgModel = datasnap.getValue(MessageModel.class);
                    msgAdapter.add(msgModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendMsg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String message = binding.inputMsg.getText().toString();
                if(message.trim().length() > 0){
                    sendMessage(message);
                }
                binding.inputMsg.setText("");
            }
        });
    }

    private void sendMessage(String message) {
        String msgId = UUID.randomUUID().toString();
        MessageModel msgModel = new MessageModel(msgId, FirebaseAuth.getInstance().getUid(),message);
        msgAdapter.add(msgModel);
        dbReferenceS.child(msgId).setValue(msgModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ChatActivity.this,"Message sent", Toast.LENGTH_SHORT).show();
            }
        });
        dbReferenceR.child(msgId).setValue(msgModel);

    }
}