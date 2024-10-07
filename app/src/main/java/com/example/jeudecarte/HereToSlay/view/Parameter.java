package com.example.jeudecarte.HereToSlay.view;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.jeudecarte.HereToSlay.Settings;
import com.example.jeudecarte.HereToSlay.controller.HubController;
import com.example.jeudecarte.HereToSlay.network.Client;
import com.example.jeudecarte.HereToSlay.network.Server;
import com.example.jeudecarte.MainActivity;
import com.example.jeudecarte.databinding.HereToSlayParameterBinding;

import java.util.ArrayList;

public class Parameter extends Activity {
    private String INFINI = "+∞";

    //todo make it so player max equals the number of different leader
    private int PLAYER_MIN = 2;
    private int PLAYER_MAX = 20;

    private int ACTION_POINT_MIN = 1;
    private int ACTION_POINT_MAX = 99;

    private int CARD_LIMIT_MIN = 0;
    private int CARD_LIMIT_MAX = 99;

    //todo make it so monster goal max equals the number of monster
    private int MONSTER_GOAL_MIN = 0;
    private int MONSTER_GOAL_MAX = 10;

    //todo make it so hero count max equals the number of heroes
    private int HERO_COUNT_MIN = 0;
    private int HERO_COUNT_MAX = 99;

    //todo make it so category cap max equals the maximum number of heroes in one category
    private int CATEGORY_CAP_MIN = 1;
    private int CATEGORY_CAP_MAX = 9;

    private int FORCED_LEADER_REROLL_MIN = 0;
    private int FORCED_LEADER_REROLL_MAX = 10;

    private boolean moreOption = false;

    @SuppressLint("StaticFieldLeak")
    private HereToSlayParameterBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HereToSlayParameterBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

        loadSettings();

        setPlayerCount();
        setGameRule();
        setExpansion();
        setActionPoint();
        setCardLimit();
        setMonsterGoal();
        setHeroCount();
        setCategoryCap();
        setForcedReroll();

