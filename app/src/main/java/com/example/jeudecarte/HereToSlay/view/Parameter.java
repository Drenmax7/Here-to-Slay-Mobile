package com.example.jeudecarte.HereToSlay.view;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.jeudecarte.HereToSlay.Settings;
import com.example.jeudecarte.R;
import com.example.jeudecarte.databinding.HereToSlayParameterBinding;

/**
 * List all parameters that can be changed for a game and allow their modification
 * The save button then start the game
 */
public class Parameter extends Activity {
    //Attributes

    //todo make it so player max equals the number of different leader
    /**
     * Minimal and maximal number of player in a game
     */
    private final int PLAYER_MIN = 2;
    private final int PLAYER_MAX = 20;

    /**
     * Minimal and maximal number of action point in a game
     */
    private final int ACTION_POINT_MIN = 1;
    private final int ACTION_POINT_MAX = 99;

    /**
     * Minimal and maximal card limit
     */
    private final int CARD_LIMIT_MIN = 0;
    private final int CARD_LIMIT_MAX = 99;

    //todo make it so monster goal max equals the number of monster
    /**
     * Minimal and maximal number of monster to kill to win
     */
    private final int MONSTER_GOAL_MIN = 0;
    private final int MONSTER_GOAL_MAX = 10;

    //todo make it so hero count max equals the number of heroes
    /**
     * Minimal and maximal number of heroes to have to win
     */
    private final int HERO_COUNT_MIN = 0;
    private final int HERO_COUNT_MAX = 99;

    //todo make it so category cap max equals the maximum number of heroes in one category
    /**
     * Minimal and maximal cap of heroes of one category counted to win
     */
    private final int CATEGORY_CAP_MIN = 1;
    private final int CATEGORY_CAP_MAX = 9;

    /**
     * Minimal and maximal number of forced reroll leader per person in the hub
     */
    private final int FORCED_LEADER_REROLL_MIN = 0;
    private final int FORCED_LEADER_REROLL_MAX = 10;

    /**
     * Symbol used as infinity
     */
    private final String INFINITY = "+∞";

    /**
     * If the advanced option are currently shown or not
     */
    private boolean moreOption = false;

    /**
     * The variable that list all id of the xml file
     */
    private HereToSlayParameterBinding binding;


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


