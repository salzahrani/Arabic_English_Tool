package phrasese;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sultan on 5/25/2016.
 */
public class DecodeTextMain
{
    public static Map word_to_code = null;//= new HashMap<String,String>();
    public static Map code_to_word = null;//= new HashMap<String,String>();
    public static String serialize_maps_fileName = "./ObjectSerDesr/Maps.ser";

    public static void main(String args[])
    {
        String file_org = "./test/docs_filtered.txt";

        String file_filtered = "./test/ph2.txt";
        String file_ecoded = "./test/ph3.txt";
        String file_decoded = "./test/ph4.txt";

        deserializeMaps();

        decode_file(file_ecoded,  file_decoded);

    }

    public static void deserializeMaps()
    {

        MapsObject mo = null;
        try
        {
            FileInputStream fileIn = new FileInputStream(serialize_maps_fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            mo = (MapsObject) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i)
        {
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c)
        {
            System.out.println("MapsObject class not found");
            c.printStackTrace();
            return;
        }
        word_to_code = mo.getWord_to_code();
        code_to_word = mo.getCode_to_word();
        System.out.println(word_to_code.size());
        System.out.println(code_to_word.size());
    }



    public static String decode_a_word(String str) {
        String the_word = "";
        if (code_to_word.containsKey(str)) {
            return (String)code_to_word.get(str);
        }
        return "";
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

    public static void decode_file(String file_ecoded, String file_decoded)
    {
        String content = "";//readFile(file_org);
        String[] codes = null;
        ArrayList<String> lst_lines_enconded = readFileToList(file_ecoded);
        ArrayList<String> lst_lines_decoded = new ArrayList<String>();
        ArrayList<String> lst_a_line_encoded = new ArrayList<String>();
        ArrayList<String> lst_a_line_decoded = new ArrayList<String>();

        for(String a_line:lst_lines_enconded)
        {
            lst_a_line_encoded.clear();
            lst_a_line_decoded.clear();

            content = new String(a_line);
            /*
            content = content.replaceAll("\\p{N}+", " ");
            content = content.replaceAll("\\w+", " ");
            content = content.replace("\\d+", " ");
            */
            codes = content.split("\\s+");
            StringBuilder builder_encoded = new StringBuilder();
            StringBuilder builder_decoded = new StringBuilder();

            for (String a_code : codes)
            {
                if(a_code.equals("")) continue;
                String a_word_encoded  = decode_a_word(a_code);
                builder_encoded.append(a_code);
                builder_encoded.append(" ");
                builder_decoded.append(a_word_encoded);
                builder_decoded.append(" ");
            }



            //lst_lines_filtered.add(builder_encoded.toString());
            lst_lines_decoded.add(builder_decoded.toString());

        }
        //write_lst_File(file_filtered,lst_lines_filtered);
        write_lst_File(file_decoded,lst_lines_decoded);

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
