package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.m_hike.adapter.ObservationAdapter;
import com.example.m_hike.adapter.ObservationListAdapter;
import com.example.m_hike.database.DatabaseHelper;
import com.example.m_hike.databinding.ActivityHikeDetailsBinding;
import com.example.m_hike.model.Hike;
import com.example.m_hike.model.Observation;

import java.util.ArrayList;

public class HikeDetailsActivity extends AppCompatActivity {

    private ActivityHikeDetailsBinding binding;

    Hike hike;
    ObservationListAdapter observationListAdapter;
    ArrayList<Observation> observationList;
    DatabaseHelper databaseHelper;

    int hike_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHikeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        hike_id = intent.getExtras().getInt("hike_edit_id");

        hike = databaseHelper.getHikeWithHikeId(hike_id);

        setUpHikeData();

        observationList = databaseHelper.getAllObservationByHikeID(hike_id);

        // Recycler
        if (observationList.size() != 0) {
            binding.tvObservationListTitle.setText(R.string.observation_list);
            binding.recObservationList.setVisibility(View.VISIBLE);
            bindObservationRecyclerView();
        } else {
            binding.tvObservationListTitle.setText(R.string.there_is_no_observation);
            binding.recObservationList.setVisibility(View.INVISIBLE);
        }
    }

    private void bindObservationRecyclerView() {

        binding.recObservationList.setLayoutManager(new LinearLayoutManager(this));
        binding.recObservationList.setHasFixedSize(true);
        observationListAdapter = new ObservationListAdapter(this, observationList);
        binding.recObservationList.setAdapter(observationListAdapter);

    }

    private void setUpHikeData() {

        binding.tvHikeName.setText(hike.getName());
        binding.tvHikeLocation.setText(hike.getLocation());
        binding.tvHikeDate.setText(hike.getDateOfHike());
        binding.tvParking.setText(hike.getParkingAvailable());
        binding.tvHikeLength.setText(hike.getLengthOfHeight() + " Km");
        binding.tvHikeDifficulty.setText(hike.getDifficulty());

        if (hike.getStartPoint().length() != 0) {
            binding.tvHikeStartPoint.setText(hike.getStartPoint());
        } else {
            binding.tvHikeStartPoint.setText("-");
        }

        if (hike.getEndPoint().length() != 0) {
            binding.tvHikeEndPoint.setText(hike.getEndPoint());
        } else {
            binding.tvHikeEndPoint.setText("-");
        }

        if (hike.getDescription().length() != 0) {
            binding.tvHikeDescription.setText(hike.getDescription());
        } else {
            binding.tvHikeDescription.setText("-");
        }

    }
}