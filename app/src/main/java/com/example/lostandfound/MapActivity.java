package com.example.lostandfound;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        SharedPreferences sharedPreferences = getSharedPreferences("LostFoundPrefs", MODE_PRIVATE);
        String itemsJson = sharedPreferences.getString("items", "[]");
        Type type = new TypeToken<ArrayList<LostFoundItemsActivity>>(){}.getType();
        ArrayList<LostFoundItemsActivity> itemList = new Gson().fromJson(itemsJson, type);

        if (!itemList.isEmpty()) {
            LostFoundItemsActivity firstItem = itemList.get(0);
            LatLng location = new LatLng(firstItem.getLatitude(), firstItem.getLongitude());
            float zoomLevel = 10.0f;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));

            for (LostFoundItemsActivity item : itemList) {
                location = new LatLng(item.getLatitude(), item.getLongitude());
                Marker marker = map.addMarker(new MarkerOptions().position(location).title(item.getTitle()));
                marker.setTag(item);
            }
            map.setOnMarkerClickListener(this);
        }
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        LostFoundItemsActivity item = (LostFoundItemsActivity) marker.getTag();
        if (item != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(item.getTitle());
            builder.setMessage("Description: " + item.getDescription() +
                    "\nPhone: " + item.getPhone() +
                    "\nDate: " + item.getDate() +
                    "\nLocation: " + item.getLocation());
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(this, "No details available", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}