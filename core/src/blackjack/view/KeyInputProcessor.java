package blackjack.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

public class KeyInputProcessor implements InputProcessor {
    private final StartScreen screen;

    KeyInputProcessor(StartScreen startScreen){
        this.screen = startScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode >= Input.Keys.NUM_0 && keycode <= Input.Keys.NUM_9)
            screen.addToText((char)('0' + keycode - Input.Keys.NUM_0));
        else if(keycode == Input.Keys.BACKSPACE)
            screen.erase();
        else if(keycode == Input.Keys.ENTER)
            screen.startGame();
        else
            return false;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
