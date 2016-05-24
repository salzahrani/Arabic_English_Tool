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



public class EncodeTextMain {



    Map word_to_code = new HashMap<String,String>();
    Map code_to_word = new HashMap<String,String>();

    public static void main(String args[])
    {
        System.out.println("Hello World!");
        printTest();
    }



    public static void printTest()
    {


        for(int i=0;i<1000;i++)
        {
           // String oldStr = newStr;
            //newStr =  incrementedAlpha(newStr);
            //System.out.println(CIPSPrefexIt(oldStr)+ " | " + CIPSPrefexIt(newStr));
            System.out.println(NameUtilityGenerator.getNextName());
        }
         // returns ['A']
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

}
