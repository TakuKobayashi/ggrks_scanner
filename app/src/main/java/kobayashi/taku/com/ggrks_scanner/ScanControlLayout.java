package kobayashi.taku.com.ggrks_scanner;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.vision.text.TextBlock;

public class ScanControlLayout extends RelativeLayout {
    private ScaleGestureDetector mScaleGestureDetector;
    private Camera mCamera;
    private SparseArray<DrawRectView> mScanTextList = new SparseArray<DrawRectView>();

    public ScanControlLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleGestureDetector = new ScaleGestureDetector(context, gestureListener);
    }

    public void setScanCamera(Camera camera) {
        Log.d(Config.TAG, "" + camera);
        camera.setZoomChangeListener(zoomChangeListener);
        mCamera = camera;
    }

    private Camera.OnZoomChangeListener zoomChangeListener = new Camera.OnZoomChangeListener() {
        @Override
        public void onZoomChange(int zoomValue, boolean stopped, Camera camera) {
            Log.d(Config.TAG, "zoom : "+ zoomValue + " stopped : " + stopped);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mScaleGestureDetector != null) {
            mScaleGestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    public void updateScanText(SparseArray<TextBlock> scanTextList) {
        for(int i = 0;i < scanTextList.size();++i) {
            int id = scanTextList.keyAt(i);
            TextBlock scan = scanTextList.valueAt(i);
            Rect bound = scan.getBoundingBox();

            DrawRectView scanTextRectView = getScanTextView(id);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
            params.leftMargin = bound.left; //Your X coordinate
            params.topMargin = bound.top; //Your Y coordinate
            params.width = bound.width();
            params.height = bound.height();
            scanTextRectView.setLayoutParams(params);
            scanTextRectView.invalidate();
        }
    }

    private DrawRectView getScanTextView(int id) {
        DrawRectView scanTextRectView = mScanTextList.get(id, null);
        if(scanTextRectView == null) {
            scanTextRectView = new DrawRectView(this.getContext());
            scanTextRectView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    View view = inflater.inflate(R.layout.scan_text_button_list_view, null);

                }
            });
        }
        return scanTextRectView;
    }


    private void handleZoom(MotionEvent event, Camera.Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
//        float newDist = getFingerSpacing(event);
//      if (newDist > mDist) {
            //zoom in
//            if (zoom < maxZoom)
//                zoom++;
//        } else if (newDist < mDist) {
            //zoom out
//            if (zoom > 0)
//               zoom--;
//        }
//        mDist = newDist;
        params.setZoom(zoom);
        mCamera.setParameters(params);
    }


    private ScaleGestureDetector.SimpleOnScaleGestureListener gestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.d(Config.TAG, "onScaleBegin : "+ detector.getScaleFactor());
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            Log.d(Config.TAG, "onScaleEnd : "+ detector.getScaleFactor());
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.d(Config.TAG, "onScale : "+ detector.getScaleFactor());
            return true;
        };
    };
}
