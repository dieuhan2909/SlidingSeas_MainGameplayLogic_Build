package gameplay;

import board.Board;
import board.Tile;
import board.TileType;

import java.util.*;

public class MatchDetector {
    // 4 hướng di chuyển
    private static final int[][] DIRECTIONS = {
        {-1, 0},
        {1, 0},
        {0, -1},
        {0, 1}
    };

    /**
     * Tìm tất cả nhóm ô cùng cấp có thể hợp nhất.
     */
    public List<List<Tile>> findMatches(Board board) {
        List<List<Tile>> matchGroups = new ArrayList<>();

        int rows = board.getRows();
        int cols = board.getCols();

        boolean[][] isMatched = new boolean[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                isMatched[r][c] = false;
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (isMatched[r][c]) continue;

                int tileType = board.getTile(r, c).getType();
                //Nếu duyệt trúng tường hoặc ô đất cao tối đa thì bỏ qua
                if(tileType == 0 || tileType == 8) continue;

                List<Tile> tileGroup = new ArrayList<>();
                checkMatchHorizontally(board, isMatched, tileGroup, r, c, rows, cols);
                checkMatchVertically(board, isMatched, tileGroup, r, c, rows, cols);

                if (!tileGroup.isEmpty()) matchGroups.add(tileGroup);
            }
        }

        return matchGroups;
    }

    private void checkMatchHorizontally(Board board, boolean[][] isMatched, List<Tile> tileGroup,
                                        int r, int c, int rows, int cols) {
        int i = c;
        while (true) {
            if (i < cols - 2) {
                if (board.getTile(r, i + 1).getType() == board.getTile(r, i).getType()
                    && board.getTile(r, i + 2).getType() == board.getTile(r, i + 1).getType()
                    && !isMatched[r][i + 2]) {
                    addToTileGroup(board, isMatched, tileGroup, r, i);
                    addToTileGroup(board, isMatched, tileGroup, r, i + 1);
                    addToTileGroup(board, isMatched, tileGroup, r, i + 2);
                    checkMatchVertically(board, isMatched, tileGroup, r, i, rows, cols);
                    i++;
                } else {
                    if (tileGroup.contains(board.getTile(r, i)))
                        checkMatchVertically(board, isMatched, tileGroup, r, i, rows, cols);
                    if (tileGroup.contains(board.getTile(r, i + 1)))
                        checkMatchVertically(board, isMatched, tileGroup, r, i + 1, rows, cols);
                    break;
                }
            } else break;
        }

        i = c;
        if (i > 0 && i < cols - 1) {
            if (board.getTile(r, i + 1).getType() == board.getTile(r, i).getType()
                && board.getTile(r, i - 1).getType() == board.getTile(r, i).getType()) {
                addToTileGroup(board, isMatched, tileGroup, r, i);
                addToTileGroup(board, isMatched, tileGroup, r, i + 1);
                addToTileGroup(board, isMatched, tileGroup, r, i - 1);
                checkMatchVertically(board, isMatched, tileGroup, r, i + 1, rows, cols);
                checkMatchVertically(board, isMatched, tileGroup, r, i - 1, rows, cols);
            }
        }

        while (true) {
            if (i > 1) {
                if (board.getTile(r, i - 1).getType() == board.getTile(r, i).getType()
                    && board.getTile(r, i - 2).getType() == board.getTile(r, i - 1).getType()
                    && !isMatched[r][i - 2]) {
                    addToTileGroup(board, isMatched, tileGroup, r, i);
                    addToTileGroup(board, isMatched, tileGroup, r, i - 1);
                    addToTileGroup(board, isMatched, tileGroup, r, i - 2);
                    checkMatchVertically(board, isMatched, tileGroup, r, i, rows, cols);
                    i--;
                } else {
                    if (tileGroup.contains(board.getTile(r, i)))
                        checkMatchVertically(board, isMatched, tileGroup, r, i, rows, cols);
                    if (tileGroup.contains(board.getTile(r, i - 1)))
                        checkMatchVertically(board, isMatched, tileGroup, r, i - 1, rows, cols);
                    break;
                }
            } else break;
        }
    }

    private void checkMatchVertically(Board board, boolean[][] isMatched, List<Tile> tileGroup,
                                      int r, int c, int rows, int cols) {
        int i = r;
        while (true) {
            if (i < rows - 2) {
                if (board.getTile(i + 1, c).getType() == board.getTile(i, c).getType()
                    && board.getTile(i + 2, c).getType() == board.getTile(i + 1, c).getType()
                    && !isMatched[i + 2][c]) {
                    addToTileGroup(board, isMatched, tileGroup, i, c);
                    addToTileGroup(board, isMatched, tileGroup, i + 1, c);
                    addToTileGroup(board, isMatched, tileGroup, i + 2, c);
                    checkMatchHorizontally(board, isMatched, tileGroup, i, c, rows, cols);
                    i++;
                } else {
                    if (tileGroup.contains(board.getTile(i, c)))
                        checkMatchHorizontally(board, isMatched, tileGroup, i, c, rows, cols);
                    if (tileGroup.contains(board.getTile(i + 1, c)))
                        checkMatchHorizontally(board, isMatched, tileGroup, i + 1, c, rows, cols);
                    break;
                }
            }
        }

        i = r;
        if (i > 0 && i < rows - 1) {
            if (board.getTile(i + 1, c).getType() == board.getTile(i, c).getType()
                && board.getTile(i - 1, c).getType() == board.getTile(i, c).getType()) {
                addToTileGroup(board, isMatched, tileGroup, i, c);
                addToTileGroup(board, isMatched, tileGroup, i + 1, c);
                addToTileGroup(board, isMatched, tileGroup, i - 1, c);
                checkMatchHorizontally(board, isMatched, tileGroup, i + 1, c, rows, cols);
                checkMatchHorizontally(board, isMatched, tileGroup, i - 1, c, rows, cols);
            }
        }

        while (true) {
            if (i > 1) {
                if (board.getTile(i - 1, c).getType() == board.getTile(i, c).getType()
                    && board.getTile(i - 2, c).getType() == board.getTile(i - 1, c).getType()
                    && !isMatched[i - 2][c]) {
                    addToTileGroup(board, isMatched, tileGroup, i, c);
                    addToTileGroup(board, isMatched, tileGroup, i - 1, c);
                    addToTileGroup(board, isMatched, tileGroup, i - 2, c);
                    checkMatchHorizontally(board, isMatched, tileGroup, i, c, rows, cols);
                    i++;
                } else {
                    if (tileGroup.contains(board.getTile(i, c)))
                        checkMatchHorizontally(board, isMatched, tileGroup, i, c, rows, cols);
                    if (tileGroup.contains(board.getTile(i - 1, c)))
                        checkMatchHorizontally(board, isMatched, tileGroup, i - 1, c, rows, cols);
                    break;
                }
            }
        }
    }

    private void addToTileGroup(Board board, boolean[][] isMatched, List<Tile> tileGroup, int r, int c) {
        if (!isMatched[r][c]) {
            tileGroup.add(board.getTile(r, c));
            isMatched[r][c] = true;
        }
    }
}

