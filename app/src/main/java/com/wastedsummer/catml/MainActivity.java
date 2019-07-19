package com.wastedsummer.catml;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;

public class MainActivity extends AppCompatActivity implements GestureListener {

    FirebaseVisionImageLabeler mLabeler;
    private CameraView mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mCamera = findViewById(R.id.camera);
        mCamera.setLifecycleOwner(this);

        GestureDetector gestureDetector = new GestureDetector(this, this);
        mCamera.setOnTouchListener((v, event) -> !gestureDetector.onTouchEvent(event));
        mCamera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                Bitmap bitmap = BitmapFactory.decodeByteArray(result.getData(), 0, result.getData().length);
                ((ImageView) findViewById(R.id.image)).setImageBitmap(bitmap);
                analyze(bitmap);
            }
        });

        FirebaseVisionOnDeviceImageLabelerOptions options = new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                .build();
        mLabeler = FirebaseVision.getInstance().getOnDeviceImageLabeler(options);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mCamera.takePictureSnapshot();
        return true;
    }

    private void analyze(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        mLabeler.processImage(image)
                .addOnSuccessListener(firebaseVisionImageLabels -> {
                    for (FirebaseVisionImageLabel label : firebaseVisionImageLabels) {
                        String text = label.getText();
                        String entityId = label.getEntityId();
                        float confidence = label.getConfidence();
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.e("KING", e.getMessage()));
    }

}
