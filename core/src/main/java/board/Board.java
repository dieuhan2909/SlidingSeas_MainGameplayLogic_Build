package board;

public class Board {

    private final int rows;
    private final int cols;

    private final Tile[][] tiles;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        tiles = new Tile[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                tiles[row][col] =
                    new Tile(row, col, 0);

            }
        }
    }

    public int getRows() { return rows; }

    public int getCols() { return cols; }

    public Tile getTile(int row, int col) {
        if (!isValidPosition(row, col)) {
            return null;
        }

        return tiles[row][col];
    }

    public void setTileType(int row, int col, int type) {
        if (!isValidPosition(row, col)) {
            return;
        }

        tiles[row][col].setType(type);
    }

//    public TileType getTileType(int row, int col) {
//        return tiles[row][col].getType();
//    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public void swapTiles(Tile tile1, Tile tile2) {

        if (tile1 == null || tile2 == null) {
            return;
        }

        int row1 = tile1.getRow();
        int col1 = tile1.getCol();

        int row2 = tile2.getRow();
        int col2 = tile2.getCol();

        if (!isValidPosition(row1, col1) ||
            !isValidPosition(row2, col2)) {
            return;
        }

        tiles[row1][col1] = tile2;
        tiles[row2][col2] = tile1;

        tile1.setPosition(row2, col2);
        tile2.setPosition(row1, col1);
    }
}
