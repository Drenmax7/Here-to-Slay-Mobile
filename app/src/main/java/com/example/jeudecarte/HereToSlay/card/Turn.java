package com.example.jeudecarte.HereToSlay.card;

/**
 * Abstract class that all class that represents a card that can be played exclusively during ones turn inherit
 */
public abstract class Turn extends Playable{
    //Constructors

    public Turn(String name){
        super(name);
    }
}