        setSave();
        setReset();
        setMoreOption();
    }

    private void setPlayerCount(){
        binding.decreasePlayerCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.playerCountEditText.getText().toString());
            if (value > PLAYER_MIN) {
                value--;
                binding.playerCountEditText.setText(Integer.toString(value));
            }
        });

        binding.increasePlayerCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.playerCountEditText.getText().toString());
            if (value < PLAYER_MAX){
                value++;
                binding.playerCountEditText.setText(Integer.toString(value));
            }
        });

        binding.playerCountEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.playerCountEditText.getText().toString();
                if (text.isEmpty())
                    binding.playerCountEditText.setText(Integer.toString(PLAYER_MIN));
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(PLAYER_MIN, value);
                    newValue = min(PLAYER_MAX, newValue);
                    if (newValue != value)
                        binding.playerCountEditText.setText(Integer.toString(newValue));
                }
            }
            return false;
        });

    }

    private void setGameRule(){
        //todo changing rules change default settings
        String[] arraySpinner = new String[] {
                "Normal", "2 Leaders", "Teams"
        };
        Spinner s = binding.gameStyleSpinner;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    private void setExpansion(){

    }

    private void setActionPoint(){
        binding.decreaseActionPoint.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.actionPointEditText.getText().toString());
            if (value > ACTION_POINT_MIN) {
                value--;
                binding.actionPointEditText.setText(Integer.toString(value));
            }
        });

        binding.increaseActionPoint.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.actionPointEditText.getText().toString());
            if (value < ACTION_POINT_MAX){
                value++;
                binding.actionPointEditText.setText(Integer.toString(value));
            }
        });

        binding.actionPointEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.actionPointEditText.getText().toString();
                if (text.isEmpty())
                    binding.actionPointEditText.setText(Integer.toString(ACTION_POINT_MIN));
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(ACTION_POINT_MIN, value);
                    newValue = min(ACTION_POINT_MAX, newValue);
                    if (newValue != value)
                        binding.actionPointEditText.setText(Integer.toString(newValue));
                }
            }
            return false;
        });
    }

    private void setCardLimit(){
        binding.decreaseCardLimit.setOnClickListener(v -> {
            String text = binding.cardLimitEditText.getText().toString();

            int value;
            if (text.equals(INFINI)) value = 100;
            else value = Integer.parseInt(text);

            if (value > CARD_LIMIT_MIN) {
                value--;
                binding.cardLimitEditText.setText(Integer.toString(value));
            }
        });

        binding.increaseCardLimit.setOnClickListener(v -> {
            String text = binding.cardLimitEditText.getText().toString();

            if (!text.equals(INFINI)) {
                int value = Integer.parseInt(text);
                if (value < CARD_LIMIT_MAX) {
                    value++;
                    if (value == 100) binding.cardLimitEditText.setText(INFINI);
                    else binding.cardLimitEditText.setText(Integer.toString(value));
                }
            }
        });

        binding.cardLimitEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.cardLimitEditText.getText().toString();

                try{
                    int value = Integer.parseInt(text);
                    int newValue = max(CARD_LIMIT_MIN, value);
                    newValue = min(CARD_LIMIT_MAX, newValue);
                    if (newValue != value) {
                        if (value > CARD_LIMIT_MAX) binding.cardLimitEditText.setText(INFINI);
                        else binding.cardLimitEditText.setText(Integer.toString(newValue));
                    }
                } catch (NumberFormatException e) {
                    if (!text.equals(INFINI))
                        binding.cardLimitEditText.setText(Integer.toString(CARD_LIMIT_MIN));
                }
            }
            return false;
        });
    }

    private void setMonsterGoal(){
        binding.decreaseMonsterCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.monsterCountEditText.getText().toString());
            if (value > MONSTER_GOAL_MIN) {
                value--;
                binding.monsterCountEditText.setText(Integer.toString(value));
            }
        });

        binding.increaseMonsterCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.monsterCountEditText.getText().toString());
            if (value < MONSTER_GOAL_MAX){
                value++;
                binding.monsterCountEditText.setText(Integer.toString(value));
            }
        });

        binding.monsterCountEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.monsterCountEditText.getText().toString();
                if (text.isEmpty())
                    binding.monsterCountEditText.setText(Integer.toString(MONSTER_GOAL_MIN));
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(MONSTER_GOAL_MIN, value);
                    newValue = min(MONSTER_GOAL_MAX, newValue);
                    if (newValue != value)
                        binding.monsterCountEditText.setText(Integer.toString(newValue));
                }
            }
            return false;
        });
    }

    private void setHeroCount(){
        binding.decreaseHeroCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.heroCountEditText.getText().toString());
            if (value > HERO_COUNT_MIN) {
                value--;
                binding.heroCountEditText.setText(Integer.toString(value));
            }
        });

        binding.increaseHeroCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.heroCountEditText.getText().toString());
            if (value < HERO_COUNT_MAX){
                value++;
                binding.heroCountEditText.setText(Integer.toString(value));
            }
        });

        binding.heroCountEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.heroCountEditText.getText().toString();
                if (text.isEmpty())
                    binding.heroCountEditText.setText(Integer.toString(HERO_COUNT_MIN));
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(HERO_COUNT_MIN, value);
                    newValue = min(HERO_COUNT_MAX, newValue);
                    if (newValue != value)
                        binding.heroCountEditText.setText(Integer.toString(newValue));
                }
            }
            return false;
        });
    }

    private void setCategoryCap(){
        binding.decreaseCategoryCap.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.categoryCapEditText.getText().toString());
            if (value > CATEGORY_CAP_MIN) {
                value--;
                binding.categoryCapEditText.setText(Integer.toString(value));
            }
        });

        binding.increaseCategoryCap.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.categoryCapEditText.getText().toString());
            if (value < CATEGORY_CAP_MAX){
                value++;
                binding.categoryCapEditText.setText(Integer.toString(value));
            }
        });

        binding.categoryCapEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.categoryCapEditText.getText().toString();
                if (text.isEmpty())
                    binding.categoryCapEditText.setText(Integer.toString(CATEGORY_CAP_MIN));
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(CATEGORY_CAP_MIN, value);
                    newValue = min(CATEGORY_CAP_MAX, newValue);
                    if (newValue != value)
                        binding.categoryCapEditText.setText(Integer.toString(newValue));
                }
            }
            return false;
        });
    }

    private void setForcedReroll(){
        binding.decreaseForcedReroll.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.forcedRerollEditText.getText().toString());
            if (value > FORCED_LEADER_REROLL_MIN) {
                value--;
                binding.forcedRerollEditText.setText(Integer.toString(value));
            }
        });

        binding.increaseForcedReroll.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.forcedRerollEditText.getText().toString());
            if (value < FORCED_LEADER_REROLL_MAX){
                value++;
                binding.forcedRerollEditText.setText(Integer.toString(value));
            }
        });

        binding.forcedRerollEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.forcedRerollEditText.getText().toString();
                if (text.isEmpty())
                    binding.forcedRerollEditText.setText(Integer.toString(FORCED_LEADER_REROLL_MIN));
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(FORCED_LEADER_REROLL_MIN, value);
                    newValue = min(FORCED_LEADER_REROLL_MAX, newValue);
                    if (newValue != value)
                        binding.forcedRerollEditText.setText(Integer.toString(newValue));
                }
            }
            return false;
        });
    }


    private void setSave(){
        binding.saveSettingsButton.setOnClickListener(v -> {
            //save all fields from parameters to settings class
            Settings.playerNumber = Integer.parseInt(binding.playerCountEditText.getText().toString());
            Settings.gameRule = binding.gameStyleSpinner.getSelectedItemPosition();

            Settings.warriorDruid = binding.warriosDruids.isChecked();
            Settings.berserkerNecromancer = binding.berserkersNecromancers.isChecked();
            Settings.sorcerer = binding.sorcerers.isChecked();
            Settings.monster = binding.monster.isChecked();
            Settings.leader = binding.leader.isChecked();
            Settings.hereToSleigh = binding.hereToSleigh.isChecked();

            Settings.actionPoint = Integer.parseInt(binding.actionPointEditText.getText().toString());

            String text = binding.cardLimitEditText.getText().toString();
            if (text.equals(INFINI)) Settings.cardLimit = -1;
            else Settings.cardLimit = Integer.parseInt(text);

            Settings.monsterGoal = Integer.parseInt(binding.monsterCountEditText.getText().toString());
            Settings.heroCount = Integer.parseInt(binding.heroCountEditText.getText().toString());
            Settings.categoryCap = Integer.parseInt(binding.categoryCapEditText.getText().toString());

            Settings.forcedReroll = Integer.parseInt(binding.forcedRerollEditText.getText().toString());

            // create the server
            Server server = new Server(6666);
            new Thread(server::run).start();
            server.controller = new HubController(server);

            //connect to server
            Client client = new Client("localhost",6666);

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
            }
            else {
                server.stop();
            }
        });
    }

    private void loadSettings(){
        binding.playerCountEditText.setText(Integer.toString(Settings.playerNumber));
        binding.gameStyleSpinner.setSelection(Settings.gameRule);

        binding.warriosDruids.setChecked(Settings.warriorDruid);
        binding.berserkersNecromancers.setChecked(Settings.berserkerNecromancer);
        binding.sorcerers.setChecked(Settings.sorcerer);
        binding.monster.setChecked(Settings.monster);
        binding.leader.setChecked(Settings.leader);
        binding.hereToSleigh.setChecked(Settings.hereToSleigh);

        binding.actionPointEditText.setText(Integer.toString(Settings.actionPoint));

        int value = Settings.cardLimit;
        if (value < 0){
            binding.cardLimitEditText.setText(INFINI);
        }
        else {
            binding.cardLimitEditText.setText(Integer.toString(value));
        }
        binding.monsterCountEditText.setText(Integer.toString(Settings.monsterGoal));
        binding.heroCountEditText.setText(Integer.toString(Settings.heroCount));
        binding.categoryCapEditText.setText(Integer.toString(Settings.categoryCap));

        binding.forcedRerollEditText.setText(Integer.toString(Settings.forcedReroll));
    }

    private void setReset(){
        binding.resetButton.setOnClickListener(v -> {
            Settings.resetSettings();
            loadSettings();
        });
    }

    private void setMoreOption(){
        binding.moreOptionButton.setOnClickListener(v -> {
            if (moreOption) {
                binding.advancedOption.setVisibility(View.GONE);
                binding.moreOptionButton.setText("More Options");
                moreOption = false;
            }
            else {
                binding.advancedOption.setVisibility(View.VISIBLE);
                binding.moreOptionButton.setText("Less Options");
                moreOption = true;
            }
        });
    }
}
