package board;

public class TileType {
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
    private int type;

    TileType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
    public void setType(int level) { this.type = level; }
    public void levelUp() { this.type++; }
    public void toIceberg() { this.type = 9; }
}
