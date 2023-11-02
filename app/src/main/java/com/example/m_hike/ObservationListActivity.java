package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.m_hike.adapter.HikeAdapter;
import com.example.m_hike.adapter.ObservationAdapter;
import com.example.m_hike.database.DatabaseHelper;
import com.example.m_hike.databinding.ActivityObservationListBinding;
import com.example.m_hike.model.Hike;
import com.example.m_hike.model.Observation;

import java.util.ArrayList;

public class ObservationListActivity extends AppCompatActivity implements ObservationAdapter.RecyclerViewListener {

    private ActivityObservationListBinding binding;

    DatabaseHelper databaseHelper;

    ArrayList<Observation> observationList = new ArrayList<>();
    ObservationAdapter observationAdapter;
    AlertDialog.Builder builder;

    int hikeId;

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        hikeId = intent.getExtras().getInt("hike_edit_id");

        databaseHelper = new DatabaseHelper(this);

        observationList = databaseHelper.getAllObservationByHikeID(hikeId);
        bindRecyclerView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityObservationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        hikeId = intent.getExtras().getInt("hike_edit_id");
        databaseHelper = new DatabaseHelper(this);

        observationList = databaseHelper.getAllObservationByHikeID(hikeId);

        // Recycler
        if (observationList.size() != 0) {
            binding.tvNoObservation.setVisibility(View.INVISIBLE);
            binding.recObservationList.setVisibility(View.VISIBLE);
            binding.mbDeleteAllObservation.setVisibility(View.VISIBLE);
            bindRecyclerView();
        } else {
            binding.tvNoObservation.setVisibility(View.VISIBLE);
            binding.tvNoObservation.setText(R.string.no_observation_record_found);
            binding.recObservationList.setVisibility(View.INVISIBLE);
            binding.mbDeleteAllObservation.setVisibility(View.GONE);
        }

        binding.mbDeleteAllObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder = new AlertDialog.Builder(ObservationListActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Are you sure want to delete all observation records?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteAllObservationByHikeId(hikeId);
                        Toast.makeText(getApplicationContext(), "Successfully deleted all observations.", Toast.LENGTH_SHORT).show();
                        binding.mbDeleteAllObservation.setVisibility(View.GONE);

                        binding.tvNoObservation.setVisibility(View.VISIBLE);
                        binding.tvNoObservation.setText(R.string.no_observation_record_found);
                        binding.recObservationList.setVisibility(View.INVISIBLE);

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

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void bindRecyclerView() {

        binding.recObservationList.setLayoutManager(new LinearLayoutManager(this));
        binding.recObservationList.setHasFixedSize(true);
        observationAdapter = new ObservationAdapter(this, observationList, this);
        binding.recObservationList.setAdapter(observationAdapter);

    }

    @Override
    public void editObservationClick(int observation_id) {
        Intent intent = new Intent(this.getApplicationContext(), EditObservationActivity.class);
        intent.putExtra("observation_id", observation_id);
        startActivity(intent);
    }

    @Override
    public void deleteObservationClick(int observation_id) {
        builder = new AlertDialog.Builder(ObservationListActivity.this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Are you sure want to delete this observation record?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteObservationByObservationId(hikeId);
                Toast.makeText(getApplicationContext(), "Successfully deleted.", Toast.LENGTH_SHORT).show();
                observationList.clear();
                observationList = databaseHelper.getAllObservationByHikeID(hikeId);

                if (observationList.size() != 0) {
                    binding.tvNoObservation.setVisibility(View.INVISIBLE);
                    binding.recObservationList.setVisibility(View.VISIBLE);
                    binding.mbDeleteAllObservation.setVisibility(View.VISIBLE);
                    bindRecyclerView();
                } else {
                    binding.tvNoObservation.setVisibility(View.VISIBLE);
                    binding.tvNoObservation.setText(R.string.no_observation_record_found);
                    binding.recObservationList.setVisibility(View.INVISIBLE);
                    binding.mbDeleteAllObservation.setVisibility(View.INVISIBLE);
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
}