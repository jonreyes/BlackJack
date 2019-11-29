package sample;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;

    public Deck(){
        this.cards = new ArrayList<Card>();
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }

    public Card draw(){
        return this.cards.get(0);
    }
    public void add(Card card){
        this.cards.add(card);
    }

    public void remove(Card card){
        this.cards.remove(card);
    }

    public void complete(){
        this.empty();
        for(Suit suit: Suit.values()){
            for(Value value: Value.values()){
                this.cards.add(new Card(suit,value));
            }
        }
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public void empty(){
        this.cards = new ArrayList<>();
    }

    public int value(){
        int v = 0;
        for(Card c : cards){
            if(c.getValue()!=Value.ACE) {
                v += c.getValue().get();
            }else{
                if(v>15) v+=1;
                else{
                    v+= c.getValue().get();
                }
            }
        }
        return v;
    }

    public String toString(){
        StringBuilder deckList = new StringBuilder();
        for(Card c: this.cards){
            deckList.append(c.toString()+"\n");
        }
        return deckList.toString();
    }
}
