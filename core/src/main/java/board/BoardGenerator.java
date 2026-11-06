package board;

import gameplay.GameRules;

import java.util.Random;

public class BoardGenerator {
    public static final Random random = new Random();

    public static final int[] availableTypes = {
        1, 2, 3, 4, //5
    };

    public Board generate(int rows, int cols) {
        Board board = new Board(rows, cols);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int index = random.nextInt(availableTypes.length);
                if(r < 2 && c > 1) {
                    while(board.getTile(r, c - 2).getType() == availableTypes[index]
                        && board.getTile(r, c - 1).getType() == availableTypes[index])
                        index = random.nextInt(availableTypes.length);
                }
                else if(c < 2 && r > 1) {
                    while(board.getTile(r - 2, c).getType() == availableTypes[index]
                        && board.getTile(r - 1, c).getType() == availableTypes[index])
                        index = random.nextInt(availableTypes.length);
                }
                else if(r > 1) {
                    while(board.getTile(r, c - 2).getType() == availableTypes[index]
                        && board.getTile(r, c - 1).getType() == availableTypes[index]
                        || board.getTile(r - 2, c).getType() == availableTypes[index]
                        && board.getTile(r - 1, c).getType() == availableTypes[index])
                        index = random.nextInt(availableTypes.length);
                }
                board.setTileType(r, c, availableTypes[index]);
            }
        }

        boolean isValidBoard = false, outerLoopBreak = false;
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                if(c < cols - 2) {
                    if(r < rows - 1) {
                        if(GameRules.canMerge(board.getTile(r, c), board.getTile(r, c + 1), board.getTile(r + 1, c + 2))
                            || GameRules.canMerge(board.getTile(r, c), board.getTile(r + 1, c + 1), board.getTile(r, c + 2))
                            || GameRules.canMerge(board.getTile(r, c), board.getTile(r + 1, c + 1), board.getTile(r + 1, c + 2))) {
                            isValidBoard = true;
                            outerLoopBreak = true;
                            break;
                        }
                    }
                    if(r > 0) {
                        if(GameRules.canMerge(board.getTile(r, c), board.getTile(r, c + 1), board.getTile(r - 1, c + 2))
                            || GameRules.canMerge(board.getTile(r, c), board.getTile(r - 1, c + 1), board.getTile(r, c + 2))
                            || GameRules.canMerge(board.getTile(r, c), board.getTile(r - 1, c + 1), board.getTile(r - 1, c + 2))) {
                            isValidBoard = true;
                            outerLoopBreak = true;
                            break;
                        }
                    }
                }
                if(r < rows - 2) {
                    if(c < cols - 1) {
                        if(GameRules.canMerge(board.getTile(r, c), board.getTile(r + 1, c), board.getTile(r + 2, c + 1))
                            || GameRules.canMerge(board.getTile(r, c), board.getTile(r + 1, c + 1), board.getTile(r + 2, c))
                            || GameRules.canMerge(board.getTile(r, c), board.getTile(r + 1, c + 1), board.getTile(r + 2, c + 1))) {
                            isValidBoard = true;
                            outerLoopBreak = true;
                            break;
                        }
                    }
                    if(c > 0) {
                        if(GameRules.canMerge(board.getTile(r, c), board.getTile(r + 1, c), board.getTile(r + 2, c - 1))
                            || GameRules.canMerge(board.getTile(r, c), board.getTile(r + 1, c - 1), board.getTile(r + 2, c))
                            || GameRules.canMerge(board.getTile(r, c), board.getTile(r + 1, c - 1), board.getTile(r + 2, c - 1))) {
                            isValidBoard = true;
                            outerLoopBreak = true;
                            break;
                        }
                    }
                }
            }
            if(outerLoopBreak) break;
        }

        if(isValidBoard) return board;
        else return generate(rows, cols);
    }
}
