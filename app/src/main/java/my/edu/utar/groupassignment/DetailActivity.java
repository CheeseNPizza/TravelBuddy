package my.edu.utar.groupassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import my.edu.utar.groupassignment.databinding.ActivityMainBinding;
import my.edu.utar.groupassignment.structure.Place;

public class DetailActivity extends AppCompatActivity {

    private TextView text_name, text_address, text_rating, text_price_level, text_description;
    private Place place;
    private ImageView photo, return_btn;
    private String web_url;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        setVariable();

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        Button book_btn = findViewById(R.id.book_btn);
        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
    }

    private void setVariable() {
        place = (Place) getIntent().getSerializableExtra("object");

        text_name.setText(place.getName());
        text_address.setText(place.getAddress());
        text_rating.setText(place.getRating());
        text_price_level.setText(place.getPrice_level());
        text_description.setText(place.getDescription());

        Glide.with(this)
                .load(place.getPhoto())
                .into(photo);

        web_url = place.getWeb_url();

        return_btn.setOnClickListener(v -> finish());
    }

    private void initView() {
        text_name = findViewById(R.id.detail_name);
        text_address = findViewById(R.id.detail_address);
        text_rating = findViewById(R.id.detail_rating);
        text_price_level = findViewById(R.id.detail_price);
        text_description = findViewById(R.id.detail_description);
        photo = findViewById(R.id.detail_photo);
        return_btn = findViewById(R.id.return_btn);
    }

    private void show_dialog(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        WebView webView = new WebView(this);
        webView.loadUrl(web_url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(webView);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}