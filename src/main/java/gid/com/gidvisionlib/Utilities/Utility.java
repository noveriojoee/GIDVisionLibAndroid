package gid.com.gidvisionlib.Utilities;

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
}
