package my.edu.utar.groupassignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import my.edu.utar.groupassignment.adapter.MessageAdapter;
import my.edu.utar.groupassignment.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityChatBinding binding;
    private String receiverId;
    private DatabaseReference dbReference, dbReferenceS, dbReferenceR;
    private String senderRoom, receiverRoom;
    private MessageAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Handle the camera result here
                            Intent data = result.getData();
                            if (data != null) {
                                // The captured image can be found in the 'data' intent
                                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");

                                binding.inputMsg.setEnabled(false);
                                binding.photoCaptured.setVisibility(View.VISIBLE);
                                binding.photoCaptured.setImageBitmap(capturedImage);

                            }
                        }
                    }
                }
        );

        receiverId = getIntent().getStringExtra("id");
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        dbReference.child(receiverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HelperClass receiverModel = dataSnapshot.getValue(HelperClass.class);
                    binding.ChatName.setText(receiverModel.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
        receiverRoom = receiverId + FirebaseAuth.getInstance().getUid();

        msgAdapter = new MessageAdapter(this);
        binding.msgRecycler.setAdapter(msgAdapter);
        binding.msgRecycler.setLayoutManager(new LinearLayoutManager(this));

        dbReferenceS = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        Query queryS = dbReferenceS.orderByChild("timestamp");
        dbReferenceR = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);


        queryS.addValueEventListener(new ValueEventListener() {
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
                Bitmap image;
                if(binding.inputMsg.isEnabled()){
                    if(message.trim().length() > 0){
                        sendMessage(message);
                    }
                }
                else{
                    if (binding.photoCaptured.getDrawable() instanceof BitmapDrawable) {
                        BitmapDrawable drawable = (BitmapDrawable) binding.photoCaptured.getDrawable();
                        image = drawable.getBitmap();
                        sendMessage(message);
                    }
                }
                binding.inputMsg.setText("");
            }
        });

        binding.takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[] {Manifest.permission.CAMERA}, 101);
                }

                binding.takePhoto.setClickable(false);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(intent);
            }
        });



    }

    private void sendMessage(String message) {
        String msgId = UUID.randomUUID().toString();
        MessageModel msgModel = new MessageModel(msgId, FirebaseAuth.getInstance().getUid(),message, receiverId);
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