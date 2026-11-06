
package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import managers.ScreenManager;
import managers.LevelManager;
import data.LevelData;
import ui.ActionButton;
import ui.TextureUtils;

public class LevelSelectScreen extends BaseScreen {
    private static final float WORLD_WIDTH = 1920f;
    private static final float WORLD_HEIGHT = 1080f;

    private final ScreenManager screenManager;
    private final LevelManager levelManager;
    private final String gameMode;

    private Stage stage;
    private Table root;

    private Texture buttonTexture;
    private BitmapFont menuFont;
    private ActionButton backButton;

    public LevelSelectScreen(ScreenManager screenManager, LevelManager levelManager, String gameMode) {
        if (screenManager == null || levelManager == null) {
            throw new IllegalArgumentException("ScreenManager and LevelManager cannot be null");
        }

        if (gameMode == null || gameMode.isBlank()) {
            throw new IllegalArgumentException("GameMode cannot be empty");
        }

        this.screenManager = screenManager;
        this.levelManager = levelManager;
        this.gameMode = gameMode;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        loadAssets();
        createLevelMenu();
        initializeBackButton();

        Gdx.input.setInputProcessor(stage);
    }

    // ==========================================
    // LOAD ASSETS
    // ==========================================
    private void loadAssets() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/segoeuib.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.color = Color.BLACK;
        parameter.size = 40;
        menuFont = generator.generateFont(parameter);
        generator.dispose();

        buttonTexture = TextureUtils.createRoundedTexture(new Color(0.35f, 0.85f, 0.87f, 1f), 256, 256, 16);
    }

    private void initializeBackButton() {
        backButton = new ActionButton("Back", TextureUtils.backIcon, Color.valueOf("#A8F792"),
            screenManager::showGameModeMenu);

        Table topBar = new Table();

        topBar.setFillParent(true);
        topBar.top().left();
        topBar.add(backButton).size(165, 65).padTop(20).padLeft(20);
        stage.addActor(topBar);
    }

    // ==========================================
    // CREATE LEVEL MENU
    // ==========================================
    private void createLevelMenu() {
        root.clear();

        root.setFillParent(true);
        root.center();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.font = menuFont;
        style.fontColor = Color.BLACK;
        style.up = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = menuFont;
        labelStyle.fontColor = Color.WHITE;

        Label title = new Label("SELECT LEVEL", labelStyle);

        root.add(title).colspan(3).padBottom(50);
        root.row();

        for (int i = 1; i <= 3; i++) {
            final int levelNumber = i;

            TextButton levelButton = new TextButton("Level " + i, style);
            levelButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {selectLevel(levelNumber);
                }
            });

            root.add(levelButton).width(300).height(100).pad(20);
        }
    }

    private void selectLevel(int levelNumber) {
        boolean selected = levelManager.selectLevel(gameMode, levelNumber);

        if (selected) screenManager.showGameScreen(gameMode, levelManager.getCurrentLevel());
    }

    // ==========================================
    // RENDER
    // ==========================================
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.2f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderBackground();
        stage.act(delta);
        stage.draw();
    }

    // ==========================================
    // RESIZE
    // ==========================================
    @Override
    public void resize(int width, int height) {
        if (stage != null) stage.getViewport().update(width, height, true);
    }

    // ==========================================
    // DISPOSE
    // ==========================================
    @Override
    public void dispose() {
        if (backButton != null) {
            backButton.dispose();
        }
        if (stage != null) {
            stage.dispose();
        }
        if (buttonTexture != null) {
            buttonTexture.dispose();
        }
        if (menuFont != null) {
            menuFont.dispose();
        }
    }
}
