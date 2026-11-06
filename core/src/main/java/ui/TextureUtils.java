package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class TextureUtils {
    public static Texture createRoundedTexture(Color color, int width, int height, int radius) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);

        int rgba = Color.rgba8888(color);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int nearestX = Math.max(
                    radius,
                    Math.min(width - radius - 1, x)
                );

                int nearestY = Math.max(
                    radius,
                    Math.min(height - radius - 1, y)
                );

                int dx = x - nearestX;
                int dy = y - nearestY;

                if (dx * dx + dy * dy <= radius * radius) pixmap.drawPixel(x, y, rgba);
            }
        }

        Texture texture = new Texture(pixmap);

        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        pixmap.dispose();

        return texture;
    }

    public static Texture backIcon = new Texture(Gdx.files.internal("images/back.png"));

    public static void dispose() {
        if (backIcon != null) {
            backIcon.dispose();
            backIcon = null;
        }
    }
}
