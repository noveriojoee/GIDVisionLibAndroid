package gid.com.gidvisionlib.ViewModels;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import gid.com.gidvisionlib.Utilities.Utility;

public class OcrDetectorViewModel {

    private String textCaptured;
    private String textResult;
    private byte[] imageCaptured;
    private String imageCapturedPath;
    private String ocrMode;

    public String getTextResult() {
        return textResult;
    }

    public String getImageCapturedPath() {
        return imageCapturedPath;
    }

    public String getOcrMode() {
        return ocrMode;
    }

    public void setOcrMode(String ocrMode) {
        this.ocrMode = ocrMode;
    }

    public byte[] getImageCaptured() {
        return imageCaptured;
    }

    public void setImageCaptured(byte[] imageCaptured) throws Exception {
        String filename = "ocrCaptured.png";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageCaptured, 0, imageCaptured.length);
        ByteArrayOutputStream compressedImage = new ByteArrayOutputStream();
        this.imageCaptured = compressedImage.toByteArray();
        FileOutputStream out = new FileOutputStream(dest);
        this.imageCapturedPath = dest.getAbsolutePath();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.flush();
        out.close();
    }

    public String getTextCaptured() {
        return textCaptured;
    }

    public String getBase64Image() throws Exception {
        String result = "";
        if (this.imageCaptured != null && this.imageCaptured.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(this.imageCaptured, 0, this.imageCaptured.length);
            ByteArrayOutputStream compressedImage = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, compressedImage);
            result = Base64.encodeToString(compressedImage.toByteArray(), 0);
        }
        return result;
    }

    public byte[] getComprressedImageByte() {
        byte[] result = new byte[10];
        if (this.imageCaptured != null && this.imageCaptured.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(this.imageCaptured, 0, this.imageCaptured.length);
            ByteArrayOutputStream compressedImage = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, compressedImage);
            return compressedImage.toByteArray();
        }
        return result;
    }

    public void setTextCaptured(String textCaptured) {
        this.textCaptured = textCaptured;
    }

    public OcrDetectorViewModel() {
        super();
        this.textCaptured = "";
    }

    public OcrDetectorViewModel(String ocrMode) {
        super();
        this.ocrMode = ocrMode;

    }

    public String extractData() {
        this.textResult = "NOT_FOUND";

        if (this.textCaptured != null && this.textCaptured.length() > 0) {
            if (this.ocrMode.equals("DEBIT_CARD")) {
                this.textResult  = Utility.extractDebitCardData(this.textCaptured);
            } else if (this.ocrMode.equals("KTP")) {
                this.textResult  = Utility.extractKtpData(this.textCaptured);
            } else if (this.ocrMode.equals("NPWP")) {
                this.textResult  = Utility.extractNpwpData(this.textCaptured);
            }
        }

        return this.textResult ;
    }
}
