package com.example.jeudecarte.HereToSlay.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import com.example.jeudecarte.HereToSlay.network.Client;
import com.example.jeudecarte.HereToSlay.network.Packet;
import com.example.jeudecarte.databinding.HereToSlayGameBinding;
import com.example.jeudecarte.databinding.HereToSlayHubBinding;

public class Game extends Activity implements View {
    public static Client client;

    @SuppressLint("StaticFieldLeak")
    private HereToSlayGameBinding binding;

    @Override
    public void dataTreatment(Packet packet) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HereToSlayGameBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        client.view = this;
    }
}
