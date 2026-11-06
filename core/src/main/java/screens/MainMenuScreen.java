package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import managers.ScreenManager;
import ui.ActionButton;
import ui.GameModeButton;
import ui.TextureUtils;


import java.util.ArrayList;
import java.util.List;

import static ui.TextureUtils.createRoundedTexture;

public class MainMenuScreen extends BaseScreen {
    private static final float WORLD_WIDTH = 1920f;
    private static final float WORLD_HEIGHT = 1080f;

    private static final String LOGO_PATH = "images/logo.png";

    private static final String ICON_PATH = "images/gamemode_icons/";

    private final ScreenManager screenManager;

    private Stage stage;
    private Table root;
    private ActionButton backButton;

    public static final String[] modeNames = {
        "To Shelter",
        "Rescue Boat",
        "Against Pirate",
        "Precious Items",
        "To The Shore"
    };

    private Texture logoTexture;
    private final List<Texture> modeTextures = new ArrayList<>();

    private Texture cyanTexture;
    private Texture buttonTexture;

    private BitmapFont menuFont;
    private BitmapFont modeFont;

    private Skin skin;

    public MainMenuScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        Gdx.input.setInputProcessor(stage);

        loadAssets();
        createSkin();
        createMainMenu();
    }

    // ==========================================
    // LOAD ASSETS
    // ==========================================
    private void loadAssets() {
        logoTexture = new Texture(Gdx.files.internal(LOGO_PATH));

        for(int i = 0; i < 5; i++) {
            Texture t = new Texture(Gdx.files.internal(ICON_PATH + modeNames[i] + ".png"));
            modeTextures.add(t);
        }

        // Generate Segoe UI Bold fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/segoeuib.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.color = Color.BLACK;
        parameter.size = 40;
        menuFont = generator.generateFont(parameter);
        parameter.size = 30;
        modeFont = generator.generateFont(parameter);
        generator.dispose();

        // Cyan background textures
        cyanTexture = createRoundedTexture(new Color(0.61f, 0.93f, 0.94f, 1f), 256, 256, 10);

        buttonTexture = createRoundedTexture(new Color(0.35f, 0.85f, 0.87f, 1f), 256, 256, 16);
    }

    // ==========================================
    // CREATE SKIN
    // ==========================================
    private void createSkin() {
        skin = new Skin();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = menuFont;
        style.fontColor = Color.BLACK;
        style.up = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        skin.add("default", style);
    }

    // ==========================================
    // MAIN MENU
    // ==========================================
    private void createMainMenu() {
        if (backButton != null) {
            backButton.remove();
            backButton.dispose();
            backButton = null;
        }

        if(root != null) root.clearChildren();

        root = new Table();
        root.setFillParent(true);
        root.center();

        stage.addActor(root);

        Image logo = new Image(logoTexture);
        logo.setScaling(com.badlogic.gdx.utils.Scaling.fit);

        TextButton newGameButton = new TextButton("NEW GAME", skin);
        TextButton aboutButton = new TextButton("ABOUT", skin);

        root.add(logo).size(450, 220).padBottom(35);
        root.row();
        root.add(newGameButton).size(300, 80).padBottom(20);
        root.row();
        root.add(aboutButton).size(300, 80);

        // New Game
        newGameButton.addListener(
            new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    createGameModeMenu();
                }
            }
        );

        // About
        aboutButton.addListener(
            new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    screenManager.showAboutScreen();
                }
            }
        );
    }

    // ==========================================
    // GAME MODE MENU
    // ==========================================

    public void createGameModeMenu() {
        root.clearChildren();

        initializeBackButton();

        // Screen title
        Label.LabelStyle titleStyle = new Label.LabelStyle(menuFont, Color.BLACK);

        Label title = new Label("SELECT GAME MODE", titleStyle);

        title.setAlignment(Align.center);

        root.add(title).colspan(5).padBottom(60);
        root.row();

        for (int i = 0; i < modeNames.length; i++) {
            final String modeName = modeNames[i];

            GameModeButton modeButton = new GameModeButton(modeName, modeTextures.get(i), modeFont,
                new TextureRegionDrawable(cyanTexture), () -> screenManager.showLevelSelectScreen(modeName));

            root.add(modeButton).size(220, 280).pad(12);
        }
    }

    private void initializeBackButton() {
        backButton = new ActionButton("Back", TextureUtils.backIcon, Color.valueOf("#A8F792"), this::createMainMenu);

        Table topBar = new Table();
        topBar.setFillParent(true);
        topBar.top().left();

        topBar.add(backButton).size(165, 65).padTop(20).padLeft(20);

        stage.addActor(topBar);
    }

    public void showGameModeMenu() {
        createGameModeMenu();
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
        stage.getViewport().update(width, height, true);
    }

    // ==========================================
    // DISPOSE
    // ==========================================
    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (logoTexture != null) {
            logoTexture.dispose();
        }
        for (Texture texture : modeTextures) {
            texture.dispose();
        }
        if (cyanTexture != null) { cyanTexture.dispose(); }
        if (buttonTexture != null) { buttonTexture.dispose(); }
        if (menuFont != null) { menuFont.dispose(); }
        if (modeFont != null) { modeFont.dispose(); }
        if (skin != null) { skin.dispose(); }
    }
}
