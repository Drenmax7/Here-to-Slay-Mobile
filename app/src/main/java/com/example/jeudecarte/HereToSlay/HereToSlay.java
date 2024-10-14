package com.example.jeudecarte.HereToSlay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
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

    @SuppressLint("StaticFieldLeak")
    private HereToSlayHomeBinding binding;
    private static final int REQUEST_ENABLE_BT = 1; // Code de requête pour activer le Bluetooth
    private static final int REQUEST_PERMISSIONS = 2; // Code de requête pour demander les permissions

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HereToSlayHomeBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        //load name and preferred language from saved file
        SharedPreferences sharedPreferences = getSharedPreferences("HereToSlay", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        if (name == null) {
            name = "";
        }
        Settings.name = name;

        String language = sharedPreferences.getString("language", null);
        if (language == null) {
            language = "English";
        }
        Settings.language = language;


        // Initialisation de l'adaptateur Bluetooth
        /*mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            // L'appareil ne prend pas en charge le Bluetooth
            Toast.makeText(this, "Le Bluetooth n'est pas disponible sur cet appareil", Toast.LENGTH_LONG).show();
            finish(); // Ferme l'application si Bluetooth non disponible
            return;
        }

        // Demander l'activation du Bluetooth et les permissions si nécessaire
        checkBluetoothPermissions();
        ensureDiscoverable();*/

        binding.retour.setOnClickListener(v -> retourMenu());
        binding.parameter.setOnClickListener(v -> selfParameter());
        binding.joinGame.setOnClickListener(v -> joinGame());
        binding.createGame.setOnClickListener(v -> createGame());
        binding.loadGame.setOnClickListener(v -> loadGame());
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    /*private void ensureDiscoverable() {
        try{
            if (mBluetoothAdapter.getScanMode() !=
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
            }
        }
        catch (SecurityException exception){
            //todo handle exception
            Log.d("affichage debug","bluetooth non permis : " + exception.getMessage());
        }
    }*/



    // Vérifie les permissions et demande l'activation du Bluetooth
    /*private void checkBluetoothPermissions() {
        // Vérifier si les permissions sont nécessaires (Android 6.0 et supérieur)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Demander les permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.BLUETOOTH,
                                Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.BLUETOOTH_SCAN,
                        },
                        REQUEST_PERMISSIONS);
            } else {
                // Si les permissions sont déjà accordées, vérifier si le Bluetooth est activé
                checkBluetoothEnabled();
            }
        } else {
            // Pour les versions antérieures à Android 6.0, seulement vérifier si le Bluetooth est activé
            checkBluetoothEnabled();
        }
    }*/

    /*// Demande à l'utilisateur d'activer le Bluetooth si ce n'est pas déjà fait
    private void checkBluetoothEnabled() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            try {
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            catch (SecurityException exception){
                //todo handle exception
                Log.d("affichage debug","bluetooth non permis : " + exception.getMessage());
            }
        }
    }*/

    // Gestion des résultats de la demande d'activation du Bluetooth
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode != RESULT_OK) {
                // L'utilisateur a refusé d'activer le Bluetooth
                Toast.makeText(this, "Le Bluetooth est nécessaire pour utiliser cette application", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    /*// Gestion du résultat de la demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("affichage debug",""+requestCode);
        Log.d("affichage debug", Arrays.toString(grantResults));
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                // Si toutes les permissions sont accordées, vérifier si le Bluetooth est activé
                checkBluetoothEnabled();
            } else {
                // Si les permissions ne sont pas accordées, afficher un message et fermer l'application
                Toast.makeText(this, "Les permissions Bluetooth sont nécessaires", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }*/

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

    private void selfParameter(){
        Intent intent = new Intent(this, SelfParameter.class);
        startActivity(intent);
    }
}
