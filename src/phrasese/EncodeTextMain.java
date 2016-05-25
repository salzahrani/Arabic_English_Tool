package phrasese;

/**
 * Created by Sultan on 5/24/2016.
 */
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class EncodeTextMain {



    public static Map word_to_code = new HashMap<String,String>();
    public static Map code_to_word = new HashMap<String,String>();

    public static void main(String args[])
    {
        System.out.println("Hello World!");
        //printTest(); Testing 1000 incremental string
        //String file_org = "./test/f1.txt";
        String file_org = "./test/docs_filtered.txt";
        String file_filtered = "./test/f2.txt";
        String file_endoced = "./test/f3.txt";

        encode_file(file_org,file_filtered,file_endoced);

    }

    public static ArrayList<String> readFileToList(String fileName)
    {
        ArrayList<String> lst = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                lst.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lst;
    }


    public static void printTest()
    {


        for(int i=0;i<1000;i++)
        {
            // String oldStr = newStr;
            // newStr =  incrementedAlpha(newStr);
            // System.out.println(CIPSPrefexIt(oldStr)+ " | " + CIPSPrefexIt(newStr));
            System.out.println(NameUtilityGenerator.getNextName());
        }
         // returns ['A']
    }

    public static void encode_file(String file_org,String file_filtered, String file_endoced)
    {
        String content = "";//readFile(file_org);
        String[] words = null;
        ArrayList<String> lst = readFileToList(file_org);
        ArrayList<String> lst_encoded = new ArrayList<String>();

        for(String a_line:lst)
        {
            content = new String(a_line);
            content = content.replaceAll("\\p{N}+", " ");
            content = content.replaceAll("\\w+", " ");
            content = content.replace("\\d+", " ");
            words = content.split("\\s+");
            StringBuilder builder_flt = new StringBuilder();
            StringBuilder builder_encoded = new StringBuilder();
            for (String s : words) {
                encode_a_word(s);
                builder_flt.append(s);
                builder_flt.append(" ");
                builder_encoded.append(encode_a_word(s));
                builder_encoded.append(" ");
                lst.add(builder_flt.toString());
                lst_encoded.add(builder_encoded.toString());
            }

        }
        write_lst_File(file_filtered,lst);
        write_lst_File(file_endoced,lst_encoded);

    }

    public static String encode_a_word(String str)
    {
        String the_code = "";
        if( ! word_to_code.containsKey(str) ) {
            the_code = NameUtilityGenerator.getNextName();
            word_to_code.put(str, the_code);
            code_to_word.put(the_code,str);
        }
        else
        {
            the_code = (String)word_to_code.get(str);
        }
        return the_code;
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
    public static void write_lst_File(String fileName, ArrayList<String> lst)
    {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName)));
            for(String str:lst)
                writer.write(new StringBuilder().append(str).append("\n").toString());
        } catch (IOException ex) {
            // report
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }

    }

}
