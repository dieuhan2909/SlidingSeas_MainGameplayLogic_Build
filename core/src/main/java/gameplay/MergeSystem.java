package gameplay;

import board.Board;
import board.BoardGenerator;
import board.Tile;
import board.TileType;
import managers.InputManager;

import java.util.List;

public class MergeSystem {
    private final InputManager inputManager;
    private final MatchDetector matchDetector;

    public MergeSystem(InputManager inputManager) {
        this.inputManager = inputManager;
        this.matchDetector = new MatchDetector();
    }
    /**
     * Hợp nhất tất cả nhóm đảo hợp lệ.
     *
     * @return số lần hợp nhất đã thực hiện
     */
    public boolean merge(String mode, Board board) {
        boolean mergeAvailable = false;
        boolean firstRound = true;

        while (true) {
            List<List<Tile>> matches = matchDetector.findMatches(board);
            if (matches.isEmpty()) break;

            if(firstRound) {
                mergeAvailable = true;

                Tile selectedTile = inputManager.getSelectedTile(),
                    adjacentTile = inputManager.getAdjacentTile();

                for(List<Tile> group : matches) {
                    if(group.getFirst().getType() == selectedTile.getType()) mergeGroup(mode, group, selectedTile);
                    else mergeGroup(mode, group, adjacentTile);
                }

                firstRound = false;
            } else {
                for(List<Tile> group : matches) mergeGroup(mode, group, null);
            }

            for(int r = 0; r < board.getRows(); r++) {
                for(int c = 0; c < board.getCols(); c++) {
                    if (board.getTile(r, c).getType() == 10) {
                        for (int i = r; i > 0; i--) {
                            board.getTile(i, c).setType(board.getTile(i - 1, c).getType());
                        }
                        int newType = BoardGenerator.random.nextInt(BoardGenerator.availableTypes.length);
                        board.setTileType(0, c, BoardGenerator.availableTypes[newType]);
                    }
                }
            }
        }

        return mergeAvailable;
    }

    /**
     * Hợp nhất một nhóm đảo.
     */
    private void mergeGroup(String mode, List<Tile> group, Tile pivotTile) {
        if (group == null) return;

        int size = group.size(), i;

        if(pivotTile != null) {
            if(pivotTile.getType() == 4) {
                pivotTile.setType(mode.equals("Rescue Boat") || mode.equals("Precious Items") ? 9 : 5);
            }
            else if(pivotTile.getType() != 9) {
                pivotTile.levelUp();
            }
            else pivotTile.setType(10);

            for(Tile t : group) {
                if(t.getRow() != pivotTile.getRow() || t.getCol() != pivotTile.getCol()) t.setType(10);
                else t.setType(pivotTile.getType());
            }
        }
        else {
            if(group.getFirst().getType() == 9) group.forEach(t -> t.setType(10));
            else {

                boolean isHorizontalGroup = true;
                int row = group.getFirst().getRow();
                for (i = 1; i < size; i++)
                    if (group.get(i).getRow() != row) {
                        isHorizontalGroup = false;
                        break;
                    }

                if (isHorizontalGroup) {
                    if (size != 4) {
                        group.get(size / 2).levelUp();
                        group.forEach(t -> { if(t != group.get(size/2)) t.setType(10); });
                    } else {

                        group.get(1).levelUp();
                        group.get(0).setType(10);
                        group.get(2).setType(10);
                        group.get(3).setType(10);
                    }
                } else {

                    boolean isVerticalGroup = true;
                    int col = group.getFirst().getCol();
                    for (i = 1; i < size; i++)
                        if (group.get(i).getCol() != col) {
                            isVerticalGroup = false;
                            break;
                        }

                    if (isVerticalGroup) {
                        group.getLast().levelUp();
                        group.forEach(t -> { if(t != group.getLast()) t.setType(10); });
                    } else {

                        Tile secondTile = group.get(1);
                        int intersectionRow = 0, intersectionCol = 0;
//                        boolean outerLoopBreak;

                        if(row == secondTile.getRow()) {
                            for(i = 3; i < size; i++) {
                                if(group.get(i).getRow() != row) {
                                    intersectionCol = group.get(i).getCol();
                                    break;
                                }
                            }
                            for(Tile t : group) {
                                if(t.getCol() == intersectionCol) {
                                    t.levelUp();
                                    group.forEach(tile -> { if(tile != t) tile.setType(10);} );
                                    break;
                                }
                            }
                        } else {

                            for(i = 3; i < size; i++) {
                                if(group.get(i).getCol() != col) {
                                    intersectionRow = group.get(i).getRow();
                                    break;
                                }
                            }
                            for(Tile t : group) {
                                if(t.getRow() == intersectionRow) {
                                    t.levelUp();
                                    group.forEach(tile -> { if(tile != t) tile.setType(10); });
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
