package board;

public class Tile {
    private int row;
    private int col;

    // type = 0: empty tile/ wall
    // type = 1: sea 1
    // type = 2: sea 2
    // type = 3: sea 3
    // type = 4: sea 4
    // type = 5: land 1
    // type = 6: land 2
    // type = 7: land 3
    // type = 8: land 4
    // type = 9: iceberg
    // type = 10: pending tile
    // type = 11: coral 1
    // type = 12: coral 2
    // type = 13: coral 3
    private int type;

    public Tile(int row, int col, int type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() { return col; }

    public int getType() { return type; }

    public void setType(int type) {
        this.type = type;
    }

    public void levelUp() { this.type++; }

    public void toIceberg() { this.type = 9; }

    public boolean isEmpty() {
        return type == 0;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
