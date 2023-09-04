package my.edu.utar.groupassignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import my.edu.utar.groupassignment.adapter.MessageAdapter;
import my.edu.utar.groupassignment.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityChatBinding binding;
    private String receiverId;
    private DatabaseReference dbReference, dbReferenceS, dbReferenceR;
    private String senderRoom, receiverRoom;
    private String imageURI;
    private MessageAdapter msgAdapter;
    private StorageReference storageReference;

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
                                binding.photoCaptured.setImageBitmap(capturedImage);

                                binding.inputMsg.setEnabled(false);
                                binding.photoCaptured.setVisibility(View.VISIBLE);

                            }
                        }
                    }
                }
        );

        receiverId = getIntent().getStringExtra("id");
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
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

        dbReferenceS = FirebaseDatabase.getInstance().getReference("Chats").child(senderRoom);
        Query queryS = dbReferenceS.orderByChild("timestamp");
        dbReferenceR = FirebaseDatabase.getInstance().getReference("Chats").child(receiverRoom);


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


        //Define a listener to delete conversation
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and show a confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete this Conversation?Once deleted, the data cannot be recovered.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference nodeToDeleteRef;
                                // User clicked "Yes," perform the action
                                if(FirebaseAuth.getInstance().getUid() != receiverId){
                                    // Create a reference to the specific node using the child() method
                                    nodeToDeleteRef = dbReferenceS;
                                }
                                else{
                                    // Create a reference to the specific node using the child() method
                                    nodeToDeleteRef = dbReferenceR;
                                }
                                nodeToDeleteRef.removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ChatActivity.this,"Successfully Deleted", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle the error
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked "No," cancel the action
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        //Define a listener to send message
        binding.sendMsg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String message = binding.inputMsg.getText().toString();
                Bitmap image = drawableToBitmap(binding.photoCaptured.getDrawable());
                if(message.trim().length() > 0 || image != null){
                        sendMessage(message, image);
                }

                binding.photoCaptured.setImageResource(0);
                binding.inputMsg.setText("");
            }
        });

        //Define a listener for taking photo icon
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

    //define a method to add message to Firebase
    private void sendMessage(String message, Bitmap image) {
        String msgId = UUID.randomUUID().toString();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + ".JPEG");
        imageReference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get the download URL of the uploaded image
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        // Handle the download URL
                        imageURI = downloadUri.toString();

                        MessageModel msgModel = new MessageModel(msgId, FirebaseAuth.getInstance().getUid(), message, imageURI, receiverId);
                        msgAdapter.add(msgModel);
                        dbReferenceS.child(msgId).setValue(msgModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ChatActivity.this,"Message sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dbReferenceR.child(msgId).setValue(msgModel);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this,"Upload failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Define a method to convert a Drawable to a Bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}