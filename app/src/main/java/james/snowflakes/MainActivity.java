package james.snowflakes;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ACCESSIBILITY = 1425;
    private static final int REQUEST_OVERLAY = 5422;

    private View accessibilityView, overlayView, doneView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accessibilityView = findViewById(R.id.accessibility);
        overlayView = findViewById(R.id.overlay);
        doneView = findViewById(R.id.done);

        accessibilityView.setVisibility(isAccessibility() ? View.GONE : View.VISIBLE);
        accessibilityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), REQUEST_ACCESSIBILITY);
            }
        });

        overlayView.setVisibility(isOverlay() ? View.GONE : View.VISIBLE);
        overlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), REQUEST_OVERLAY);
            }
        });

        doneView.setVisibility(isAccessibility() && isOverlay() ? View.VISIBLE : View.GONE);
    }

    private boolean isAccessibility() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SnowflakeService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return Settings.canDrawOverlays(this);
        else return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        accessibilityView.setVisibility(isAccessibility() ? View.GONE : View.VISIBLE);
        overlayView.setVisibility(isOverlay() ? View.GONE : View.VISIBLE);
        doneView.setVisibility(isAccessibility() && isOverlay() ? View.VISIBLE : View.GONE);
    }
}
