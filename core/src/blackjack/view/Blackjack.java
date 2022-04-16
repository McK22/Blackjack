package blackjack.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class Blackjack extends Game {
	final int screenWidth;
	final int screenHeight;

	ShapeRenderer shapeRenderer;
	BitmapFont font;
	SpriteBatch batch;

	public Blackjack(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void create(){
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont(Gdx.files.internal("f.fnt"));
		batch = new SpriteBatch();
		shapeRenderer.setAutoShapeType(true);
		setScreen(new StartScreen(this));
		//setScreen(new GameScreen(this, 1000));
	}

	@Override
	public void render(){
		super.render();
	}
	
	@Override
	public void dispose(){
		shapeRenderer.dispose();
		font.dispose();
		batch.dispose();
	}
}
