package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.m_hike.adapter.HikeAdapter;
import com.example.m_hike.database.DatabaseHelper;
import com.example.m_hike.databinding.ActivitySearchHikeBinding;
import com.example.m_hike.model.Hike;

import java.util.ArrayList;

public class SearchHikeActivity extends AppCompatActivity implements HikeAdapter.RecyclerViewListener {

    private ActivitySearchHikeBinding binding;

    DatabaseHelper databaseHelper;
    AlertDialog.Builder builder;

    ArrayList<Hike> hikeList = new ArrayList<>();
    HikeAdapter hikeAdapter;
    String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchHikeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.etSearchHike.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.recHikeList.setVisibility(View.INVISIBLE);
                    binding.tvNoHIke.setVisibility(View.INVISIBLE);

                } else {
                    searchText = s.toString();
                    databaseHelper = new DatabaseHelper(getApplicationContext());
                    hikeList.clear();
                    hikeList = databaseHelper.getSearchHike(s.toString());

                    // Recycler
                    if (hikeList.size() != 0) {
                        binding.tvNoHIke.setVisibility(View.INVISIBLE);
                        binding.recHikeList.setVisibility(View.VISIBLE);
                        bindRecyclerView();
                    } else {
                        binding.tvNoHIke.setVisibility(View.VISIBLE);
                        binding.tvNoHIke.setText(R.string.no_hike_record_found);
                        binding.recHikeList.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void bindRecyclerView() {
        binding.recHikeList.setLayoutManager(new LinearLayoutManager(this));
        binding.recHikeList.setHasFixedSize(true);
        hikeAdapter = new HikeAdapter(this, hikeList, this);
        binding.recHikeList.setAdapter(hikeAdapter);
    }

    @Override
    public void hikeItemClick(int hikeId) {

        Intent intent = new Intent(this.getApplicationContext(), HikeDetailsActivity.class);
        intent.putExtra("hike_edit_id", hikeId);
        startActivity(intent);

    }

    @Override
    public void editHikeClick(int hikeId) {
        Intent intent = new Intent(this.getApplicationContext(), EditHikeActivity.class);
        intent.putExtra("hike_edit_id", hikeId);
        startActivity(intent);
    }

    @Override
    public void deleteHikeClick(int hikeId) {
        builder = new AlertDialog.Builder(SearchHikeActivity.this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Are you sure want to delete this hike record?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteHikeDetails(hikeId);
                Toast.makeText(getApplicationContext(), "Hike Id " + Integer.toString(hikeId) + " is successfully deleted.", Toast.LENGTH_SHORT).show();
                hikeList.clear();
                hikeList = databaseHelper.getSearchHike(searchText);

                if (hikeList.size() != 0) {
                    binding.tvNoHIke.setVisibility(View.INVISIBLE);
                    binding.recHikeList.setVisibility(View.VISIBLE);
                    bindRecyclerView();
                } else {
                    binding.tvNoHIke.setVisibility(View.VISIBLE);
                    binding.tvNoHIke.setText(R.string.no_hike_record_found);
                    binding.recHikeList.setVisibility(View.INVISIBLE);
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void addObservation(int hikeId) {
        Intent intent = new Intent(getApplicationContext(), AddObservationActivity.class);
        intent.putExtra("hike_edit_id", hikeId);
        startActivity(intent);
    }

    @Override
    public void showObservationList(int hikeId) {
        Intent intent = new Intent(getApplicationContext(), ObservationListActivity.class);
        intent.putExtra("hike_edit_id", hikeId);
        startActivity(intent);
    }
}