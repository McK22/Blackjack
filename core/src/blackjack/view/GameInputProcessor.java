package blackjack.view;

import blackjack.controller.Action;
import blackjack.controller.Controller;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.LinkedList;

public class GameInputProcessor implements InputProcessor {
    private final Blackjack game;
    private final GameScreen gameScreen;
    private final Controller controller;

    public GameInputProcessor(Blackjack game, GameScreen gameScreen, Controller controller){
        this.game = game;
        this.gameScreen = gameScreen;
        this.controller = controller;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(controller.isBettingActive()){
            if(keycode == Input.Keys.BACKSPACE){
                gameScreen.eraseFromMoneyText();
                return true;
            }else if(keycode == Input.Keys.ENTER){
                int bet = Integer.parseInt(gameScreen.getMoneyText());
                if(bet > 0 && bet <= controller.getPlayerMoney())
                    controller.setBet(bet);
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if(controller.isBettingActive() && character >= '0' && character <= '9') {
            gameScreen.addToMoneyText(character);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(controller.isBettingActive())
            return false;
        if(controller.isEndOfDeal()){
            while(gameScreen.eraseFromMoneyText());
            controller.activateBetting();
            return true;
        }
        if(button == Input.Buttons.LEFT){
            int y = (game.screenHeight - gameScreen.buttonHeight) / 2;
            if(screenY < y || screenY > y + gameScreen.buttonHeight)
                return false;

            LinkedList<Action> actions = controller.getAvailableActions();
            int totalWidth = actions.size() * gameScreen.buttonWidth + (actions.size() * gameScreen.buttonGap);
            int x = (game.screenWidth - totalWidth) / 2;

            for(Action action: actions){
                if(screenX >= x && screenX <= x + gameScreen.buttonWidth){
                    System.out.println(action);
                    controller.performAction(action);
                    return true;
                }
                x += gameScreen.buttonWidth + gameScreen.buttonGap;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
