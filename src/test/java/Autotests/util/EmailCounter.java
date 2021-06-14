package Autotests.util;

public class EmailCounter {

    public static String nextEmail(String email) {
        int atSignIndex = email.indexOf("@");
        int plusIndex = email.indexOf("+");
        if (atSignIndex > 0 && plusIndex > 0) {
            int emailNumber = Integer.parseInt(email.substring(plusIndex + 1, atSignIndex));
            emailNumber++;

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(email, 0, plusIndex + 1);
            stringBuilder.append(emailNumber);
            stringBuilder.append(email, atSignIndex, email.length());
            return stringBuilder.toString();
        } else {
            return email;
        }
    }

}
