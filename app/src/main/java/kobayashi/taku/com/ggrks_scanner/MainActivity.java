package kobayashi.taku.com.ggrks_scanner;

import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static int PERMISSION_REQUEST_CODE = 1;
    private SurfaceView mFrontPreview;
    private CameraSource mCameraSource;
    private ScanControlLayout mScanControllLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ApplicationHelper.requestPermissions(this, PERMISSION_REQUEST_CODE);
        mFrontPreview = (SurfaceView) findViewById(R.id.camera_front);
        mFrontPreview.getHolder().addCallback(surfaceHolderCallback);

        mScanControllLayout = (ScanControlLayout) findViewById(R.id.scane_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCameraSource != null) {
            startScan(mCameraSource);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(mCameraSource == null) {
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main);
            mCameraSource = setupCamera(layout.getWidth(), layout.getHeight());
            startScan(mCameraSource);
        }
    }

    private void startScan(CameraSource scanner) {
        try {
            scanner.start(mFrontPreview.getHolder());
            mScanControllLayout.setScanCamera(ApplicationHelper.getClassByField(mCameraSource, Camera.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraSource.release();
        mCameraSource = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private CameraSource setupCamera(int width, int height) {
        //TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
        CostumTextRecognizer textRecognizer = new CostumTextRecognizer();
        textRecognizer.setProcessor(
                new MultiProcessor.Builder<>(new MultiProcessor.Factory<TextBlock>() {
                    @Override
                    public Tracker<TextBlock> create(TextBlock block) {
                        return new Tracker<TextBlock>() {
                            @Override
                            public void onNewItem(int faceId, TextBlock item) {
                                Log.d(Config.TAG, "new");
                            }

                            @Override
                            public void onUpdate(TextRecognizer.Detections<TextBlock> detectionResults, TextBlock block) {
                                Log.d(Config.TAG, "update");
                                mScanControllLayout.updateScanText(detectionResults.getDetectedItems());
                            }

                            @Override
                            public void onMissing(TextRecognizer.Detections<TextBlock> detectionResults) {
                                Log.d(Config.TAG, "missing");
                            }

                            @Override
                            public void onDone() {
                                Log.d(Config.TAG, "done");
                            }
                        };
                    }
                }).build());

        CameraSource cameraSource = new CameraSource.Builder(this.getApplicationContext(), textRecognizer)
                .setRequestedPreviewSize(width, height)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .setRequestedFps(30.0f)
                .build();
        return cameraSource;
    }

    //コールバック
    private SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.d(Config.TAG, "create");
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            Log.d(Config.TAG, "change");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.d(Config.TAG, "destroy");
        }
    };
}
