package kobayashi.taku.com.ggrks_scanner;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.RelativeLayout;

import java.io.IOException;

public class ScanControlLayout extends RelativeLayout {
    private ScaleGestureDetector mScaleGestureDetector;
    private Camera mCamera;

    public ScanControlLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleGestureDetector = new ScaleGestureDetector(context, gestureListener);
    }

    public void setScanCamera(Camera camera) {
        mCamera = camera;
        mCamera.setZoomChangeListener(zoomChangeListener);
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
