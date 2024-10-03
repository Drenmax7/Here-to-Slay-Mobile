package com.example.jeudecarte;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;

import com.example.jeudecarte.databinding.BatailleBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Bataille extends Activity {
    private int nombreJoueur = 2;

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
    }

    private void retourMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void draw(){
        if (calculGagnant() != 0) {
            finDePartie();
            return;
        }

        Pair<Integer, Integer> card;
        card = joueurs.get(0).first.remove(0);
        joueurs.get(0).second.add(card);

        int resourceId = getRessourceId(card);
        binding.playingStack1.setImageResource(resourceId);


        card = joueurs.get(1).first.remove(0);
        joueurs.get(1).second.add(card);

        resourceId = getRessourceId(card);
        binding.playingStack2.setImageResource(resourceId);

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
        int taille = joueurs.get(0).second.size();
        for (int i = 0; i < taille; i++){

            Pair<Integer,Integer> card = joueurs.get(0).second.remove(0);
            joueurs.get(0).first.add(card);

            card = joueurs.get(1).second.remove(0);
            joueurs.get(0).first.add(card);
        }

        refreshCardCount();
        takeOffPlayedCard();

        binding.draw.setText("DRAW");
        binding.draw.setOnClickListener(v -> draw());
    }

    @SuppressLint("SetTextI18n")
    private void give(){
        int taille = joueurs.get(0).second.size();
        for (int i = 0; i < taille ; i++){

            Pair<Integer,Integer> card = joueurs.get(0).second.remove(0);
            joueurs.get(1).first.add(card);

            card = joueurs.get(1).second.remove(0);
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

        binding.playingStack1.setImageResource(R.drawable.back_dark);


        card = joueurs.get(1).first.remove(0);
        joueurs.get(1).second.add(card);

        binding.playingStack2.setImageResource(R.drawable.back_dark);

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

        binding.draw.setText("Retour au menu");

        binding.draw.setOnClickListener(v -> retourMenu());
    }

    @SuppressLint("DiscouragedApi")
    private int getRessourceId(Pair<Integer,Integer> card){
        return binding.playingStack1.getContext().getResources().getIdentifier(
                cardToString(card),
                "drawable",
                binding.playingStack1.getContext().getPackageName());
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
