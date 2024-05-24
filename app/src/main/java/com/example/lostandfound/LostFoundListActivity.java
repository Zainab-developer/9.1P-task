package com.example.lostandfound;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class LostFoundListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLostFound;
    private Adapter adapter;
    private List<LostFoundItemsActivity> itemList;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        boolean itemRemoved = data.getBooleanExtra("ITEM_REMOVED", false);
                        int position = data.getIntExtra("ITEM_POSITION", -1);
                        if (itemRemoved && position != -1) {
                            adapter.removeItem(position);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found_list);


        Button btnBack = findViewById(R.id.btnBack);


        btnBack.setOnClickListener(v -> {

            Intent intent = new Intent(LostFoundListActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        });

        recyclerViewLostFound = findViewById(R.id.recyclerViewLostFound);
        recyclerViewLostFound.setLayoutManager(new LinearLayoutManager(this));

        itemList = loadItems();

        adapter = new Adapter(this, itemList, position -> {
            Intent intent = new Intent(LostFoundListActivity.this, ItemDetailsActivity.class);
            intent.putExtra("ITEM_TITLE", itemList.get(position).getTitle());
            intent.putExtra("ITEM_DESCRIPTION", itemList.get(position).getDescription());
            intent.putExtra("ITEM_PHONE", itemList.get(position).getPhone());
            intent.putExtra("ITEM_DATE", itemList.get(position).getDate());
            intent.putExtra("ITEM_LOCATION", itemList.get(position).getLocation());
            intent.putExtra("ITEM_POSITION", position);
            activityResultLauncher.launch(intent);
        });

        recyclerViewLostFound.setAdapter(adapter);
    }

    private List<LostFoundItemsActivity> loadItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("LostFoundPrefs", MODE_PRIVATE);
        String itemsJson = sharedPreferences.getString("items", "[]");
        Type type = new TypeToken<ArrayList<LostFoundItemsActivity>>(){}.getType();
        return new Gson().fromJson(itemsJson, type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        itemList.addAll(loadItems());
        adapter.notifyDataSetChanged();
    }
}
