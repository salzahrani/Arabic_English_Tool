package phrasese;

/**
 * Created by Sultan on 5/24/2016.
 */
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class EncodeText {

    public final static char MIN_DIGIT = '0';
    public final static char MAX_DIGIT = '9';
    public final static char MIN_LETTER = 'A';
    public final static char MAX_LETTER = 'Z';


    public static String prestr = "CIPS";

    Map word_to_code = new HashMap<String,String>();
    Map code_to_word = new HashMap<String,String>();

    public static void main(String args[])
    {
        System.out.println("Hello World!");
        printTest();
    }

    public static void incrementLetter(String str)
    {
        int charValue = str.charAt(str.length()-1);

        String next = String.valueOf( (char) (charValue + 1));
        //Character.toChars(65); // returns ['A']
        System.out.println(next);
    }

    public static  String findNext(String str)
    {
        int seed = 65; //'A'
        char lastLetter = str.charAt(str.length()-1);
        String lastLetterString = String.valueOf(lastLetter);
        int currentIntVal = (int) lastLetter - seed;
        int nextcurrentIntVal =  getNextIntVal(currentIntVal)+seed;
        return (nextcurrentIntVal-seed == 0)? str.substring(0,str.length())+String.valueOf( (char) nextcurrentIntVal):str.substring(0,str.length()-1)+String.valueOf( (char) nextcurrentIntVal);
    }


    public static int getNextIntVal(int val)
    {
        return (val == 25)? 0:val+1;
    }


    public static void printTest()
    {
        String seed_Str = "";
        int seed = 65;
        int count = 0;
        String newStr = seed_Str+"A";
        for(int i=0;i<1000;i++)
        {
            String oldStr = newStr;
            newStr =  incrementedAlpha(newStr);
            System.out.println(CIPSPrefexIt(oldStr)+ " | " + CIPSPrefexIt(newStr));
        }
         // returns ['A']
    }

    public static String CIPSPrefexIt(String str)
    {
        return new StringBuilder().append("CIPS").append(str).toString();
    }

    public static String readFile(String fileName)
    {
        String everything = "";
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = null;

            line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
            everything = sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return everything;
    }

    public static String  incrementedAlpha(String original) {
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
    public static void writeFile(String fileName, String content)
    {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName)));
            writer.write(content);
        } catch (IOException ex) {
            // report
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }

    }
     /*  public static void printTest_old()
    {
        String seed_Str = "CIPS";
        int seed = 65;
        int count = 0;
        String newStr = seed_Str;
        for(int i=0;i<300;i++)
        {
            char a_letter = (char) (seed+count); // returns ['A']
            newStr = seed_Str + a_letter;
            System.out.println(newStr +" | " + findNext(newStr)  );
            count++;
            if(count == 26) count=0;
        }
        // returns ['A']
    }
    */
}
