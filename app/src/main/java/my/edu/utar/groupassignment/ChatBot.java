//code by thong wei xin
//chatbot
//call ChatGPT API to act as a chatbot
package my.edu.utar.groupassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import my.edu.utar.groupassignment.databinding.ActivityChatBotBinding;

public class ChatBot extends AppCompatActivity {
    private ActivityChatBotBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.botBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}