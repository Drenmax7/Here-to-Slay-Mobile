package com.example.jeudecarte.HereToSlay.card;

/**
 * The class from which all cards inherit
 */
abstract class Card {
    /**
     * The name of the card
     */
    String name;

    /**
     * Initialize all attributes
     *
     * @param name The name of the card
     */
    Card(String name){
        this.name = name;
    }
}
