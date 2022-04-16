package blackjack.model;

public class Card {
    private final Figure figure;
    private final Suit suit;

    public Card(Figure figure, Suit suit){
        this.figure = figure;
        this.suit = suit;
    }

    public String toString(){
        return figure + " OF " + suit;
    }

    public int getValue(){
        switch(figure){
            case JACK:
            case QUEEN:
            case KING:
                return 10;
            case ACE:
                return 11;
            default:
                return figure.ordinal() + 2;
        }
    }

    public int getOrdinal(){
        return suit.ordinal() * 13 + figure.ordinal();
    }

    public Suit getSuit(){
        return suit;
    }

    public Figure getFigure(){
        return figure;
    }
}
