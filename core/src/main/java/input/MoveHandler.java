package input;

import board.BoardManager;
import board.Tile;

import gameplay.MergeSystem;
import managers.AnimationManager;

public class MoveHandler {

    private enum MoveState {
        IDLE,
        SWAPPING,
        REVERSING,
        MERGING
    }

//    private final BoardManager boardManager;
    private final AnimationManager animationManager;

    private MoveState state = MoveState.IDLE;

    private Tile selectedTile;
    private Tile adjacentTile;

    public MoveHandler(
//        BoardManager boardManager,
        AnimationManager animationManager
    ) {

//        this.boardManager = boardManager;
        this.animationManager = animationManager;
    }

    // Nhận yêu cầu swipe.
    public void handleMove(
        BoardManager boardManager,
        Tile selectedTile,
        SwipeDirection direction
    ) {

        // Không nhận input khi đang animation.
        if (state != MoveState.IDLE) return;

        if (selectedTile == null || direction == null) return;

        Tile adjacent = boardManager.getAdjacentTile(
            selectedTile,
            direction
        );

        if (adjacent == null || adjacent.isEmpty()) return;

        this.selectedTile = selectedTile;
        this.adjacentTile = adjacent;

        // Swap logic Board.
        boardManager.getBoard().swapTiles(
            selectedTile,
            adjacent
        );

        // Bắt đầu swap animation.
        animationManager.startSwapAnimation(
            selectedTile,
            adjacent,
            boardManager.getTileSize()
        );

        state = MoveState.SWAPPING;
    }

    // Cập nhật trạng thái mỗi frame.
    public void update(BoardManager boardManager, MergeSystem mergeSystem, float delta) {

        animationManager.update(delta);

        if (!animationManager.isAnimationFinished()) {
            return;
        }

        switch (state) {

            case IDLE:
                break;

            case SWAPPING:

                // Chỉ kiểm tra merge sau khi swap animation kết thúc.
                boolean mergeAvailable =
                    boardManager.processMerges(mergeSystem);

                if (mergeAvailable) {

                    // Giữ nguyên vị trí mới.
                    animationManager.playMergeAnimation(
                        boardManager.getBoard()
                    );

                    state = MoveState.MERGING;

                } else {

                    // Bắt đầu animation quay trở lại.
                    animationManager.startReverseSwapAnimation();

                    state = MoveState.REVERSING;
                }

                break;

            case REVERSING:

                // Hình ảnh đã trở về vị trí ban đầu.
                // Bây giờ hoàn tác swap logic.
                boardManager.getBoard().swapTiles(
                    selectedTile,
                    adjacentTile
                );

                resetMove();

                break;

            case MERGING:

                // Merge animation đã hoàn tất.
                resetMove();

                break;
        }
    }

    private void resetMove() {

        selectedTile = null;
        adjacentTile = null;

        state = MoveState.IDLE;
    }

    public boolean isMoving() {
        return state != MoveState.IDLE;
    }
}
