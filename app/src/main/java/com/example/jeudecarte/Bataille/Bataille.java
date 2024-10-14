package com.example.jeudecarte.Bataille;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.example.jeudecarte.MainActivity;
import com.example.jeudecarte.databinding.BatailleBinding;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Bataille extends Activity {
    private static String TAG = "affichage debug BATAILLE";

    private static String path = "cards_52/";

    private boolean autoDraw = false;

    private int totalCardsDrawn = 0;

    private int nombreJoueur = 2;

    private Thread drawThread;

    //liste contenant pour chaque joueur une pair. Cette pair est sa pile de carte a
    // piocher et sa pile de carte joué. Une pile de carte est une arraylist contenant des cartes.
    // Une carte est une pair composé de sa valeur et sa couleur, 2 int
    private static ArrayList<Pair<ArrayList<Pair<Integer,Integer>>,ArrayList<Pair<Integer,Integer>>>> joueurs;

    @SuppressLint("StaticFieldLeak")
    private BatailleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = BatailleBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize player hands
        joueurs = new ArrayList<>();
        for (int i = 0; i < nombreJoueur; i++){
            joueurs.add(new Pair<>(new ArrayList<>(), new ArrayList<>()));
        }

        //create all the playable cards
        ArrayList<Pair<Integer,Integer>> fullDeck = new ArrayList<>();
        for (int i = 0; i < 13; i++){
            for (int j = 0; j < 4; j++){
                Pair<Integer,Integer> carte = new Pair<>(i, j);
                fullDeck.add(carte);
            }
        }
        Collections.shuffle(fullDeck);

        //deal the cards to players
        int i = 0;
        while (!fullDeck.isEmpty()){
            joueurs.get(i).first.add(fullDeck.remove(0));
            i = (i+1)%nombreJoueur;
        }

        refreshCardCount();
        takeOffPlayedCard();

        binding.draw.setWidth(700);
        binding.draw.setOnClickListener(v -> draw());
        binding.retour.setOnClickListener(v -> retourMenu());
        binding.auto.setOnClickListener(v -> toggleAuto());
        binding.auto.setBackgroundColor(0xFFF08080);

        loadImageFromAssets(binding.drawingStack1, path+"back_dark.png");
        loadImageFromAssets(binding.drawingStack2, path+"back_dark.png");

    }

    private void toggleAuto(){
        autoDraw = !autoDraw;

        if (autoDraw){
            binding.auto.setBackgroundColor(0xFF90EE90);
            drawThread = new Thread(this::autoDrawing);
            drawThread.start();
        }
        else{
            binding.auto.setBackgroundColor(0xFFF08080);
            try {
                drawThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void autoDrawing(){
        while (autoDraw){
            runOnUiThread(() -> binding.draw.performClick());

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadImageFromAssets(ImageView image, String imagePath) {
        AssetManager assetManager = getAssets();

        try (InputStream inputStream = assetManager.open(imagePath)) {
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            image.setImageDrawable(drawable);
        }
        catch (IOException e) {
            Log.d(TAG,"error : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void retourMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void draw(){
        totalCardsDrawn += 1;
        binding.total.setText(Integer.toString(totalCardsDrawn));

        if (calculGagnant() != 0) {
            finDePartie();
            return;
        }

        Pair<Integer, Integer> card;
        card = joueurs.get(0).first.remove(0);
        joueurs.get(0).second.add(card);

        loadImageFromAssets(binding.playingStack1, path + cardToString(card) + ".png");



        card = joueurs.get(1).first.remove(0);
        joueurs.get(1).second.add(card);

        loadImageFromAssets(binding.playingStack2, path + cardToString(card) + ".png");


        refreshCardCount();

        int result = compareValeur();
        if (result == 1){
            binding.draw.setText("TAKE CARDS");
            binding.draw.setOnClickListener(v -> take());
        }
        else if (result == 2) {
            binding.draw.setText("GIVE CARDS");
            binding.draw.setOnClickListener(v -> give());
        }
        else {
            binding.draw.setText("BATAILLE");
            binding.draw.setOnClickListener(v -> bataille());
        }

    }

    @SuppressLint("SetTextI18n")
    private void take(){
        //shuffle the cards to avoid soft locks
        //shuffle one deck with itself
        Collections.shuffle(joueurs.get(0).second);
        Collections.shuffle(joueurs.get(1).second);

        int taille = joueurs.get(0).second.size();
        for (int i = 0; i < taille; i++){

            //randomly place either the player's 1 or 2 card first in the deck
            int first,second;
            if (Math.random() < 0.5){
                first = 0;
                second = 1;
            }
            else {
                first = 1;
                second = 0;
            }

            Pair<Integer,Integer> card = joueurs.get(first).second.remove(0);
            joueurs.get(0).first.add(card);

            card = joueurs.get(second).second.remove(0);
            joueurs.get(0).first.add(card);
        }

        refreshCardCount();
        takeOffPlayedCard();

        binding.draw.setText("DRAW");
        binding.draw.setOnClickListener(v -> draw());
    }

    @SuppressLint("SetTextI18n")
    private void give(){
        //shuffle the cards to avoid soft locks
        //shuffle one deck with itself
        Collections.shuffle(joueurs.get(0).second);
        Collections.shuffle(joueurs.get(1).second);

        int taille = joueurs.get(0).second.size();
        for (int i = 0; i < taille ; i++){

            //randomly place either the player's 1 or 2 card first in the deck
            int first,second;
            if (Math.random() < 0.5){
                first = 0;
                second = 1;
            }
            else {
                first = 1;
                second = 0;
            }

            Pair<Integer,Integer> card = joueurs.get(first).second.remove(0);
            joueurs.get(1).first.add(card);

            card = joueurs.get(second).second.remove(0);
            joueurs.get(1).first.add(card);
        }

        refreshCardCount();
        takeOffPlayedCard();

        binding.draw.setText("DRAW");
        binding.draw.setOnClickListener(v -> draw());
    }

    @SuppressLint("SetTextI18n")
    private void bataille(){
        if (calculGagnant() != 0) {
            finDePartie();
            return;
        }

        Pair<Integer, Integer> card;
        card = joueurs.get(0).first.remove(0);
        joueurs.get(0).second.add(card);

        loadImageFromAssets(binding.playingStack1, path + "back_dark.png");




        card = joueurs.get(1).first.remove(0);
        joueurs.get(1).second.add(card);

        loadImageFromAssets(binding.playingStack2, path + "back_dark.png");

        refreshCardCount();

        binding.draw.setText("DRAW");
        binding.draw.setOnClickListener(v -> draw());
    }

    private int compareValeur(){
        ArrayList<Pair<Integer, Integer>> playedCard;

        playedCard = joueurs.get(0).second;
        int v1 = playedCard.get(playedCard.size()-1).first;

        playedCard = joueurs.get(1).second;
        int v2 = playedCard.get(playedCard.size()-1).first;

        if (v1 > v2) return 1;
        else if (v1 < v2) return 2;
        else return 0;
    }

    private int calculGagnant(){
        //j2 gagne
        if (joueurs.get(0).first.isEmpty()) return 2;

        //j1 gagne
        else if (joueurs.get(1).first.isEmpty()) return 1;

        //partie non fini
        else return 0;
    }

    @SuppressLint("SetTextI18n")
    private void finDePartie(){

        //disable autoDraw
        binding.auto.setOnClickListener(null);
        if (autoDraw){
            toggleAuto();
        }

        binding.draw.setOnClickListener(null);
        binding.draw.setVisibility(View.GONE);

        binding.endGame.setVisibility(View.VISIBLE);
        binding.endGame.setOnClickListener(v -> retourMenu());

        String victoryText;
        if (calculGagnant() == 1) victoryText = "Victoire du J1 !";
        else victoryText = "Victoire du J2 !";

        binding.playingStack1.setVisibility(View.INVISIBLE);
        binding.playingStack2.setVisibility(View.INVISIBLE);
        binding.drawingStack1.setVisibility(View.INVISIBLE);
        binding.drawingStack2.setVisibility(View.INVISIBLE);
        binding.cardCount1.setVisibility(View.INVISIBLE);
        binding.cardCount2.setVisibility(View.INVISIBLE);

        binding.victoire.setVisibility(View.VISIBLE);
        binding.victoire.setText(victoryText);
    }

    @SuppressLint("SetTextI18n")
    private void refreshCardCount(){
        binding.cardCount1.setText(Integer.toString(joueurs.get(0).first.size()));
        binding.cardCount2.setText(Integer.toString(joueurs.get(1).first.size()));
    }

    private void takeOffPlayedCard(){
        binding.playingStack1.setImageDrawable(null);
        binding.playingStack2.setImageDrawable(null);
    }

    public String cardToString(Pair<Integer, Integer> card){
        ArrayList<String> color = new ArrayList<>(Arrays.asList("hearts", "spades", "diamonds", "clubs"));
        ArrayList<String> value = new ArrayList<>(Arrays.asList("2","3","4","5","6","7","8","9","10","j","q","k","a"));

        String nom = "";
        nom += color.get(card.second);
        nom += "_";
        nom += value.get(card.first);

        return nom;
    }
}
