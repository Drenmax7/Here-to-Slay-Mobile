package com.example.jeudecarte.HereToSlay.view;

import static com.example.jeudecarte.HereToSlay.Utility.getRandomName;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.jeudecarte.HereToSlay.Settings;
import com.example.jeudecarte.databinding.HereToSlaySelfParameterBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SelfParameter extends Activity {
    private static String TAG = "affichage debug SELF_PARAMETER";

    @SuppressLint("StaticFieldLeak")
    private HereToSlaySelfParameterBinding binding;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HereToSlaySelfParameterBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        setRandomName();
        setLanguage();
        setSave();
        setReset();
        setName();

    }

    private void setName(){
        if (Settings.name.isEmpty()){
            Settings.name = getRandomName();
        }

        binding.nameEditText.setText(Settings.name);
    }

    private void setRandomName(){
        binding.randomNameButton.setOnClickListener(v -> {
            binding.nameEditText.setText(getRandomName());
        });
    }

    private void setSave(){
        binding.saveSettingsButton.setOnClickListener(v -> {
            Settings.name = binding.nameEditText.getText().toString();
            Settings.language = binding.languageSpinner.getSelectedItem().toString();

            SharedPreferences sharedPreferences = getSharedPreferences("HereToSlay", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", Settings.name);
            editor.putString("language", Settings.language);
            editor.apply();

            finish();
        });
    }

    private void setReset(){
        binding.resetButton.setOnClickListener(v -> {
            binding.nameEditText.setText(getRandomName());
            binding.languageSpinner.setSelection(0);
        });
    }

    private void setLanguage(){
        String[] arraySpinner = new String[] {
                "English", "Français"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.languageSpinner.setAdapter(adapter);

        int position = Arrays.asList(arraySpinner).indexOf(Settings.language);
        binding.languageSpinner.setSelection(position);
    }
}
