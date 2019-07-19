package com.wastedsummer.catml;

import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

public class TrackingHelper {

    public static int getFirebaseRotation(int rotation) {
        int result;
        switch (rotation) {
            case 0:
                result = FirebaseVisionImageMetadata.ROTATION_0;
                break;
            case 90:
                result = FirebaseVisionImageMetadata.ROTATION_90;
                break;
            case 180:
                result = FirebaseVisionImageMetadata.ROTATION_180;
                break;
            case 270:
                result = FirebaseVisionImageMetadata.ROTATION_270;
                break;
            default:
                result = FirebaseVisionImageMetadata.ROTATION_0;
        }
        return result;
    }

}
