package blackjack.view;

import blackjack.controller.Action;
import blackjack.controller.Controller;
import blackjack.model.Card;
import blackjack.model.Deck;
import blackjack.model.Hand;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.LinkedList;
import java.util.Locale;

public class GameScreen implements Screen {
    private final Controller controller;
    private final int cardWidth = 150;
    private final int cardHeight = 225;
    private final int cardGap = 2;
    private final Blackjack game;
    private final Texture[] cards;
    private final Texture revers;
    private final GlyphLayout[] actionGlyphLayout;
    private final GlyphLayout moneyGlyphLayout;
    private final GlyphLayout endOfDealGlyphLayout;

    private String moneyText = "";
    private int cursorCounter = 0;

    final int buttonWidth;
    final int buttonHeight;
    final int buttonGap;

    public GameScreen(Blackjack game, int startMoney){
        controller = new Controller(this, startMoney, 1);
        this.game = game;

        //create cards
        Deck deck = new Deck(1);
        cards = new Texture[52];
        for(int i = 0; i < cards.length; i++) {
            Card card = deck.draw();
            String name = card.getFigure().toString().toLowerCase(Locale.ROOT) + "_" + card.getSuit().toString().toLowerCase(Locale.ROOT);
            cards[card.getOrdinal()] = new Texture(Gdx.files.internal("cards/" + name + ".png"));
        }

        revers = new Texture(Gdx.files.internal("cards/revers.png"));

        game.font.getData().setScale(0.5f);

        actionGlyphLayout = new GlyphLayout[Action.values().length];
        for(int i = 0; i < actionGlyphLayout.length; i++) {
            actionGlyphLayout[i] = new GlyphLayout();
            actionGlyphLayout[i].setText(game.font, Action.values()[i].toString());
        }

        game.font.getData().setScale(1);

        buttonGap = 10;
        buttonWidth = game.screenWidth / Action.values().length - 20;
        buttonHeight = game.screenHeight - 2 * cardHeight - 150;

        moneyGlyphLayout = new GlyphLayout();
        endOfDealGlyphLayout = new GlyphLayout();

        Gdx.input.setInputProcessor(new GameInputProcessor(game, this, controller));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0.6f, 0.2f, 0);
        if(controller.isBettingActive()){
            cursorCounter++;
            displayBet(cursorCounter);
            if(cursorCounter == 100)
                cursorCounter = 0;
            return;
        }

        displayButtons();

        game.batch.begin();
        if(controller.isEndOfDeal())
            displayEndOfDeal();
        displayPlayerHand();
        displayDealerHand();
        displayMoney();
        game.batch.end();
    }

    private void displayButtons(){
        LinkedList<Action> actions = controller.getAvailableActions();
        game.font.getData().setScale(0.5f);
        game.shapeRenderer.setColor(Color.WHITE);

        int totalWidth = actions.size() * buttonWidth + (actions.size() - 1) * buttonGap;
        int x = (game.screenWidth - totalWidth) / 2;
        int y = (game.screenHeight - buttonHeight) / 2;
        for(Action action: actions){
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.rect(x, y, buttonWidth, buttonHeight);
            game.shapeRenderer.end();

            int textX = (buttonWidth - (int)actionGlyphLayout[action.ordinal()].width) / 2 + x;
            int textY = (buttonHeight + (int)actionGlyphLayout[action.ordinal()].height) / 2 + y;
            game.batch.begin();
            game.font.draw(game.batch, actionGlyphLayout[action.ordinal()], textX, textY);
            game.batch.end();

            x += buttonWidth + buttonGap;
        }
    }

    void addToMoneyText(char c){
        if(c != '0' || moneyText.length() > 0){
            moneyText += c;
            moneyGlyphLayout.setText(game.font, moneyText);
        }
    }

    boolean eraseFromMoneyText(){
        if(moneyText.length() > 0){
            moneyText = moneyText.substring(0, moneyText.length() - 1);
            moneyGlyphLayout.setText(game.font, moneyText);
            return true;
        }
        return false;
    }

    String getMoneyText(){
        return moneyText;
    }

    public void setEndOfDealText(String text){
        endOfDealGlyphLayout.setText(game.font, text);
    }

    private void displayEndOfDeal(){
        int x = (game.screenWidth - (int)endOfDealGlyphLayout.width) / 2;
        int y = (game.screenHeight + (int)endOfDealGlyphLayout.height) / 2;
        game.font.getData().setScale(1);
        game.font.draw(game.batch, endOfDealGlyphLayout, x, y);
    }

    private void displayBet(int cursorCounter){
        int fieldWidth = 400;
        int fieldHeight = 52;
        int fieldX = (game.screenWidth - fieldWidth) / 2;
        int fieldY = (game.screenHeight - fieldHeight) / 2;

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(Color.WHITE);
        game.shapeRenderer.rect(fieldX, fieldY, fieldWidth, fieldHeight);
        if(cursorCounter > 50){
            int cursorX = fieldX + 10 + (int)moneyGlyphLayout.width;
            int cursorY = (int)(fieldY + fieldHeight * 0.05f);
            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(cursorX, cursorY, 1.5f, fieldHeight * 0.9f);
        }
        game.shapeRenderer.end();

        game.batch.begin();
        game.font.draw(game.batch, moneyGlyphLayout, fieldX + 10, (int)(fieldY + fieldHeight * 0.95f));
        game.batch.end();
    }

    private void displayDealerHand(){
        LinkedList<Card> deck = controller.getDealerHand().getCards();
        int numberOfCards = deck.size();
        if(numberOfCards > 0){
            int width = numberOfCards * cardWidth + (numberOfCards - 1) * cardGap;
            int x = (game.screenWidth - width) / 2;
            int y = game.screenHeight - 10 - cardHeight;
            if(controller.isEndOfDeal()){
                for (Card card : deck){
                    game.batch.draw(cards[card.getOrdinal()], x, y, cardWidth, cardHeight);
                    x += cardWidth + cardGap;
                }
            }else{
                for(int i = 0; i < deck.size(); i++){
                    if(i == deck.size() - 1)
                        game.batch.draw(revers, x, y, cardWidth, cardHeight);
                    else
                        game.batch.draw(cards[deck.get(i).getOrdinal()], x, y, cardWidth, cardHeight);
                    x += cardWidth + cardGap;
                }
            }
        }
    }

    private void displayPlayerHand(){
        LinkedList<Card> deck = controller.getPlayerHand().getCards();
        int numberOfCards = deck.size();
        if(numberOfCards > 0){
            int width = numberOfCards * cardWidth + (numberOfCards - 1) * cardGap;
            int x = (game.screenWidth - width) / 2;
            int y = 10;

            for(Card card: deck){
                game.batch.draw(cards[card.getOrdinal()], x, y, cardWidth, cardHeight);
                x += cardWidth + cardGap;
            }
        }
    }

    private void displayMoney(){
        game.font.getData().setScale(1);
        game.font.draw(game.batch, "Saldo: " + controller.getPlayerMoney(), 50, game.screenHeight - 100);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
