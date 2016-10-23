package kobayashi.taku.com.ggrks_scanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;

public class DrawRectView extends View {
    public DrawRectView(Context context) {
        super(context);
        Log.d(Config.TAG, "isNew");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255,0,0));
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        Rect rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());
        Log.d(Config.TAG, "width:" + canvas.getWidth() + "height:" + canvas.getHeight());
        canvas.drawRect(rect, paint);
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.invalidate();
    }
}
