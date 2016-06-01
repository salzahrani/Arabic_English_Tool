package topFrequentNgram;


import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

 /**
 * Created by Sultan on 5/30/2016.
 */
public class Generate_one_file {
    public static String folderName = "";
    public static String fileName = "";
    public  static ArrayList<String> lst = null;
    public static void run(String pfolderName,String pfileName)
    {
        setFolderName(pfolderName);
        setFileName(pfileName);
        readFolderToList(folderName);
        lst = readFolderToList(folderName);

        write_a_single_file(fileName,lst);

    }
    public static void setFolderName(String pfolderName) {
        folderName = pfolderName;
    }
    public static void setFileName(String pfileName) {
        fileName = pfileName;
    }
    public String getFileName() {
        return fileName;
    }
    public String getFolderName() {
        return folderName;
    }

    ////////////////////////////////

    public static ArrayList<String> readFolderToList(String pfolderName)
    {
        File folder = new File(pfolderName);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> a_list = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {
                try {
                    String content = FileUtils.readFileToString(file,"UTF-8");
                    a_list.add(content);

                } catch (IOException e) {
                    e.printStackTrace();
                }
    /* do somthing with content */
            }
        }
        return a_list;
    }

    public static void write_a_single_file(String pfileName,ArrayList<String> p_lst)
    {
        try{
            // create new file
            File file = new File(pfileName);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(String str: p_lst)
            {
                bw.write(str+"\n");
            }

            // close connection
            bw.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }


}
