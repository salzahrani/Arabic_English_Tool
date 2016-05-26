package phrasese;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sultan on 5/25/2016.
 */
public class DecodeAFolder

{
    public static Map word_to_code = null;//= new HashMap<String,String>();
    public static Map code_to_word = null;//= new HashMap<String,String>();
    public static String serialize_maps_fileName = "./ObjectSerDesr/Maps.ser";
    public static String folder_name = "./PhraseMiner/topicalPhrases/output/outputFiles/";
    public static void main(String args[])
    {
        deserializeMaps();
        decoded_folder(folder_name);
    }

    public static ArrayList<String> decoded_folder(String folderName)
    {
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> a_list = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {

                //String content = FileUtils.readFileToString(file,"UTF-8");
                //String decoded = decode_an_string_keep_format(content);
                String newFileName = new String(file.getName());
                newFileName = newFileName.substring(newFileName.lastIndexOf(".") -1)+"_decoded.txt";
                decode_and_write_new_file(folderName + file.getName(),
                        folderName + "decodedFiles/" + newFileName);
                //writeFile(folderName + newName,content);
  /* do somthing with content */
            }
        }
        return a_list;
    }

    public static void decode_and_write_new_file(String fileName,String newFileName)
    {
        ArrayList<String> lines = readFileToList(fileName);
        ArrayList<String> lines_decode = new ArrayList<String>();

        for(String a_line: lines)
        {
            lines_decode.add(process_string(a_line));
        }

        write_lst_File(newFileName,lines_decode);
    }


    public static String process_string(String str)
    {
        Pattern p = Pattern.compile("cips\\w+");
        Matcher m = p.matcher(str);
        while(m.find()) {
            String an_str = m.group();
            System.out.println(an_str);
            //System.out.println("The decode: " + decode_a_word(an_str));

            str = str.replace(an_str,decode_a_word(an_str));

        }
        return str;

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
