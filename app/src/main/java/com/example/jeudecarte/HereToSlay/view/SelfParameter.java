package com.example.jeudecarte.HereToSlay.view;

import static com.example.jeudecarte.HereToSlay.InfoDeck.getRandomName;
import static com.example.jeudecarte.HereToSlay.view.HereToSlay.GENERIC;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.jeudecarte.HereToSlay.Settings;
import com.example.jeudecarte.databinding.HereToSlaySelfParameterBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * List all personal parameters that can be changed and allow their modification
 * The save button then save the settings and return to previous activity
 */
public class SelfParameter extends Activity {
    //Attributes

    /**
     * The tag showed in the logcat console
     */
    private static final String TAG = GENERIC + "SELF_PARAMETER";

    /**
     * The variable that list all id of the xml file
     */
    private HereToSlaySelfParameterBinding binding;


    //Methods

    /**
     * Function launched when activity is started
     * Initialize buttons and textViews
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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

    /**
     * Put the name specified in the Setting class inside of the textView
     * If no name has been specified in the class, generate one randomly
     */
    private void setName(){
        if (Settings.name.isEmpty()){
            Settings.name = getRandomName();
        }

        binding.nameEditText.setText(Settings.name);
    }

    /**
     * Initialize the random button to give a random name in the textView
     */
    private void setRandomName(){
        binding.randomNameButton.setOnClickListener(v -> binding.nameEditText.setText(getRandomName()));
    }

    /**
     * Initialize the save button to save all fields into the Setting class
     * Then finish the activity
     */
    private void setSave(){
        binding.saveSettingsButton.setOnClickListener(v -> {
            Settings.name = binding.nameEditText.getText().toString();

            int newLanguage = binding.languageSpinner.getSelectedItemPosition();
            boolean changeLanguage = true;
            if (Settings.language == newLanguage) changeLanguage = false;
            Settings.language = newLanguage;

            SharedPreferences sharedPreferences = getSharedPreferences("HereToSlay", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", Settings.name);
            editor.putInt("language", Settings.language);
            editor.apply();

            if (changeLanguage) restartApplication();
            else finish();
        });
    }

    private void restartApplication() {
        Intent intent = new Intent(getApplicationContext(), HereToSlay.class);
        startActivity(intent);
    }

    /**
     * Reset fields to their original state
     */
    private void setReset(){
        binding.resetButton.setOnClickListener(v -> {
            binding.nameEditText.setText(getRandomName());
            binding.languageSpinner.setSelection(0);
        });
    }

    /**
     * Fill the language spinner with some language
     * and put selection on the one give by the Setting class
     */
    private void setLanguage(){
        String[] arraySpinner = new String[] {
                "English", "Français"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.languageSpinner.setAdapter(adapter);

        binding.languageSpinner.setSelection(Settings.language);
    }
}
