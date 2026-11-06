
package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;

import board.BoardManager;
import board.Board;
import board.Tile;
import data.LevelData;
import gameplay.MergeSystem;
import input.MoveHandler;
import managers.AnimationManager;
import managers.AssetManager;
import managers.InputManager;
import rendering.BoardRenderer;
import managers.ScreenManager;
import ui.ActionButton;

import static ui.TextureUtils.createRoundedTexture;

public class GameScreen extends BaseScreen {
    // Virtual world dimensions
    private static final float WORLD_WIDTH = 1920f;
    private static final float WORLD_HEIGHT = 1080f;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;

    private AssetManager assetManager;
    private ShapeRenderer shapeRenderer;
    private BoardManager boardManager;
    private AnimationManager animationManager;
    private InputManager inputManager;

    private BoardRenderer boardRenderer;
    private MoveHandler moveHandler;
    private MergeSystem mergeSystem;

    // Quit button and confirmation dialog
    private Stage uiStage;
    private Texture quitIcon;
    private Texture quitButtonTexture;
    private Texture dialogBackgroundTexture;
    private Texture okButtonTexture;
    private BitmapFont uiFont;
    private Table quitButton;
    private Table dialogOverlay;

    private Texture dialogOverlayTexture;
    private final ScreenManager screenManager;

    private final String mode;
    private final LevelData level;

    // Asset path
    private static final String QUIT_ICON_PATH = "images/quitgame.png";

    public GameScreen(ScreenManager screenManager, String mode, LevelData level) {
        this.screenManager = screenManager;
        this.mode = mode;
        this.level = level;
    }

    @Override
    public void show() {
        // Initialize camera
        camera = new OrthographicCamera();

        // Initialize viewport
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        batch = new SpriteBatch();

        assetManager = new managers.AssetManager();
        shapeRenderer = new ShapeRenderer();

        // Initialize board using selected level dimensions
        boardManager = new BoardManager(mode, level.getRows(), level.getCols());

        animationManager = new AnimationManager(boardManager.getBoard().getRows(), boardManager.getBoard().getCols());

        boardRenderer = new BoardRenderer(batch, assetManager, animationManager, boardManager.getTileSize(),
            boardManager.getBoardX(), boardManager.getBoardY());

        moveHandler = new MoveHandler(animationManager);

        inputManager = new InputManager(viewport, moveHandler, boardManager);
        mergeSystem = new MergeSystem(inputManager);

        // Initialize UI
        initializeQuitUI();

        // Allow both UI and gameplay input
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(inputManager);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void initializeQuitUI() {
        uiStage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        // Load quit icon
        quitIcon = new Texture(Gdx.files.internal(QUIT_ICON_PATH));

        // Generate UI font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/segoeuib.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 28;
        parameter.color = Color.WHITE;
        uiFont = generator.generateFont(parameter);
        generator.dispose();

        // Create button backgrounds
        quitButtonTexture = createRoundedTexture(new Color(1f, 0.35f, 0.35f, 1f), 256, 128, 10);
        okButtonTexture = createRoundedTexture(new Color(0.61f, 0.93f, 0.94f, 1f), 256, 128, 16);

        dialogBackgroundTexture = createRoundedTexture(new Color(0.08f, 0.32f, 0.18f, 1f), 512, 300, 16);

        quitButton = new ActionButton("Quit", quitIcon, new Color(1f, 0.35f, 0.35f, 1f),
            this::showQuitConfirmation);

        Table topBar = new Table();
        topBar.setFillParent(true);
        topBar.top().left();
        topBar.add(quitButton).size(165, 65).padTop(20).padLeft(20);

        uiStage.addActor(topBar);
    }

    private void showQuitConfirmation() {
        // Prevent multiple dialogs
        if (dialogOverlay != null) return;

        dialogOverlay = new Table();

        dialogOverlay.setFillParent(true);

        dialogOverlay.setTouchable(Touchable.enabled);

        // Dark transparent overlay
        Pixmap overlayPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        overlayPixmap.setColor(0f, 0f, 0f, 0.55f);
        overlayPixmap.fill();
        Texture overlayTexture = new Texture(overlayPixmap);
        overlayPixmap.dispose();

        dialogOverlay.setBackground(new TextureRegionDrawable(new TextureRegion(overlayTexture)));

        // Dialog panel
        Table panel = new Table();
        panel.setBackground(new TextureRegionDrawable(new TextureRegion(dialogBackgroundTexture)));

        Label.LabelStyle messageStyle = new Label.LabelStyle(uiFont, Color.WHITE);

        Label message = new Label("Are you sure you want to quit this game?", messageStyle);

        message.setAlignment(Align.center);
        message.setWrap(true);
        panel.add(message).width(500).pad(35).row();

        // Buttons
        TextButton okButton = createDialogButton("OK", okButtonTexture, Color.BLACK);
        TextButton cancelButton = createDialogButton("Cancel", quitButtonTexture, Color.WHITE);

        Table buttonTable = new Table();
        buttonTable.add(okButton).size(150, 60).padRight(20);
        buttonTable.add(cancelButton)
            .size(150, 60);

        panel.add(buttonTable).padBottom(30);

        dialogOverlay.add(panel).width(600).height(260).center();

        uiStage.addActor(dialogOverlay);

        // Consume clicks on the background
        dialogOverlay.addListener(new ClickListener() {

            @Override
            public void clicked(
                InputEvent event,
                float x,
                float y
            ) {
                event.stop();
            }
        });

        // OK: Return to game mode selection
        okButton.addListener(new ClickListener() {

            @Override
            public void clicked(
                InputEvent event,
                float x,
                float y
            ) {screenManager.showGameModeMenu();
            }
        });

        // Cancel: Continue playing
        cancelButton.addListener(new ClickListener() {

            @Override
            public void clicked(
                InputEvent event,
                float x,
                float y
            ) {
                closeQuitConfirmation();
            }
        });

        // Keep overlay texture available for disposal
        dialogOverlayTexture = overlayTexture;
    }

    private TextButton createDialogButton(String text, Texture background, Color textColor) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.font = uiFont;
        style.fontColor = textColor;
        style.up = new TextureRegionDrawable(new TextureRegion(background));
        style.down = new TextureRegionDrawable(new TextureRegion(background));

        return new TextButton(text, style);
    }

    private void closeQuitConfirmation() {
        if (dialogOverlay != null) {
            dialogOverlay.remove();
            dialogOverlay = null;
        }
        if (dialogOverlayTexture != null) {
            dialogOverlayTexture.dispose();
            dialogOverlayTexture = null;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.2f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderBackground();

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        moveHandler.update(boardManager, mergeSystem, delta);
        boardRenderer.render(boardManager.getBoard());

        // Render UI on top of gameplay
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (uiStage != null) {
            uiStage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void dispose() {
        if (uiStage != null) {
            uiStage.dispose();
        }

        if (quitIcon != null) {
            quitIcon.dispose();
        }

        if (quitButtonTexture != null) {
            quitButtonTexture.dispose();
        }

        if (dialogBackgroundTexture != null) {
            dialogBackgroundTexture.dispose();
        }

        if (okButtonTexture != null) {
            okButtonTexture.dispose();
        }

        if (dialogOverlayTexture != null) {
            dialogOverlayTexture.dispose();
        }

        if (uiFont != null) {
            uiFont.dispose();
        }

        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }

        if (batch != null) {
            batch.dispose();
        }

        if (assetManager != null) {
            assetManager.dispose();
        }
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }
}
