package blackjack.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Deck {
    private final int size;
    private final LinkedList<Card> discardPile;

    private LinkedList<Card> deck;

    public Deck(int numberOfDecks){
        size = numberOfDecks * 52;
        deck = new LinkedList<>();
        discardPile = new LinkedList<>();
        create(numberOfDecks);
        shuffle();
    }

    public void print(){
        for(Card card : deck)
            System.out.println(card);
    }

    public Card draw(){
        return deck.poll();
    }

    private void create(int numberOfDecks){
        for(int i = 0; i < numberOfDecks; i++)
            for(Figure figure: Figure.values())
                for(Suit suit: Suit.values())
                    deck.add(new Card(figure, suit));
    }

    private void shuffle(){
        Random random = new Random();
        LinkedList<Card> temp = new LinkedList<>();
        for(int i = 0; i < size; i++){
            int index = Math.abs(random.nextInt()) % deck.size();
            temp.add(deck.get(index));
            deck.remove(index);
        }
        deck = temp;
    }

    public void discard(Card card){
        discardPile.add(card);
    }

    public void reshuffle(){
        deck.addAll(discardPile);
        discardPile.clear();
        shuffle();
    }
}
