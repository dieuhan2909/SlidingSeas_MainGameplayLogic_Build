package managers;

import board.BoardManager;
import board.Tile;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

import input.GestureHandler;
import input.MoveHandler;
import input.SwipeDirection;

public class InputManager extends InputAdapter {
    private final BoardManager boardManager;
    private final GestureHandler gestureHandler;
    private final MoveHandler moveHandler;
    private final Viewport viewport;

    private final Vector3 touchPosition = new Vector3();
    private float startX;
    private float startY;
    private Tile selectedTile, adjacentTile;

    public InputManager(Viewport viewport, MoveHandler moveHandler, BoardManager boardManager) {
        this.viewport = viewport;
        this.moveHandler = moveHandler;
        this.gestureHandler = new GestureHandler();
        this.boardManager = boardManager;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (pointer != 0) {
            return false;
        }

        touchPosition.set(screenX, screenY, 0);
        viewport.unproject(touchPosition);

        startX = touchPosition.x;
        startY = touchPosition.y;

        gestureHandler.touchDown(startX, startY);

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer != 0) return false;

        touchPosition.set(screenX, screenY, 0);
        viewport.unproject(touchPosition);

        SwipeDirection direction = gestureHandler.touchUp(touchPosition.x, touchPosition.y);

        selectedTile = boardManager.getTileAt(startX, startY);
        adjacentTile = boardManager.getAdjacentTile(selectedTile, direction);

        if (selectedTile != null && direction != null) {
            moveHandler.handleMove(boardManager, selectedTile, direction);
        }

        return true;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    public Tile getAdjacentTile() {
        return adjacentTile;
    }
}
