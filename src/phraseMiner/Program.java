package phraseMiner;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sultan on 5/25/2016.
 *
 */
public class Program {

    public static final String PhraseMiner_Folder = "./PhraseMiner_Output/";
    public static String main_folder = "./";
    public static String corpora_folder = main_folder + "corpora/";
    public static String original_folder = corpora_folder + "English_Corpus/";//"original/" ;// "original/";"movie_reviews/" /// here where you change


    public static String[] folderNames;

    public static void main(String[] args) {
        main_folder = "./";
        corpora_folder = main_folder + "corpora/";
        original_folder = corpora_folder + "ph3/";//"original/" ;// "original/";"movie_reviews/" /// here where you change

        run(original_folder);
    }

    public static void run(String org_folder) {
        original_folder = org_folder;


        original_folder = org_folder;
        folderNames = getFoldersNames();

        try {
            FileUtils.deleteDirectory(new File(PhraseMiner_Folder));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initialize_folder(PhraseMiner_Folder);
        for(String str:folderNames)
        {
            String a_folder_name = PhraseMiner_Folder + str;
            initialize_folder(a_folder_name);
        }
        for(String a_folder:folderNames)
        {
            doPhraseMining(a_folder);
        }


        System.out.println("Now I am trying to run an script");
        File file = new File("./PhraseMiner/topicalPhrases/FamilyWomenRisingKids.bat");
        String batchFileName = file.getAbsolutePath();
        //String path="cmd /c start C:/Users/Sultan/IdeaProjects/JavaArabic/PhraseMiner/topicalPhrases/win_run.bat";
        String path = "cmd /c start /wait " + batchFileName;
        Runtime rn = Runtime.getRuntime();
        try {
            final Process pr = rn.exec(path);
            pr.waitFor();
            //System.out.println("exitVal = " + exitVal);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void doPhraseMining(String a_folder)
    {
        System.out.println("Phrase Mining for " +a_folder );
        String fldr_name  = original_folder + a_folder;
        String file_name = "./PhraseMiner/topicalPhrases/rawFiles/"+a_folder.substring(0,a_folder.length()-1)+".txt";
        Generate_one_file.run(fldr_name,file_name);
        String fullpath_file_name = (new File(file_name)).getAbsolutePath();
        fullpath_file_name = fullpath_file_name.replace("\\.","");
        System.out.println(fullpath_file_name);
        create_batch_file(a_folder,fullpath_file_name);
    }

    public static void write_list_to_file(String pfileName,ArrayList<String> p_lst)
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

    public static void create_batch_file(String a_folder,String fullpath_file_name)
    {
        System.out.println("Creating "+a_folder +"batch file");

        String a_batch_file_name = "./PhraseMiner/topicalPhrases/"+a_folder.substring(0,a_folder.length()-1)+".bat";
        ArrayList<String> lines_r = readFileToList("./tmp/base.bat");
        ArrayList<String> lines = new ArrayList<String>();
        int count = 1;
        for(String str:lines_r)
        {
            System.out.println(count + "--" +str);
            String repstr = "";

            if(count == 6)
            {
                repstr = "@set inputFile=" + fullpath_file_name;
                System.out.println(count + "--" + repstr);
                lines.add(repstr);
            }
            else
                lines.add(str);
            ++count;
        }


        write_list_to_file(a_batch_file_name,lines);

    }

    public static ArrayList<String> readFileToList(String fileName)
    {
        ArrayList<String> lst = new ArrayList<String>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    lst.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  lst;
    }

    public static void initialize_folder(String filderName) {
        File file = new File(filderName);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        } else {
            // deleting existing files
            File[] flist = file.listFiles();
            if (flist != null && flist.length > 0) {
                for (File f : flist) {
                    f.delete();
                }
            }
        }

    }

    public static String[] getFoldersNames() {
        String[] arr_of_fodler = null;
        ArrayList<String> list_of_folders = new ArrayList<String>();

        File folder = new File(original_folder);
        File[] listOfFiles = folder.listFiles();


        for (int i = 0; i < listOfFiles.length; i++)
        {
            File file = listOfFiles[i];
            if (file.isDirectory()) {
                String fileName = file.getName()+"/";
                list_of_folders.add(fileName);
            }
        }

        Collections.sort(list_of_folders);
        arr_of_fodler = new String[list_of_folders.size()];
        arr_of_fodler = (String[]) list_of_folders.toArray(arr_of_fodler);
        return arr_of_fodler;
    }
}