package com.example.jeudecarte.HereToSlay.stack;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A generic stack of cards
 * If inherited when there is a need for a stack of a specific card
 *
 * @param <Card> The type of card the stack is made of
 *
 * @see LeaderStack
 * @see MonsterStack
 * @see NormalCardStack
 */
abstract class Stack<Card> {
    //Attributes

    /**
     * The cards list of the stack
     */
    protected ArrayList<Card> cardsList;


    //Methods

    protected void shuffle(){
        Collections.shuffle(cardsList);
    }
}
