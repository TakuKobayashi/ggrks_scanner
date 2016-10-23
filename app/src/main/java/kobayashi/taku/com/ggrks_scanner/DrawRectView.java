package kobayashi.taku.com.ggrks_scanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ScaleGestureDetector;
import android.view.View;

public class DrawRectView extends View {
    public DrawRectView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(255,0,0));
        paint.setStrokeWidth(10);
        Rect rect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());
        canvas.drawRect(rect, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.invalidate();
    }
}
