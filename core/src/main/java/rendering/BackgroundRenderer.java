package rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BackgroundRenderer {
    private final Texture background;

    public BackgroundRenderer() {
        background = new Texture(Gdx.files.internal("images/background.png"));
    }

    public void render(SpriteBatch batch) {
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void dispose() {
        background.dispose();
    }
}
