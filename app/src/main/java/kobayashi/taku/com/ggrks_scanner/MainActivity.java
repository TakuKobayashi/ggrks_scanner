package kobayashi.taku.com.ggrks_scanner;

import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationHelper.requestPermissions(this, PERMISSION_REQUEST_CODE);
        mCameraSource = setupCamera();
        mFrontPreview = (SurfaceView) findViewById(R.id.camera_front);
        mFrontPreview.getHolder().addCallback(surfaceHolderCallback);

        Camera camera = ApplicationHelper.getClassByField(mCameraSource, Camera.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mCameraSource.start(mFrontPreview.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraSource.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraSource.release();;
        mCameraSource = null;
    }

    private CameraSource setupCamera() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
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
                            public void onUpdate(FaceDetector.Detections<TextBlock> detectionResults, TextBlock block) {
                                Log.d(Config.TAG, "update");
                            }

                            @Override
                            public void onMissing(FaceDetector.Detections<TextBlock> detectionResults) {
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
                .setRequestedPreviewSize(640, 480)
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
