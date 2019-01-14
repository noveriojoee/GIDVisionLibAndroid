package gid.com.gidvisionlib.Utilities;

import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import gid.com.gidvisionlib.Interfaces.IOCRListener;
import gid.com.gidvisionlib.Utilities.Camera.GraphicOverlay;
import gid.com.gidvisionlib.Utilities.Graphic.OcrGraphic;

public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<OcrGraphic> graphicOverlay;
    private IOCRListener activityListener;


    public OcrDetectorProcessor(IOCRListener activityListener) {
        this.activityListener = activityListener;
    }

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        this.activityListener.onReceiveText(detections.getDetectedItems());

    }
}