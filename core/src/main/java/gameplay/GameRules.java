package gameplay;

import board.Tile;
import board.TileType;

public class GameRules {
//    // Số lượng đảo tối thiểu để hợp nhất
//    public static final int MIN_MERGE_COUNT = 3;

    private GameRules() {
        // Utility class
    }

    public static boolean canMerge(Tile t1, Tile t2, Tile t3) {
        return t1.getType() == t2.getType() && t2.getType() == t3.getType();
    }

//    public static boolean canUpgrade(TileType type) {
//        return type != null && type != TileType.EMPTY
//            && type != MAX_LEVEL;
//    }
}
