package redhat.org.ipark.extras;

import android.text.TextUtils;
import android.util.Log;
import java.util.Calendar;

public class CreditCardUtils {
    public static final int NONE = 0;
//    public static final int VISA = 1;
//    public static final int MASTERCARD = 2;
//    public static final int DISCOVER = 3;
    public static final int AMEX = 4;

//    public static final String VISA_PREFIX = "4";
//    public static final String MASTERCARD_PREFIX = "51,52,53,54,55,";
//    public static final String DISCOVER_PREFIX = "6011";
    public static final String AMEX_PREFIX = "34,37,";

    public static String CARD_PATTERN = "#### ###### #####";
    public static String EXPDATE_PATTERN = "##/##";

    public static int getCardType(String cardNumber) {
        if (AMEX_PREFIX.contains(cardNumber.substring(0, 2) + ","))
            return AMEX;
//        if (cardNumber.substring(0, 1).equals(VISA_PREFIX))
//            return VISA;
//        else if (MASTERCARD_PREFIX.contains(cardNumber.substring(0, 2) + ","))
//            return MASTERCARD;
//        else if (cardNumber.substring(0, 4).equals(DISCOVER_PREFIX))
//            return DISCOVER;

        return NONE;
    }

    public static String formattedCardNumber(String cardNumber) {
        cardNumber = cardNumber.replaceAll("\\D+", "");
        StringBuilder stringBuilder = new StringBuilder(cardNumber);
        for (int i = 0; i < stringBuilder.length(); i ++) {
            char c = CARD_PATTERN.charAt(i);

            if ((c != '#') && (c != stringBuilder.charAt(i))) {
                stringBuilder.insert(i, c);
            }
        }

        return stringBuilder.toString();
    }

    public static boolean isValidCard(String cardNumber) {
        cardNumber = cardNumber.replaceAll("\\D+","");
        if (!TextUtils.isEmpty(cardNumber) && cardNumber.length() >= 4)
            if (getCardType(cardNumber) == AMEX && cardNumber.length() == 15)
                return true;
//            if (getCardType(cardNumber) == VISA && ((cardNumber.length() == 13 || cardNumber.length() == 16)))
//                return true;
//            else if (getCardType(cardNumber) == MASTERCARD && cardNumber.length() == 16)
//                return true;
//            else if (getCardType(cardNumber) == DISCOVER && cardNumber.length() == 16)
//                return true;
        return false;
    }

    public static String formattedExpDate(String expDate) {
        expDate = expDate.replaceAll("\\D+", "");
        int length = expDate.length();
        String str = expDate;
        if (length > 0 && length < 3) {
            int month = Integer.parseInt(expDate);
            if (length == 1 && month >= 2) {
                str = "0" + month + "/";
            } else if (length == 2 && month <= 12) {
                str = month + "/";
            } else if (length == 2 && month > 12) {
                str = "01/" + expDate.substring(1) ;
            }
        }

        StringBuilder stringBuilder = new StringBuilder(str);
        for (int i = 0; i < stringBuilder.length(); i ++) {
            char c = EXPDATE_PATTERN.charAt(i);

            if ((c != '#') && (c != stringBuilder.charAt(i))) {
                stringBuilder.insert(i, c);
            }
        }

        return stringBuilder.toString();
    }

    public static boolean isValidExpDate(String cardValidity) {
        if (!TextUtils.isEmpty(cardValidity) && cardValidity.length() == 5) {
            String month = cardValidity.substring(0, 2);
            String year = cardValidity.substring(3, 5);

            int monthpart = -1, yearpart = -1;

            try {
                monthpart = Integer.parseInt(month) - 1;
                yearpart = Integer.parseInt(year);

                Calendar current = Calendar.getInstance();
                current.set(Calendar.DATE, 1);
                current.set(Calendar.HOUR, 12);
                current.set(Calendar.MINUTE, 0);
                current.set(Calendar.SECOND, 0);
                current.set(Calendar.MILLISECOND, 0);

                Calendar validity = Calendar.getInstance();
                validity.set(Calendar.DATE, 1);
                validity.set(Calendar.HOUR, 12);
                validity.set(Calendar.MINUTE, 0);
                validity.set(Calendar.SECOND, 0);
                validity.set(Calendar.MILLISECOND, 0);

                if (monthpart > -1 && monthpart < 12 && yearpart > -1) {
                    validity.set(Calendar.MONTH, monthpart);
                    validity.set(Calendar.YEAR, yearpart + 2000);
                } else
                    return false;

                Log.d("Util", "isValidDate: " + current.compareTo(validity));

                if (current.compareTo(validity) <= 0)
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}