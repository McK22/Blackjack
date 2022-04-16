package blackjack.model;

import java.util.LinkedList;

public class Hand {
    private final LinkedList<Card> cards = new LinkedList<>();

    private int cardsValue;
    private int aces = 0;

    public void draw(Deck deck){
        Card card = deck.draw();
        cards.add(card);
        if(card.getFigure() == Figure.ACE)
            aces++;
        cardsValue += card.getValue();
        while(cardsValue > 21 && aces > 0){
            aces--;
            cardsValue -= 10;
        }
    }

    public int getValue(){
        return cardsValue;
    }

    public void dump(Deck deck){
        for(Card card: cards)
            deck.discard(card);
        cards.clear();
        cardsValue = 0;
    }

    public LinkedList<Card> getCards(){
        return cards;
    }
}
