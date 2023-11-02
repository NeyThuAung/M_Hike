package com.example.m_hike;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.m_hike.databinding.ActivityAddObservationBinding;
import com.example.m_hike.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}