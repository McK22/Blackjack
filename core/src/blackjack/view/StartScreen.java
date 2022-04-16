package blackjack.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class StartScreen implements Screen {
    private final int textFieldWidth = 400;
    private final int textFieldHeight = 52;
    private final float textFieldX;
    private final float textFieldY;
    private final OrthographicCamera camera;
    private final GlyphLayout glyphLayout;
    private final Blackjack game;

    private String text = "";

    int counter = 0;
    boolean cursorDisplayed = false;

    public StartScreen(Blackjack game){
        this.game = game;

        textFieldX = (game.screenWidth - textFieldWidth) / 2f;
        textFieldY = (game.screenHeight - textFieldHeight) / 2f;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.screenWidth, game.screenHeight);

        KeyInputProcessor keyInputProcessor = new KeyInputProcessor(this);
        Gdx.input.setInputProcessor(keyInputProcessor);

        glyphLayout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.2f, 0.6f, 0);
        camera.update();
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderField();
        if(counter % 50 == 0)
            cursorDisplayed = !cursorDisplayed;
        if(cursorDisplayed)
            displayCursor();
        game.shapeRenderer.end();

        game.batch.begin();
        game.font.draw(game.batch, glyphLayout, textFieldX + 5, textFieldY + textFieldHeight * 0.95f);
        game.batch.end();

        counter++;
    }

    void addToText(char c){
        if(text.length() > 0 || c != '0'){
            text += c;
            glyphLayout.setText(game.font, text);
        }
    }

    void erase(){
        if(text.length() > 0){
            text = text.substring(0, text.length() - 1);
            glyphLayout.setText(game.font, text);
        }
    }

    void startGame(){
        if(text.length() > 0){
            game.setScreen(new GameScreen(game, Integer.parseInt(text)));
            dispose();
        }
    }

    private void renderField(){
        game.shapeRenderer.setColor(new Color(1, 0.95f, 0.8f, 0));
        game.shapeRenderer.rect(textFieldX, textFieldY, textFieldWidth, textFieldHeight);
    }

    private void displayCursor(){
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(textFieldX + 10 + glyphLayout.width, textFieldY + textFieldHeight * 0.05f, 1.5f, textFieldHeight * 0.9f);
    }

    @Override
    public void show() {

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
