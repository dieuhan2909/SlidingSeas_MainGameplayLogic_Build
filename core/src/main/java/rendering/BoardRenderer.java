package rendering;

import board.Board;
import board.Tile;

import managers.AssetManager;
import managers.AnimationManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BoardRenderer {

    private final SpriteBatch batch;
    private final AssetManager assetManager;
    private final AnimationManager animationManager;

    private final float tileSize;
    private final float boardX;
    private final float boardY;

    public BoardRenderer(
        SpriteBatch batch,
        AssetManager assetManager,
        AnimationManager animationManager,
        float tileSize,
        float boardX,
        float boardY
    ) {

        this.batch = batch;
        this.assetManager = assetManager;
        this.animationManager = animationManager;

        this.tileSize = tileSize;
        this.boardX = boardX;
        this.boardY = boardY;
    }

    public void render(Board board) {

        batch.begin();

        for (int row = 0; row < board.getRows(); row++) {

            for (int col = 0; col < board.getCols(); col++) {

                Tile tile = board.getTile(row, col);

                if (tile == null) continue;

                // Lấy texture theo type hiện tại.
                Texture texture =
                    assetManager.getTileTexture(tile.getType());

                if (texture == null) continue;

                // Vị trí logic.
                float x = boardX + tile.getCol() * tileSize;

                float y = boardY
                    + (board.getRows() - 1 - tile.getRow()) * tileSize;

                // Offset animation.
                x += animationManager.getOffsetX(tile);
                y += animationManager.getOffsetY(tile);

                // Scale animation.
                float scale = animationManager.getScale(
                    tile.getRow(),
                    tile.getCol()
                );

                float size = tileSize * scale;

                float offset = (size - tileSize) / 2f;

                batch.draw(
                    texture,
                    x - offset,
                    y - offset,
                    size,
                    size
                );
            }
        }

        batch.end();
    }
}
