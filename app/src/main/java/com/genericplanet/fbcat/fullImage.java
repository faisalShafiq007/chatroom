package com.genericplanet.fbcat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class fullImage extends AppCompatActivity {
    String URL;
    ImageView fullImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        getSupportActionBar().setTitle("Image");
        getSupportActionBar().hide();
        Intent intent=getIntent();
        URL=intent.getStringExtra("URL");
        fullImage=findViewById(R.id.fullImage);
        Picasso.get().load(URL).into(fullImage);

    }
}
