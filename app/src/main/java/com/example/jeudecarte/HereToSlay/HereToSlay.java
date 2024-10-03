package com.example.jeudecarte.HereToSlay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.jeudecarte.HereToSlay.view.Parameter;
import com.example.jeudecarte.MainActivity;
import com.example.jeudecarte.databinding.HereToSlayHomeBinding;

public class HereToSlay extends Activity {

    @SuppressLint("StaticFieldLeak")
    private HereToSlayHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HereToSlayHomeBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        binding.retour.setOnClickListener(v -> retourMenu());
        binding.joinGame.setOnClickListener(v -> joinGame());
        binding.createGame.setOnClickListener(v -> createGame());
        binding.loadGame.setOnClickListener(v -> loadGame());
    }

    private void retourMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void joinGame(){

    }

    private void createGame(){
        Intent intent = new Intent(this, Parameter.class);
        startActivity(intent);
    }

    private void loadGame(){

    }
}
