package gid.com.gidvisionlib.ViewModels;


import gid.com.gidvisionlib.Utilities.Utility;

public class OcrDetectorViewModel {

    private String textCaptured;
    private byte[] imageCaptured;
    private String ocrMode;

    public String getOcrMode() {
        return ocrMode;
    }

    public void setOcrMode(String ocrMode) {
        this.ocrMode = ocrMode;
    }

    public byte[] getImageCaptured() {
        return imageCaptured;
    }

    public void setImageCaptured(byte[] imageCaptured) {
        this.imageCaptured = imageCaptured;
    }

    public String getTextCaptured() {
        return textCaptured;
    }

    public void setTextCaptured(String textCaptured) {
        this.textCaptured = textCaptured;
    }

    public OcrDetectorViewModel(){
        super();
        this.textCaptured = "";
    }

    public OcrDetectorViewModel(String ocrMode) {
        super();
        this.ocrMode = ocrMode;

    }

    public String extractData(){
        String result = "NOT_FOUND";

        if (this.textCaptured != null && this.textCaptured.length() > 0){
            if(this.ocrMode.equals("DEBIT_CARD")){
                result = Utility.extractDebitCardData(this.textCaptured);
            }else if(this.ocrMode.equals("KTP")){
                result = Utility.extractKtpData(this.textCaptured);
            }else if(this.ocrMode.equals("NPWP")){
                result = Utility.extractNpwpData(this.textCaptured);
            }
        }

        return result;
    }
}
