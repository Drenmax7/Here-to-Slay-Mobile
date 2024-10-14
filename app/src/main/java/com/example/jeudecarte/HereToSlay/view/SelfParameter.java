package com.example.jeudecarte.HereToSlay.view;

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

    public static ArrayList<String> nameList;

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
            Settings.name = getRandomName(getAssets());
        }

        binding.nameEditText.setText(Settings.name);
    }

    private void setRandomName(){
        binding.randomNameButton.setOnClickListener(v -> {
            binding.nameEditText.setText(getRandomName(getAssets()));
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
            binding.nameEditText.setText(getRandomName(getAssets()));
            binding.languageSpinner.setSelection(0);
        });
    }

    public static String getRandomName(AssetManager assetManager){
        //getAssets();

        if (nameList == null) {
            nameList = new ArrayList<>();
            String[] classes = new String[]{
                    "bard",
                    "berserker",
                    "druid",
                    "fighter",
                    "guardian",
                    "necromancer",
                    "ranger",
                    "sorcerer",
                    "thief",
                    "warrior",
                    "wizard",
                    "leader",
                    "monster"
            };
            try {
                for (String oneClass : classes){
                    String [] names = assetManager.list("cards_here_to_slay/" + oneClass);
                    nameList.addAll(Arrays.asList(names));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < nameList.size(); i++){
                String name = nameList.get(i);
                nameList.set(i,name.substring(0,name.length()-4));
            }
        }

        Random rand = new Random();
        return nameList.get(rand.nextInt(nameList.size()));
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
