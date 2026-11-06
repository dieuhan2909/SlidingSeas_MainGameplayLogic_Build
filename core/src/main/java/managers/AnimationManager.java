package managers;

import board.Board;
import board.Tile;

public class AnimationManager {

    public enum AnimationType {
        NONE,
        SWAP,
        REVERSE,
        MERGE
    }

    private final float[][] scales;

    private final float duration = 0.3f;

    private float elapsed;
    private float progress;

    private AnimationType currentAnimation = AnimationType.NONE;

    private Tile firstTile;
    private Tile secondTile;

    private float firstOffsetX;
    private float firstOffsetY;

    private float secondOffsetX;
    private float secondOffsetY;

    public AnimationManager(int rows, int cols) {

        scales = new float[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                scales[r][c] = 1f;
            }
        }
    }

    // Bắt đầu animation swap.
    // Lưu ý: Board đã swap dữ liệu trước khi gọi phương thức này.
    public void startSwapAnimation(
        Tile first,
        Tile second,
        float tileSize
    ) {

        firstTile = first;
        secondTile = second;

        // Tính offset từ vị trí ban đầu đến vị trí hiện tại.
        firstOffsetX = (second.getCol() - first.getCol()) * tileSize;
        firstOffsetY = (first.getRow() - second.getRow()) * tileSize;

        secondOffsetX = -firstOffsetX;
        secondOffsetY = -firstOffsetY;

        startAnimation(AnimationType.SWAP);
    }

    // Bắt đầu animation quay về vị trí ban đầu.
    public void startReverseSwapAnimation() {

        startAnimation(AnimationType.REVERSE);
    }

    private void startAnimation(AnimationType type) {

        currentAnimation = type;

        elapsed = 0f;
        progress = 0f;
    }

    // Animation merge.
    public void playMergeAnimation(Board board) {

        firstTile = null;
        secondTile = null;

        for (int r = 0; r < scales.length; r++) {
            for (int c = 0; c < scales[r].length; c++) {
                scales[r][c] = 1f;
            }
        }

        startAnimation(AnimationType.MERGE);
    }

    // Cập nhật animation mỗi frame.
    public void update(float delta) {

        if (currentAnimation == AnimationType.NONE) {
            return;
        }

        elapsed += delta;

        progress = Math.min(1f, elapsed / duration);

        if (currentAnimation == AnimationType.MERGE) {

            float scale = 1f
                + 0.3f * (float) Math.sin(Math.PI * progress);

            for (int r = 0; r < scales.length; r++) {
                for (int c = 0; c < scales[r].length; c++) {
                    scales[r][c] = scale;
                }
            }
        }

        if (progress >= 1f) {

            progress = 1f;

            for (int r = 0; r < scales.length; r++) {
                for (int c = 0; c < scales[r].length; c++) {
                    scales[r][c] = 1f;
                }
            }

            currentAnimation = AnimationType.NONE;
        }
    }

    // Offset X của Tile.
    public float getOffsetX(Tile tile) {

        if (tile == firstTile) {

            if (currentAnimation == AnimationType.SWAP) {
                return firstOffsetX * (1f - progress);
            }

            if (currentAnimation == AnimationType.REVERSE) {
                return firstOffsetX * progress;
            }
        }

        if (tile == secondTile) {

            if (currentAnimation == AnimationType.SWAP) {
                return secondOffsetX * (1f - progress);
            }

            if (currentAnimation == AnimationType.REVERSE) {
                return secondOffsetX * progress;
            }
        }

        return 0f;
    }

    // Offset Y của Tile.
    public float getOffsetY(Tile tile) {

        if (tile == firstTile) {

            if (currentAnimation == AnimationType.SWAP) {
                return firstOffsetY * (1f - progress);
            }

            if (currentAnimation == AnimationType.REVERSE) {
                return firstOffsetY * progress;
            }
        }

        if (tile == secondTile) {

            if (currentAnimation == AnimationType.SWAP) {
                return secondOffsetY * (1f - progress);
            }

            if (currentAnimation == AnimationType.REVERSE) {
                return secondOffsetY * progress;
            }
        }

        return 0f;
    }

    public float getScale(int row, int col) {
        return scales[row][col];
    }

    public boolean isAnimationFinished() {
        return currentAnimation == AnimationType.NONE;
    }

    public AnimationType getCurrentAnimation() {
        return currentAnimation;
    }
}
