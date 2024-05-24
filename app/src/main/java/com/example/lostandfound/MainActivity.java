package com.example.lostandfound;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends Activity {

    private Button CreateAdvert;
    private Button ShowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateAdvert = findViewById(R.id.CreateAdvert);
        ShowItems = findViewById(R.id.ShowItems);

        CreateAdvert.setOnClickListener(v -> {
            Intent intentCreateAdvert = new Intent(MainActivity.this, CreateAdvertActivity.class);
            startActivity(intentCreateAdvert);
        });

        ShowItems.setOnClickListener(v -> {
            Intent intentShowItems = new Intent(MainActivity.this, LostFoundListActivity.class);
            startActivity(intentShowItems);
        });
    }
}