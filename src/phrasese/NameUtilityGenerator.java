package phrasese;

/**
 * Created by Sultan on 5/24/2016.
 */
public class NameUtilityGenerator {
    public final static char MIN_DIGIT = '0';
    public final static char MAX_DIGIT = '9';
    public final static char MIN_LETTER = 'a';
    public final static char MAX_LETTER = 'z';

    public static String prestr = "cips";

    private static String current_subname = "a";

    public static String getNextName()
    {

        String str= CIPSPrefexIt(current_subname);
        current_subname = incrementedAlpha(current_subname);
        return str;
    }


    private static String CIPSPrefexIt(String str)
    {
        return new StringBuilder().append(prestr).append(str).toString();
    }

    private static String  incrementedAlpha(String original) {
        StringBuilder buf = new StringBuilder(original);
        //int index = buf.length() -1;
        int i = buf.length() - 1;
        //while(index >= 0) {
        while (i >= 0) {
            char c = buf.charAt(i);
            c++;
            // revisar si es numero
            if ((c - 1) >= MIN_LETTER && (c - 1) <= MAX_LETTER) {
                if (c > MAX_LETTER) { // overflow, carry one
                    buf.setCharAt(i, MIN_LETTER);
                    i--;
                    continue;
                }

            } else {
                if (c > MAX_DIGIT) { // overflow, carry one
                    buf.setCharAt(i, MIN_DIGIT);
                    i--;
                    continue;
                }
            }
            // revisar si es numero
            buf.setCharAt(i, c);
            return buf.toString();
        }
        // overflow at the first "digit", need to add one more digit
        buf.insert(0, MIN_LETTER);
        return buf.toString();
    }
}
