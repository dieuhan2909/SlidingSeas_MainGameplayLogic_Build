package data;

public class LevelData {

    private final int levelNumber;
    private final int rows;
    private final int cols;

    public LevelData(int levelNumber, int rows, int cols) {
        this.levelNumber = levelNumber;
        this.rows = rows;
        this.cols = cols;
    }

    public int getLevelNumber() {
        return levelNumber;
    }
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
}
