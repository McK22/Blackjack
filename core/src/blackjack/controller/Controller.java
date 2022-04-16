package blackjack.controller;

import blackjack.model.Deck;
import blackjack.model.Figure;
import blackjack.model.Hand;
import blackjack.view.GameScreen;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;

public class Controller {
    private final Hand player = new Hand();
    private final Hand dealer = new Hand();
    private final GameScreen gameScreen;

    private int playerMoney;
    private Deck deck;
    private int bet;
    private boolean[] actionAvailable;
    private boolean bettingActive;
    private boolean endOfDeal;

    public Controller(GameScreen gameScreen, int startMoney, int numberOfDecks){
        this.gameScreen = gameScreen;
        playerMoney = startMoney;
        deck = new Deck(numberOfDecks);
        actionAvailable = new boolean[Action.values().length];
        Arrays.fill(actionAvailable, false);
        bettingActive = true;
    }

    public boolean setBet(int bet){
        if(bet > playerMoney)
            return false;
        this.bet = bet;
        playerMoney -= bet;
        deal();
        bettingActive = false;
        endOfDeal = false;
        return true;
    }

    private void deal(){
        //TODO - check deck size
        player.draw(deck);
        player.draw(deck);
        dealer.draw(deck);
        dealer.draw(deck);
        if(player.getValue() == 21)
            end(false);
        actionAvailable[Action.HIT.ordinal()] = true;
        actionAvailable[Action.STAND.ordinal()] = true;
        actionAvailable[Action.SURRENDER.ordinal()] = true;
        actionAvailable[Action.DOUBLE_DOWN.ordinal()] = bet <= playerMoney;
        actionAvailable[Action.SPLIT.ordinal()] = player.getCards().get(0).getFigure() == player.getCards().get(1).getFigure();
        actionAvailable[Action.INSURANCE.ordinal()] = dealer.getCards().get(0).getFigure() == Figure.ACE;
    }

    private void hit(){
        player.draw(deck);
        actionAvailable[Action.SURRENDER.ordinal()] = false;
        if(player.getValue() >= 21)
            end(false);
    }

    private void stand(){
        end(false);
    }

    private void doubleDown(){
        bet *= 2;
        player.draw(deck);
        end(false);
    }

    private void surrender(){
        playerMoney += bet / 2;
        end(true);
    }

    private void split(){

    }

    private void insurance(){

    }

    public Hand getPlayerHand(){
        return player;
    }

    public Hand getDealerHand(){
        return dealer;
    }

    public int getPlayerMoney(){
        return playerMoney;
    }
    public boolean isActionAvailable(@NotNull Action action){
        return actionAvailable[action.ordinal()];
    }

    public boolean isBettingActive(){
        return bettingActive;
    }

    public LinkedList<Action> getAvailableActions(){
        LinkedList<Action> result = new LinkedList<>();
        for(Action action: Action.values())
            if(actionAvailable[action.ordinal()])
                result.add(action);
        return result;
    }

    public boolean isEndOfDeal(){
        return endOfDeal;
    }

    public void performAction(@NotNull Action action){
        switch(action){
            case HIT:
                hit();
                break;
            case STAND:
                stand();
                break;
            case SPLIT:
                split();
                break;
            case DOUBLE_DOWN:
                doubleDown();
                break;
            case INSURANCE:
                insurance();
                break;
            case SURRENDER:
                surrender();
                break;
        }
    }

    private void end(boolean surrender){
        Arrays.fill(actionAvailable, false);
        endOfDeal = true;
        if(player.getValue() > 21 || surrender){
            gameScreen.setEndOfDealText("Przegrałeś");
        }else{
            while(dealer.getValue() < 17)
                dealer.draw(deck);
            if(dealer.getValue() > 21 || dealer.getValue() < player.getValue()) {
                gameScreen.setEndOfDealText("Wygrałeś");
                playerMoney += 2 * bet;
            }else
                gameScreen.setEndOfDealText("Przegrałeś");
        }
    }

    public void activateBetting(){
        player.dump(deck);
        dealer.dump(deck);
        bettingActive = true;
    }
}
