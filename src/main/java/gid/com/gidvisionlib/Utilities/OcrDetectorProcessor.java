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


    public OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay, IOCRListener activityListener) {
        graphicOverlay = ocrGraphicOverlay;
        this.activityListener = activityListener;
    }



    @Override
    public void release() {
        graphicOverlay.clear();
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        graphicOverlay.clear();
//        SparseArray<TextBlock> items = detections.getDetectedItems();
//        for (int i = 0; i < items.size(); ++i) {
//            this.activityListener.onReceiveText(items.valueAt(i));
//
//        }
        this.activityListener.onReceiveText(detections.getDetectedItems());

    }


}