package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.m_hike.adapter.HikeAdapter;
import com.example.m_hike.database.DatabaseHelper;
import com.example.m_hike.databinding.ActivityHikeListBinding;
import com.example.m_hike.model.Hike;

import java.util.ArrayList;

public class HikeListActivity extends AppCompatActivity implements HikeAdapter.RecyclerViewListener {

    private ActivityHikeListBinding binding;

    DatabaseHelper databaseHelper;

    ArrayList<Hike> hikeList = new ArrayList<>();
    HikeAdapter hikeAdapter;
    AlertDialog.Builder builder;

    @Override
    protected void onResume() {
        super.onResume();

        databaseHelper = new DatabaseHelper(this);

        hikeList = databaseHelper.getAllHike();
        bindRecyclerView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHikeListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        hikeList = databaseHelper.getAllHike();

        // Recycler
        if (hikeList.size() != 0) {
            binding.tvNoHIke.setVisibility(View.INVISIBLE);
            binding.recHikeList.setVisibility(View.VISIBLE);
            binding.mbDeleteAllHike.setVisibility(View.VISIBLE);
            bindRecyclerView();
        } else {
            binding.tvNoHIke.setVisibility(View.VISIBLE);
            binding.tvNoHIke.setText(R.string.no_hike_record_found);
            binding.recHikeList.setVisibility(View.INVISIBLE);
            binding.mbDeleteAllHike.setVisibility(View.GONE);
        }

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.mbDeleteAllHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder = new AlertDialog.Builder(HikeListActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Are you sure want to delete all hike records?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteAllHike();
                        Toast.makeText(getApplicationContext(), "Successfully deleted all hikes.", Toast.LENGTH_SHORT).show();
                        binding.mbDeleteAllHike.setVisibility(View.GONE);

                        binding.tvNoHIke.setVisibility(View.VISIBLE);
                        binding.tvNoHIke.setText(R.string.no_observation_record_found);
                        binding.recHikeList.setVisibility(View.INVISIBLE);

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
        });

        binding.ivSearchHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchHikeActivity.class);
                startActivity(intent);
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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void deleteHikeClick(int hikeId) {

        builder = new AlertDialog.Builder(HikeListActivity.this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Are you sure want to delete this hike record?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteHikeDetails(hikeId);
                Toast.makeText(getApplicationContext(), "Hike Id " + Integer.toString(hikeId) + " is successfully deleted.", Toast.LENGTH_SHORT).show();
                hikeList.clear();
                hikeList = databaseHelper.getAllHike();

                if (hikeList.size() != 0) {
                    binding.tvNoHIke.setVisibility(View.INVISIBLE);
                    binding.recHikeList.setVisibility(View.VISIBLE);
                    binding.mbDeleteAllHike.setVisibility(View.VISIBLE);
                    bindRecyclerView();
                } else {
                    binding.tvNoHIke.setVisibility(View.VISIBLE);
                    binding.tvNoHIke.setText(R.string.no_hike_record_found);
                    binding.recHikeList.setVisibility(View.INVISIBLE);
                    binding.mbDeleteAllHike.setVisibility(View.INVISIBLE);
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