//    public List<List<Cell>> findMatches(Board board) {
//
//        List<List<Cell>> matches = new ArrayList<>();
//
//        int rows = board.getRows();
//        int cols = board.getCols();
//
//        boolean[][] visited = new boolean[rows][cols];
//
//        for (int r = 0; r < rows; r++) {
//            for (int c = 0; c < cols; c++) {
//
//                if (visited[r][c]) continue;
//
//                Tile tile = board.getTile(r, c);
//
//                if (tile == null || tile.isEmpty()) {
//                    visited[r][c] = true;
//                    continue;
//                }
//
//                List<Cell> group = findConnectedGroup(
//                    board, r, c, visited
//                );
//
//                TileType type = board.getTile(r, c).getType();
//
//                if (GameRules.canMerge(group.size())
//                    && GameRules.canUpgrade(type)) {
//                    matches.add(group);
//                }
//            }
//        }
//
//        return matches;
//    }
//
//    /**
//     * BFS tìm toàn bộ các ô cùng cấp liên kết với ô bắt đầu.
//     */
//    private List<Cell> findConnectedGroup(
//        Board board,
//        int startRow,
//        int startCol,
//        boolean[][] visited) {
//
//        List<Cell> group = new ArrayList<>();
//        Queue<Cell> queue = new LinkedList<>();
//
//        TileType targetType =
//            board.getTile(startRow, startCol).getType();
//
//        queue.offer(new Cell(startRow, startCol));
//        visited[startRow][startCol] = true;
//
//        while (!queue.isEmpty()) {
//
//            Cell current = queue.poll();
//
//            group.add(current);
//
//            for (int[] direction : DIRECTIONS) {
//
//                int newRow = current.row() + direction[0];
//                int newCol = current.col() + direction[1];
//
//                if (!isInsideBoard(
//                    board, newRow, newCol
//                )) continue;
//
//                if (visited[newRow][newCol]) continue;
//
//                Tile neighbor = board.getTile(newRow, newCol);
//
//                if (neighbor == null
//                    || neighbor.isEmpty()
//                    || neighbor.getType() != targetType) {
//                    continue;
//                }
//
//                visited[newRow][newCol] = true;
//
//                queue.offer(new Cell(newRow, newCol));
//            }
//        }
//
//        return group;
//    }
//
//    private boolean isInsideBoard(
//        Board board, int row, int col) {
//
//        return row >= 0
//            && row < board.getRows()
//            && col >= 0
//            && col < board.getCols();
//    }
//}
