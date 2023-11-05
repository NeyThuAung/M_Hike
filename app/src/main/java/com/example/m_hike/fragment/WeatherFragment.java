package com.example.m_hike.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import com.example.m_hike.R;
import com.example.m_hike.databinding.FragmentWeatherBinding;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebSettings webSettings = binding.weatherWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        binding.weatherWebView.getSettings().setJavaScriptEnabled(true);
        binding.weatherWebView.setFocusable(true);
        binding.weatherWebView.setFocusableInTouchMode(true);

        binding.weatherWebView.loadUrl("https://openweathermap.org/");

        binding.weatherWebView.setWebViewClient(new WebViewClient());

    }
}