        //todo remove, used to automatically go to hub
        //binding.saveSettingsButton.callOnClick();
    }

    /**
     * Initialize the increase and decrease buttons to change the value of the player count textView
     * Bound the value of the textView by PLAYER_MIN and PLAYER_MAX
     */
    private void setPlayerCount(){
        binding.decreasePlayerCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.playerCountEditText.getText().toString());
            if (value > PLAYER_MIN) {
                value--;
                String text = Integer.toString(value);
                binding.playerCountEditText.setText(text);
            }
        });

        binding.increasePlayerCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.playerCountEditText.getText().toString());
            if (value < PLAYER_MAX){
                value++;
                String text = Integer.toString(value);
                binding.playerCountEditText.setText(text);
            }
        });

        binding.playerCountEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.playerCountEditText.getText().toString();
                if (text.isEmpty()) {
                    String text2 = Integer.toString(PLAYER_MIN);
                    binding.playerCountEditText.setText(text2);
                }
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(PLAYER_MIN, value);
                    newValue = min(PLAYER_MAX, newValue);
                    if (newValue != value) {
                        String text2 = Integer.toString(newValue);
                        binding.playerCountEditText.setText(text2);
                    }
                }
            }
            return false;
        });

    }

    /**
     * Fill the spinner with some values
     */
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
        //todo when click on here to sleigh disable all others
        //todo change min and max values and eventually change the values in text views
    }

    /**
     * Initialize the increase and decrease buttons to change the value of the action point count textView
     * Bound the value of the textView by ACTION_POINT_MIN and ACTION_POINT_MAX
     */
    private void setActionPoint(){
        binding.decreaseActionPoint.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.actionPointEditText.getText().toString());
            if (value > ACTION_POINT_MIN) {
                value--;
                String text = Integer.toString(value);
                binding.actionPointEditText.setText(text);
            }
        });

        binding.increaseActionPoint.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.actionPointEditText.getText().toString());
            if (value < ACTION_POINT_MAX){
                value++;
                String text = Integer.toString(value);
                binding.actionPointEditText.setText(text);
            }
        });

        binding.actionPointEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.actionPointEditText.getText().toString();
                if (text.isEmpty()) {
                    String text2 = Integer.toString(ACTION_POINT_MIN);
                    binding.actionPointEditText.setText(text2);
                }
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(ACTION_POINT_MIN, value);
                    newValue = min(ACTION_POINT_MAX, newValue);
                    if (newValue != value) {
                        String text2 = Integer.toString(newValue);
                        binding.actionPointEditText.setText(text2);
                    }
                }
            }
            return false;
        });
    }

    /**
     * Initialize the increase and decrease buttons to change the value of the card limit textView
     * Bound the value of the textView by CARD_LIMIT_MIN and CARD_LIMIT_MAX
     */
    private void setCardLimit(){
        binding.decreaseCardLimit.setOnClickListener(v -> {
            String text = binding.cardLimitEditText.getText().toString();

            int value;
            if (text.equals(INFINITY)) value = CARD_LIMIT_MAX+1;
            else value = Integer.parseInt(text);

            if (value > CARD_LIMIT_MIN) {
                value--;
                String text2 = Integer.toString(value);
                binding.cardLimitEditText.setText(text2);
            }
        });

        binding.increaseCardLimit.setOnClickListener(v -> {
            String text = binding.cardLimitEditText.getText().toString();

            if (!text.equals(INFINITY)) {
                int value = Integer.parseInt(text);
                if (value <= CARD_LIMIT_MAX) {
                    value++;
                    if (value == CARD_LIMIT_MAX+1) binding.cardLimitEditText.setText(INFINITY);
                    else {
                        String text2 = Integer.toString(value);
                        binding.cardLimitEditText.setText(text2);
                    }
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
                        if (value > CARD_LIMIT_MAX) binding.cardLimitEditText.setText(INFINITY);
                        else {
                            String text2 = Integer.toString(newValue);
                            binding.cardLimitEditText.setText(text2);
                        }
                    }
                } catch (NumberFormatException e) {
                    if (!text.equals(INFINITY)) {
                        String text2 = Integer.toString(CARD_LIMIT_MIN);
                        binding.cardLimitEditText.setText(text2);
                    }
                }
            }
            return false;
        });
    }

    /**
     * Initialize the increase and decrease buttons to change the value of the monster goal textView
     * Bound the value of the textView by MONSTER_GOAL_MIN and MONSTER_GOAL_MAX
     */
    private void setMonsterGoal(){
        binding.decreaseMonsterCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.monsterCountEditText.getText().toString());
            if (value > MONSTER_GOAL_MIN) {
                value--;
                String text = Integer.toString(value);
                binding.monsterCountEditText.setText(text);
            }
        });

        binding.increaseMonsterCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.monsterCountEditText.getText().toString());
            if (value < MONSTER_GOAL_MAX){
                value++;
                String text = Integer.toString(value);
                binding.monsterCountEditText.setText(text);
            }
        });

        binding.monsterCountEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.monsterCountEditText.getText().toString();
                if (text.isEmpty()) {
                    String text2 = Integer.toString(MONSTER_GOAL_MIN);
                    binding.monsterCountEditText.setText(text2);
                }
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(MONSTER_GOAL_MIN, value);
                    newValue = min(MONSTER_GOAL_MAX, newValue);
                    if (newValue != value) {
                        String text2 = Integer.toString(newValue);
                        binding.monsterCountEditText.setText(text2);
                    }
                }
            }
            return false;
        });
    }

    /**
     * Initialize the increase and decrease buttons to change the value of the hero count textView
     * Bound the value of the textView by HERO_COUNT_MIN and HERO_COUNT_MAX
     */
    private void setHeroCount(){
        binding.decreaseHeroCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.heroCountEditText.getText().toString());
            if (value > HERO_COUNT_MIN) {
                value--;
                String text = Integer.toString(value);
                binding.heroCountEditText.setText(text);
            }
        });

        binding.increaseHeroCount.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.heroCountEditText.getText().toString());
            if (value < HERO_COUNT_MAX){
                value++;
                String text = Integer.toString(value);
                binding.heroCountEditText.setText(text);
            }
        });

        binding.heroCountEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.heroCountEditText.getText().toString();
                if (text.isEmpty()) {
                    String text2 = Integer.toString(HERO_COUNT_MIN);
                    binding.heroCountEditText.setText(text2);
                }
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(HERO_COUNT_MIN, value);
                    newValue = min(HERO_COUNT_MAX, newValue);
                    if (newValue != value) {
                        String text2 = Integer.toString(newValue);
                        binding.heroCountEditText.setText(text2);
                        }
                }
            }
            return false;
        });
    }

    /**
     * Initialize the increase and decrease buttons to change the value of the category cap textView
     * Bound the value of the textView by CATEGORY_CAP_MIN and CATEGORY_CAP_MAX
     */
    private void setCategoryCap(){
        binding.decreaseCategoryCap.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.categoryCapEditText.getText().toString());
            if (value > CATEGORY_CAP_MIN) {
                value--;
                String text = Integer.toString(value);
                binding.categoryCapEditText.setText(text);
            }
        });

        binding.increaseCategoryCap.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.categoryCapEditText.getText().toString());
            if (value < CATEGORY_CAP_MAX){
                value++;
                String text = Integer.toString(value);
                binding.categoryCapEditText.setText(text);
            }
        });

        binding.categoryCapEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.categoryCapEditText.getText().toString();
                if (text.isEmpty()) {
                    String text2 = Integer.toString(CATEGORY_CAP_MIN);
                    binding.categoryCapEditText.setText(text2);
                }
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(CATEGORY_CAP_MIN, value);
                    newValue = min(CATEGORY_CAP_MAX, newValue);
                    if (newValue != value) {
                        String text2 = Integer.toString(newValue);
                        binding.categoryCapEditText.setText(text2);
                    }
                }
            }
            return false;
        });
    }

    /**
     * Initialize the increase and decrease buttons to change the value of the forced reroll textView
     * Bound the value of the textView by FORCED_LEADER_REROLL_MIN and FORCED_LEADER_REROLL_MIN
     */
    private void setForcedReroll(){
        binding.decreaseForcedReroll.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.forcedRerollEditText.getText().toString());
            if (value > FORCED_LEADER_REROLL_MIN) {
                value--;
                String text = Integer.toString(value);
                binding.forcedRerollEditText.setText(text);
            }
        });

        binding.increaseForcedReroll.setOnClickListener(v -> {
            int value = Integer.parseInt(binding.forcedRerollEditText.getText().toString());
            if (value < FORCED_LEADER_REROLL_MAX){
                value++;
                String text = Integer.toString(value);
                binding.forcedRerollEditText.setText(text);
            }
        });

        binding.forcedRerollEditText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP)
            {
                String text = binding.forcedRerollEditText.getText().toString();
                if (text.isEmpty()) {
                    String text2 = Integer.toString(FORCED_LEADER_REROLL_MIN);
                    binding.forcedRerollEditText.setText(text2);
                }
                else{
                    int value = Integer.parseInt(text);
                    int newValue = max(FORCED_LEADER_REROLL_MIN, value);
                    newValue = min(FORCED_LEADER_REROLL_MAX, newValue);
                    if (newValue != value) {
                        String text2 = Integer.toString(newValue);
                        binding.forcedRerollEditText.setText(text2);
                    }
                }
            }
            return false;
        });
    }

    /**
     * Save every textView field values inside of the Setting class
     * Then start the Hub activity
     */
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
            if (text.equals(INFINITY)) Settings.cardLimit = -1;
            else Settings.cardLimit = Integer.parseInt(text);

            Settings.monsterGoal = Integer.parseInt(binding.monsterCountEditText.getText().toString());
            Settings.heroCount = Integer.parseInt(binding.heroCountEditText.getText().toString());
            Settings.categoryCap = Integer.parseInt(binding.categoryCapEditText.getText().toString());

            Settings.forcedReroll = Integer.parseInt(binding.forcedRerollEditText.getText().toString());


            //load hub view
            Hub.isHost = true;
            Intent intent = new Intent(this, Hub.class);
            startActivity(intent);
        });
    }

    /**
     * Fill every textView field values with the Setting class attributes
     */
    private void loadSettings(){

        String text = Integer.toString(Settings.playerNumber);
        binding.playerCountEditText.setText(text);
        binding.gameStyleSpinner.setSelection(Settings.gameRule);

        binding.warriosDruids.setChecked(Settings.warriorDruid);
        binding.berserkersNecromancers.setChecked(Settings.berserkerNecromancer);
        binding.sorcerers.setChecked(Settings.sorcerer);
        binding.monster.setChecked(Settings.monster);
        binding.leader.setChecked(Settings.leader);
        binding.hereToSleigh.setChecked(Settings.hereToSleigh);

        text = Integer.toString(Settings.actionPoint);
        binding.actionPointEditText.setText(text);

        int value = Settings.cardLimit;
        if (value < 0){
            binding.cardLimitEditText.setText(INFINITY);
        }
        else {
            text = Integer.toString(value);
            binding.cardLimitEditText.setText(text);
        }

        text = Integer.toString(Settings.monsterGoal);
        binding.monsterCountEditText.setText(text);

        text = Integer.toString(Settings.heroCount);
        binding.heroCountEditText.setText(text);

        text = Integer.toString(Settings.categoryCap);
        binding.categoryCapEditText.setText(text);


        text = Integer.toString(Settings.forcedReroll);
        binding.forcedRerollEditText.setText(text);
    }

    /**
     * Reset every Setting class attributes to their original state
     * Then fill every textView field values with the Setting class attributes
     */
    private void setReset(){
        binding.resetButton.setOnClickListener(v -> {
            Settings.resetSettings();
            loadSettings();
        });
    }

    /**
     * Toggle the visibility of the advanced option
     */
    private void setMoreOption(){
        binding.moreOptionButton.setOnClickListener(v -> {
            if (moreOption) {
                binding.advancedOption.setVisibility(View.GONE);
                binding.moreOptionButton.setText(getResources().getString(R.string.more_options));
                moreOption = false;
            }
            else {
                binding.advancedOption.setVisibility(View.VISIBLE);
                binding.moreOptionButton.setText(getResources().getString(R.string.less_options));
                moreOption = true;
            }
        });
    }
}
