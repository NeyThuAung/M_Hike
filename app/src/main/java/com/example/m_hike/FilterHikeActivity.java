package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.m_hike.adapter.HikeAdapter;
import com.example.m_hike.database.DatabaseHelper;
import com.example.m_hike.databinding.ActivityFilterHikeBinding;
import com.example.m_hike.model.Hike;

import java.util.ArrayList;

public class FilterHikeActivity extends AppCompatActivity {

    private ActivityFilterHikeBinding binding;

    DatabaseHelper databaseHelper;
    AlertDialog.Builder builder;

    ArrayList<Hike> hikeList = new ArrayList<>();
    HikeAdapter hikeAdapter;
    String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterHikeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.mbFilterHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FilteredHikeListActivity.class);
                intent.putExtra("filter_hike_name",binding.etHikeName.getText().toString());
                intent.putExtra("filter_hike_location",binding.etLocation.getText().toString());
                intent.putExtra("filter_hike_date",binding.etDate.getText().toString());
                startActivity(intent);
            }
        });
    }
}