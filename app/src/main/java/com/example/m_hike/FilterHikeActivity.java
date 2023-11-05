package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

                if (!binding.etHikeName.getText().toString().isEmpty() || !binding.etLocation.getText().toString().isEmpty() || !binding.etDate.getText().toString().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), FilteredHikeListActivity.class);
                    intent.putExtra("filter_hike_name", binding.etHikeName.getText().toString());
                    intent.putExtra("filter_hike_location", binding.etLocation.getText().toString());
                    intent.putExtra("filter_hike_date", binding.etDate.getText().toString());
                    startActivity(intent);
                }else {

                    Toast.makeText(FilterHikeActivity.this, "One of filter field is required. \n Enter required filter field.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void checkValidation() {

        if (binding.etHikeName.getText().toString().length() == 0) {
            binding.tilHikeName.setErrorEnabled(true);
            binding.tilHikeName.setError("Name of hike is required.");
        } else {
            binding.tilHikeName.setErrorEnabled(false);
        }

        if (binding.etLocation.getText().toString().length() == 0) {
            binding.tilLocation.setErrorEnabled(true);
            binding.tilLocation.setError("Location is required.");
        } else {
            binding.tilLocation.setErrorEnabled(false);
        }

    }
}