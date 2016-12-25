package james.snowflakes;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.Random;

public class SnowflakeView extends View {

    private static final int SNOWFLAKE_SIZE = 10;
    private static final int SNOWFLAKE_RATE = 50;

    private Paint paint;
    private Bitmap[] bitmaps;
    private Random random;
    private int iteration;
    private int length;

    public SnowflakeView(Context context) {
        super(context);
        init();
    }

    public SnowflakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SnowflakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public SnowflakeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        bitmaps = new Bitmap[]{
                Bitmap.createBitmap(SNOWFLAKE_SIZE, SNOWFLAKE_SIZE, Bitmap.Config.ARGB_4444),
                Bitmap.createBitmap(SNOWFLAKE_SIZE, SNOWFLAKE_SIZE, Bitmap.Config.ARGB_4444),
                Bitmap.createBitmap(SNOWFLAKE_SIZE, SNOWFLAKE_SIZE, Bitmap.Config.ARGB_4444),
                Bitmap.createBitmap(SNOWFLAKE_SIZE, SNOWFLAKE_SIZE, Bitmap.Config.ARGB_4444),
                Bitmap.createBitmap(SNOWFLAKE_SIZE, SNOWFLAKE_SIZE, Bitmap.Config.ARGB_4444),
                Bitmap.createBitmap(SNOWFLAKE_SIZE, SNOWFLAKE_SIZE, Bitmap.Config.ARGB_4444),
                Bitmap.createBitmap(SNOWFLAKE_SIZE, SNOWFLAKE_SIZE, Bitmap.Config.ARGB_4444),
                Bitmap.createBitmap(SNOWFLAKE_SIZE, SNOWFLAKE_SIZE, Bitmap.Config.ARGB_4444),
                Bitmap.createBitmap(SNOWFLAKE_SIZE, SNOWFLAKE_SIZE, Bitmap.Config.ARGB_4444),
                Bitmap.createBitmap(SNOWFLAKE_SIZE, SNOWFLAKE_SIZE, Bitmap.Config.ARGB_4444)
        };

        for (int i = 0; i < bitmaps.length; i++) {
            bitmaps[i].eraseColor(Color.argb((int) (255 * ((double) (bitmaps.length - i) / bitmaps.length)), 255, 255, 255));
        }

        random = new Random();
    }

    public void snow(int length) {
        iteration = length / SNOWFLAKE_RATE;
        this.length = iteration;

        ValueAnimator animator = ValueAnimator.ofInt(length / SNOWFLAKE_RATE, 0);
        animator.setDuration(length);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iteration = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    public void draw(Canvas canvas) {
        if (iteration > 0 && length > 0 && iteration <= length) {
            Bitmap bitmap = bitmaps[(int) (((double) (length - iteration) / length) * bitmaps.length)];
            for (int x = 0; x < canvas.getWidth(); x += SNOWFLAKE_SIZE) {
                if (getBoolean((double) (length - iteration) / length)) {
                    int y = (length - iteration) * SNOWFLAKE_SIZE;

                    if (getBoolean(0.5)) y += random.nextInt(length / 3) * SNOWFLAKE_SIZE;
                    else y -= random.nextInt(length / 3) * SNOWFLAKE_SIZE;

                    canvas.drawBitmap(bitmap, x, y, paint);
                }
            }
        }
    }

    private boolean getBoolean(double probability) {
        return random.nextDouble() > probability;
    }
}
