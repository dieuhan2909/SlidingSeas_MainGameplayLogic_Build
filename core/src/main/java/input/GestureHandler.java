package input;

public class GestureHandler {

    private float startX;
    private float startY;

    public void touchDown(float x, float y) {
        startX = x;
        startY = y;
    }

    public SwipeDirection touchUp(float x, float y) {
        float deltaX = x - startX;
        float deltaY = y - startY;

        // Không tính là swipe nếu khoảng cách quá ngắn
        final float MIN_SWIPE_DISTANCE = 50f;
        if (Math.abs(deltaX) < MIN_SWIPE_DISTANCE && Math.abs(deltaY) < MIN_SWIPE_DISTANCE) return null;

        // Xác định hướng dựa trên trục có độ dịch chuyển lớn hơn
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            return deltaX > 0 ? SwipeDirection.RIGHT : SwipeDirection.LEFT;
        }

        return deltaY > 0 ? SwipeDirection.UP : SwipeDirection.DOWN;
    }
}
