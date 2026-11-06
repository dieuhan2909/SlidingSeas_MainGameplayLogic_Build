package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import managers.ScreenManager;

public class AboutScreen extends BaseScreen {
    private final ScreenManager screenManager;

    private Stage stage;
    private BitmapFont font;

    public AboutScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(1920f, 1080f));

        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/segoeuib.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 40;
        parameter.color = Color.BLACK;

        font = generator.generateFont(parameter);
        generator.dispose();

        Table root = new Table();
        root.setFillParent(true);
        root.center();

        stage.addActor(root);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        Label title = new Label("ABOUT", labelStyle);

        title.setAlignment(Align.center);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        TextButton backButton = new TextButton("BACK", buttonStyle);

        root.add(title).padBottom(50);
        root.row();
        root.add(backButton).size(250, 80);

        backButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    screenManager.showMainMenuScreen();
                }
            }
        );
    }

    @Override
    public void render(float delta) {Gdx.gl.glClearColor(0.1f, 0.2f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderBackground();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (font != null) {
            font.dispose();
        }
    }
}
