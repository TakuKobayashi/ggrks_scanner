package kobayashi.taku.com.ggrks_scanner;

import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void setupCamera() {
        SurfaceView frontPreview = (SurfaceView) findViewById(R.id.camera_front);

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

        CameraSource mCameraSource = new CameraSource.Builder(this.getApplicationContext(), textRecognizer)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .setRequestedFps(30.0f)
                .build();
        Camera camera = ApplicationHelper.getClassByField(mCameraSource, Camera.class);
        try {
            Log.d(Config.TAG, "source");
            mCameraSource.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Config.TAG, "error*" + e.getMessage());
        }
/*
        SurfaceView backPreview = (SurfaceView) findViewById(R.id.camera_back);
        CameraWrapper backCamera = CameraController.getInstance(CameraController.class).open(this,1);
        backCamera.setPreviewDisplay(backPreview);
        */
    }
}
