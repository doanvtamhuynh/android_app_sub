package com.example.autoswipe;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class AutoSwipeService extends AccessibilityService {

    private Handler handler;
    private Runnable swipeRunnable;
    private static final long INTERVAL = 5000;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        handler = new Handler(Looper.getMainLooper());
        swipeRunnable = new Runnable() {
            @Override
            public void run() {
                int direction = (int) (Math.random() * 2); // 0 hoặc 1
                if (direction == 1) {
                    performSwipe(500, 1200, 500, 800); // Vuốt lên
                    Log.d("AutoSwipe", "Swipe lên");
                } else {
                    performSwipe(500, 800, 500, 1200); // Vuốt xuống
                    Log.d("AutoSwipe", "Swipe xuống");
                }
                handler.postDelayed(this, INTERVAL);
            }
        };
        handler.post(swipeRunnable);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Không cần xử lý
    }

    @Override
    public void onInterrupt() {
        if (handler != null) {
            handler.removeCallbacks(swipeRunnable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(swipeRunnable);
        }
    }

    private void performSwipe(float startX, float startY, float endX, float endY) {
        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);

        GestureDescription.StrokeDescription stroke = new GestureDescription.StrokeDescription(path, 0, 500);
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(stroke);

        dispatchGesture(gestureBuilder.build(), null, null);
    }
}
