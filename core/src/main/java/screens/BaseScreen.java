package screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rendering.BackgroundRenderer;

public abstract class BaseScreen extends ScreenAdapter {
    protected SpriteBatch batch;
    protected BackgroundRenderer background;

    public BaseScreen() {
        batch = new SpriteBatch();
        background = new BackgroundRenderer();
    }

    protected void renderBackground() {
        batch.begin();
        background.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
    }
}
