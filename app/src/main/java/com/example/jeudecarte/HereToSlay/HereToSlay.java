package com.example.jeudecarte.HereToSlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.jeudecarte.HereToSlay.network.Client;
import com.example.jeudecarte.HereToSlay.view.Hub;
import com.example.jeudecarte.HereToSlay.view.Parameter;
import com.example.jeudecarte.HereToSlay.view.SelfParameter;
import com.example.jeudecarte.MainActivity;
import com.example.jeudecarte.databinding.HereToSlayHomeBinding;

public class HereToSlay extends Activity {
    //Attributes

    /**
     * The start of all tags of the application
     */
    public static final String GENERIC = "debug message ";
    
    /**
     * The tag showed in the logcat console
     */
    private static final String TAG = GENERIC + "HERE_TO_SLAY";

    /**
     * The variable that list all id of the xml file
     */
    private HereToSlayHomeBinding binding;


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HereToSlayHomeBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize a list with all heroes names
        Utility.setNameList(getAssets());

        getName();
        getLanguage();

        setReturn();
        setJoin();
        setCreate();
        setLoad();
        setSelfParameter();
    }

    /**
     * Get the language the user might have select and assign it in the setting class
     */
    private void getLanguage(){
        SharedPreferences sharedPreferences = getSharedPreferences("HereToSlay", MODE_PRIVATE);
        String language = sharedPreferences.getString("language", null);
        if (language == null) {
            language = "English";
        }
        Settings.language = language;
    }

    /**
     * Get the name the user might have choose and assign it in the setting class
     */
    private void getName(){
        SharedPreferences sharedPreferences = getSharedPreferences("HereToSlay", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        if (name == null) {
            name = "";
        }
        Settings.name = name;
    }

    /**
     * Link the return function to the return button
     */
    private void setReturn(){
        binding.retour.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Create a dialog when click on join button
     */
    private void setJoin(){
        binding.joinGame.setOnClickListener(v -> {
            Log.d(TAG,"enter the function");

            // create an edittext to enter host code
            EditText hostAddress = new EditText(binding.joinGame.getContext());
            hostAddress.setHint("Host code");
            hostAddress.setInputType(android.text.InputType.TYPE_CLASS_TEXT);

            // build the alert dialog
            AlertDialog.Builder dialogBuilder = getBuilder(hostAddress);

            // show popup
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        });
    }

    /**
     * Create the dialog box
     * The login button of the dialog connect to the server
     *
     * @param hostAddress The EditText to display in the alert box
     * @return the dialogBuilder to show
     */
    private AlertDialog.Builder getBuilder(EditText hostAddress) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(binding.joinGame.getContext());
        dialogBuilder.setTitle("Enter host code");
        dialogBuilder.setView(hostAddress);

        // login button
        dialogBuilder.setPositiveButton("Login", (dialog, which) -> {
            //String enteredCode = hostAddress.getText().toString();

            Client client = new Client("10.0.2.2",6666);
            new Thread(client::connexion).start();

            Hub.client = client;

            //load hub view
            Intent intent = new Intent(this, Hub.class);
            startActivity(intent);
        });

        // cancel button
        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            // close popup
            dialog.dismiss();
        });

        return dialogBuilder;
    }

    /**
     * Start the parameter activity when click on create game button
     */
    private void setCreate(){
        binding.createGame.setOnClickListener(v -> {
            Intent intent = new Intent(this, Parameter.class);
            startActivity(intent);
        });
    }

    private void setLoad(){
        //todo implements save game
        binding.loadGame.setOnClickListener(v -> {

        });
    }

    /**
     * Link the parameter button to the selfParameter activity
     */
    private void setSelfParameter(){
        binding.parameter.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelfParameter.class);
            startActivity(intent);
        });
    }
}
