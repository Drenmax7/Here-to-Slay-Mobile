package com.example.jeudecarte.HereToSlay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeudecarte.HereToSlay.network.Client;
import com.example.jeudecarte.HereToSlay.view.Hub;
import com.example.jeudecarte.HereToSlay.view.Parameter;
import com.example.jeudecarte.MainActivity;
import com.example.jeudecarte.databinding.HereToSlayHomeBinding;

import java.util.ArrayList;

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

    private void joinGame() {
        Log.d("affichage debug","rentre dans la fonction");
        // create an edittext to enter host code
        EditText hostAddress = new EditText(binding.joinGame.getContext());
        hostAddress.setHint("Host code");
        hostAddress.setInputType(android.text.InputType.TYPE_CLASS_TEXT);

        // build the alert dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(binding.joinGame.getContext());
        dialogBuilder.setTitle("Enter host code");
        dialogBuilder.setView(hostAddress);

        // login button
        dialogBuilder.setPositiveButton("Login", (dialog, which) -> {
            String enteredCode = hostAddress.getText().toString();

            Client client = new Client(enteredCode,6666);

            ArrayList<Boolean> state = new ArrayList<>();
            Thread thread = new Thread(() -> client.connexion(state));
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (state.get(0)) {
                Hub.client = client;

                //load hub view
                Intent intent = new Intent(this, Hub.class);
                startActivity(intent);
            } else {
                // incorrect code, close popup
                Toast.makeText(binding.joinGame.getContext(), "Incorrect code", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // cancel button
        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            // close popup
            dialog.dismiss();
        });

        // show popup
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void createGame(){
        Intent intent = new Intent(this, Parameter.class);
        startActivity(intent);
    }

    private void loadGame(){

    }
}
