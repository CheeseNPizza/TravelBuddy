/**
 * @author Lee Teck Junn
 * @version 1.0
 */
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

    // Declaring variables
    private TextView text_name, text_address, text_rating, text_price_level, text_description;
    private Place place;
    private ImageView photo, return_btn;
    private String web_url;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize UI elements
        initView();
        // Set data for UI elements based on intent data
        setVariable();

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Set a click listener for the "book_btn" button
        Button book_btn = findViewById(R.id.book_btn);
        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog(); // Show a dialog when the button is clicked
            }
        });
    }

    // Set data for UI elements based on intent data
    private void setVariable() {
        place = (Place) getIntent().getSerializableExtra("object");

        text_name.setText(place.getName());
        text_address.setText(place.getAddress());
        text_rating.setText(place.getRating());
        text_price_level.setText(place.getPrice_level());
        text_description.setText(place.getDescription());

        // Load an image using Glide library
        Glide.with(this)
                .load(place.getPhoto())
                .into(photo);

        web_url = place.getWeb_url();

        // Set a click listener for the "return_btn" button to finish the activity
        return_btn.setOnClickListener(v -> finish());
    }

    // Initialize UI elements
    private void initView() {
        text_name = findViewById(R.id.detail_name);
        text_address = findViewById(R.id.detail_address);
        text_rating = findViewById(R.id.detail_rating);
        text_price_level = findViewById(R.id.detail_price);
        text_description = findViewById(R.id.detail_description);
        photo = findViewById(R.id.detail_photo);
        return_btn = findViewById(R.id.return_btn);
    }

    // Show a dialog with a WebView to display a web page
    private void show_dialog(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        // Load a web page URL in the WebView
        WebView webView = new WebView(this);
        webView.loadUrl(web_url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                // Open links in the same WebView
                view.loadUrl(url); 
                return true;
            }
        });

        // Set the WebView as the content of the dialog
        alert.setView(webView);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog when the "Close" button is clicked
                dialog.dismiss();
            }
        });
        // Show the dialog
        alert.show();
    }
}
