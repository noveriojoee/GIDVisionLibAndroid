package gid.com.gidvisionlib.Utilities;

import android.graphics.Bitmap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static String extractNpwpData(String text){
        String result = "NOT_FOUND";
        String REGEX_NPWP_PATTERN = "[0-9]{2}[.][0-9]{3}[.][0-9]{3}[.][0-9][-][0-9]{3}[.][0-9]{3}";
        Pattern pattern = Pattern.compile(REGEX_NPWP_PATTERN);
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()){
            result = matcher.group();
        }

        return result;
    }


    public static String extractKtpData(String text){
        String result = "NOT_FOUND";
        String REGEX_KTP_NIK_PATTERN = "[0-9]{16}";
        Pattern pattern = Pattern.compile(REGEX_KTP_NIK_PATTERN);
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()){
            result = matcher.group();
        }

        return result;
    }


    public static String extractDebitCardData(String text){
        String result = "NOT_FOUND";
        String REGEX_DEBITCARD_PATTERN = "[0-9]{4}[ ][0-9]{4}[ ][0-9]{4}[ ][0-9]{4}";
        Pattern pattern = Pattern.compile(REGEX_DEBITCARD_PATTERN);
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()){
            result = matcher.group();
        }

        return result;
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
