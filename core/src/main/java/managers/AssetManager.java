package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

import board.TileType;

public class AssetManager {
    private final com.badlogic.gdx.assets.AssetManager manager;
    private final Map<Integer, String> tilePaths;

    public AssetManager() {
        manager = new com.badlogic.gdx.assets.AssetManager();

        tilePaths = new HashMap<>();
        tilePaths.put(0, "images/tile_types/empty.png");
        tilePaths.put(1, "images/tile_types/level_1.png");
        tilePaths.put(2, "images/tile_types/level_2.png");
        tilePaths.put(3, "images/tile_types/level_3.png");
        tilePaths.put(4, "images/tile_types/level_4.png");
        tilePaths.put(5, "images/tile_types/level_5.png");
        tilePaths.put(6, "images/tile_types/level_6.png");
        tilePaths.put(7, "images/tile_types/level_7.png");
        tilePaths.put(8, "images/tile_types/level_8.png");
        tilePaths.put(9, "images/tile_types/iceberg.png");
        tilePaths.put(11, "images/tile_types/coral_1.png");
        tilePaths.put(12, "images/tile_types/coral_2.png");
        tilePaths.put(13, "images/tile_types/coral_3.png");
        loadAssets();
    }

    private void loadAssets() {
        for (String path : tilePaths.values()) manager.load(path, Texture.class);

        manager.finishLoading();
    }

    public Texture getTileTexture(int type) {
        String path = tilePaths.get(type);

        if (path == null) return null;

        return manager.get(path, Texture.class);
    }

    public void dispose() {
        manager.dispose();
    }
}
