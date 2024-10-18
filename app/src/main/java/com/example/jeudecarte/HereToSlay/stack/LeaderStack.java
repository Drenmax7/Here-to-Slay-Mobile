package com.example.jeudecarte.HereToSlay.stack;

import com.example.jeudecarte.HereToSlay.card.Leader;

import java.util.ArrayList;

/**
 * A stack containing leader cards
 *
 * @see Stack
 * @see Leader
 */
public class LeaderStack extends Stack<Leader>{
    //Constructor

    /**
     * Create a stack filled with leader cards whose names are given by the param
     *
     * @param leaderList The list containing all names
     */
    public LeaderStack(ArrayList<String> leaderList){
        cardsList = new ArrayList<>();

        for (String name : leaderList){
            Leader leader = new Leader(name);
            cardsList.add(leader);
        }

        shuffle();
    }
}
