package com.example.m_hike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.m_hike.database.DatabaseHelper;
import com.example.m_hike.databinding.ActivityMainBinding;
import com.example.m_hike.fragment.AddHikeFragment;
import com.example.m_hike.fragment.HikeListFragment;
import com.example.m_hike.fragment.WeatherFragment;
import com.example.m_hike.model.Hike;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    HikeListFragment hikeListFragment;
    AddHikeFragment addHikeFragment;
    WeatherFragment weatherFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // change bottom navigation view color
        binding.bottomNavigation.setBackgroundResource(R.color.view_line);

        hikeListFragment = new HikeListFragment();
        addHikeFragment = new AddHikeFragment();
        weatherFragment = new WeatherFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_container, hikeListFragment)
                .commit();
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.hike_home) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_container, hikeListFragment)
                            .commit();
                    return true;

                } else if (id == R.id.hike_add) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_container, addHikeFragment)
                            .commit();
                    return true;

                } else if (id == R.id.hike_weather) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_container, weatherFragment)
                            .commit();
                    return true;

                }
                return false;
            }
        });

    }

}