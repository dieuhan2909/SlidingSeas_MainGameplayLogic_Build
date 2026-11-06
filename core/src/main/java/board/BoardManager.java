package board;

import gameplay.MergeSystem;
import input.SwipeDirection;
import managers.InputManager;

public class BoardManager {
    private Board board;

    private final String mode;
    private final BoardGenerator generator;
//    private final InputManager inputManager;

    private final int rows;
    private final int cols;

    private final float tileSize;

    private final float boardX;
    private final float boardY;

//    private final MergeSystem mergeSystem;

    // Kích thước thế giới game
    private static final float WORLD_WIDTH = 1920f;
    private static final float WORLD_HEIGHT = 1080f;

    // Giới hạn khu vực dành cho bàn cờ
    private static final float MAX_BOARD_WIDTH = 1000f;
    private static final float MAX_BOARD_HEIGHT = 800f;

    // Kích thước tối đa của một ô
    private static final float MAX_TILE_SIZE = 120f;


    public BoardManager(String mode, int rows, int cols) {
        this.mode = mode;
        this.rows = rows;
        this.cols = cols;
//        this.inputManager = inputManager;

        // Tự động điều chỉnh kích thước ô
        tileSize = Math.min(
            MAX_TILE_SIZE,
            Math.min(
                MAX_BOARD_WIDTH / cols,
                MAX_BOARD_HEIGHT / rows
            )
        );

        // Căn giữa bàn cờ
        boardX = (WORLD_WIDTH - getBoardWidth()) / 2f;
        boardY = (WORLD_HEIGHT - getBoardHeight()) / 2f;

        generator = new BoardGenerator();
        board = generator.generate(rows, cols);
    }

    public Board getBoard() {
        return board;
    }
    public Tile getTile(int row, int col) {
        return board.getTile(row, col);
    }
    public float getTileSize() {
        return tileSize;
    }
    public float getBoardX() {
        return boardX;
    }
    public float getBoardY() {
        return boardY;
    }
    public float getBoardWidth() {
        return cols * tileSize;
    }
    public float getBoardHeight() {
        return rows * tileSize;
    }

    public Tile getTileAt(float x, float y) {

        float localX = x - boardX;
        float localY = y - boardY;

        if (localX < 0 || localY < 0 ||
            localX >= getBoardWidth() ||
            localY >= getBoardHeight()) {
            return null;
        }

        int col = (int) (localX / tileSize);
        int row = rows - 1 - (int) (localY / tileSize);

        return board.getTile(row, col);
    }

    public Tile getAdjacentTile(Tile tile, SwipeDirection direction) {
        if (tile == null || direction == null) {
            return null;
        }

        int row = tile.getRow();
        int col = tile.getCol();

        switch (direction) {

            case UP:
                row--;
                break;

            case DOWN:
                row++;
                break;

            case LEFT:
                col--;
                break;

            case RIGHT:
                col++;
                break;
        }

        return board.getTile(row, col);
    }

    // Xử lý hợp nhất sau mỗi lượt di chuyển.
    public boolean processMerges(MergeSystem mergeSystem) {
        return mergeSystem.merge(mode, board);
    }
}
