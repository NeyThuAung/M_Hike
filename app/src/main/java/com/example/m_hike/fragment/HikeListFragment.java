package com.example.m_hike.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.m_hike.AddObservationActivity;
import com.example.m_hike.EditHikeActivity;
import com.example.m_hike.FilterHikeActivity;
import com.example.m_hike.HikeDetailsActivity;
import com.example.m_hike.ObservationListActivity;
import com.example.m_hike.R;
import com.example.m_hike.SearchHikeActivity;
import com.example.m_hike.adapter.HikeAdapter;
import com.example.m_hike.database.DatabaseHelper;
import com.example.m_hike.databinding.FragmentHikeListBinding;
import com.example.m_hike.model.Hike;

import java.util.ArrayList;


public class HikeListFragment extends Fragment implements HikeAdapter.RecyclerViewListener {

    private FragmentHikeListBinding binding;

    DatabaseHelper databaseHelper;

    ArrayList<Hike> hikeList = new ArrayList<>();
    HikeAdapter hikeAdapter;
    AlertDialog.Builder builder;

    @Override
    public void onResume() {
        super.onResume();

        databaseHelper = new DatabaseHelper(requireContext());

        hikeList = databaseHelper.getAllHike();
        bindRecyclerView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHikeListBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(requireContext());

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

        binding.mbDeleteAllHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Are you sure?");
                builder.setMessage("Are you sure want to delete all hike records?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteAllHike();
                        Toast.makeText(requireContext(), "Successfully deleted all hikes.", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(requireContext(), SearchHikeActivity.class);
                startActivity(intent);
            }
        });

        binding.ivFilterHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), FilterHikeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void bindRecyclerView() {
        binding.recHikeList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recHikeList.setHasFixedSize(true);
        hikeAdapter = new HikeAdapter(requireContext(), hikeList, this);
        binding.recHikeList.setAdapter(hikeAdapter);
    }

    @Override
    public void hikeItemClick(int hikeId) {
        Intent intent = new Intent(requireContext(), HikeDetailsActivity.class);
        intent.putExtra("hike_edit_id", hikeId);
        startActivity(intent);
    }

    @Override
    public void editHikeClick(int hikeId) {
        Intent intent = new Intent(requireContext(), EditHikeActivity.class);
        intent.putExtra("hike_edit_id", hikeId);
        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void deleteHikeClick(int hikeId) {

        builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Are you sure?");
        builder.setMessage("Are you sure want to delete this hike record?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteHikeDetails(hikeId);
                Toast.makeText(requireActivity(), "Hike Id " + Integer.toString(hikeId) + " is successfully deleted.", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(requireContext(), AddObservationActivity.class);
        intent.putExtra("hike_edit_id", hikeId);
        startActivity(intent);
    }

    @Override
    public void showObservationList(int hikeId) {
        Intent intent = new Intent(requireContext(), ObservationListActivity.class);
        intent.putExtra("hike_edit_id", hikeId);
        startActivity(intent);
    }
}