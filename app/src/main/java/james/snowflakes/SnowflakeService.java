package james.snowflakes;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

//Snowflake Service. Snowflakeservice. Snowflakervice.
public class SnowflakeService extends AccessibilityService {

    private WindowManager windowManager;
    private SnowflakeView snowflakeView;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        snowflakeView = new SnowflakeView(this);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
        params.gravity = Gravity.TOP;

        try {
            windowManager.addView(snowflakeView, params);
        } catch (WindowManager.BadTokenException | SecurityException e) {
            e.printStackTrace();
            snowflakeView = null;
            stopSelf();
            return;
        }

        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (snowflakeView != null) snowflakeView.snow(2000);
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        if (snowflakeView != null) windowManager.removeView(snowflakeView);
        super.onDestroy();
    }
}
