/**
 * Created by Sultan on 5/19/2016.
 */

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;

public class CleanArabic {

    public static String main_folder = "C:/Users/Sultan/IdeaProjects/JavaArabic/";
    public static String corpora_folder = main_folder + "corpora/";
    public static String original_folder = corpora_folder + "original/";
    public static String folder1_original = original_folder + "FamilyWomenRisingKids/";
    public static String folder2_original = original_folder + "ReligionFatwa/";
    public static String arff_file_original = "./weka/docs_original.arff";

    public static String filter_folder = corpora_folder + "filtered/";
    public static String folder1_filter  = filter_folder + "FamilyWomenRisingKids/";
    public static String folder2_filter  = filter_folder + "ReligionFatwa/";
    public static String arff_file_filter = "./weka/docs_filtered.arff";
    public static void main(String[] args)
    {

        readCrateSameDirectoriesAsOrgn();
        // To create ARFF there is a two configuration:
        // (1) Create from the original folder
        // (2) Create from from the filtered folder.

        //Copying file (with filters propose)
        copyingTextFilesFromFolderToAnother(folder1_original,folder1_filter);
        copyingTextFilesFromFolderToAnother(folder2_original,folder2_filter);

        //Generating ARFFs
        createARFF(folder1_original,folder2_original,arff_file_original);
        createARFF(folder1_filter,folder2_filter,arff_file_filter);

    }


    /*public static void ReadWriteOneLine(String fileName_toread,String fileName_towrite)
    {
        System.out.println("To read: " + fileName_toread);
        System.out.println("To write: " + fileName_towrite);
        Scanner scan = null;
        try {
            scan = new Scanner(new File(fileName_toread));
            System.out.println("Init scanner...");
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                //Here you can manipulate the string the way you want
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
    */

    public static void initialize_folder(String filderName)
    {
        File file = new File(filderName);
        if (!file.exists())
        {
            if (file.mkdir())
            {
                System.out.println("Directory is created!");
            } else
            {
                System.out.println("Failed to create directory!");
            }
        }
        else
        {
            File[] flist = file.listFiles();
            if (flist != null && flist.length > 0) {
                for (File f : flist) {
                    f.delete();
                }
            }
        }

    }
    public static void readCrateSameDirectoriesAsOrgn()
    {
        // It will try creating 2 folders in fiiltered folder as it listed under
        // original, if already created, then it would basically wiped its content.

        initialize_folder(filter_folder);
        initialize_folder(folder1_filter);
        initialize_folder(folder2_filter);

        // first reading textual content in each folder and wirte it as it is to its
        // corresponding folder.


    }


    public static void copyingTextFilesFromFolderToAnother(String originalFolder,
                                                           String targetFolder)
    {
        File folder = new File(originalFolder);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> a_list = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {
                try {
                    String content = FileUtils.readFileToString(file,"UTF-8");
                    String content_after_process = cleanTextFunc(content);
                    String fileName = file.getName();
                    //System.out.println("File Name: "+ fileName);

                    writeToFile(targetFolder+fileName,content_after_process);

                } catch (IOException e) {
                    e.printStackTrace();
                }
    /* do somthing with content */
            }
        }

    }

    public static ArrayList<String> readFolderToList(String folderName)
    {
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> a_list = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {
                try {
                    String content = FileUtils.readFileToString(file);
                    a_list.add(content);

                } catch (IOException e) {
                    e.printStackTrace();
                }
    /* do somthing with content */
            }
        }
        return a_list;
    }

    // This function create a single arff containing all textual contents of original folder....
    public static void createARFF(String folder1, String folder2,String arff_file)
    {
        System.out.println("Creating ARFF file");

        ArrayList<String> a_lst_1 = readFolderToList(folder1);
        ArrayList<String> a_lst_2 = readFolderToList(folder2);

        String cls1 = "FamilyWomenRisingKids";
        String cls2 = "ReligionFatwa";

        print_arff(arff_file,cls1,cls2,a_lst_1,a_lst_2);


        /*
        String current = null;
        try {
            current = new File( "." ).getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Current dir:"+current);
        */
    }

    public static void writeToFile(String fileName, String str)
    {
        try
        {
            // create new file
            File file = new File(fileName);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            // write in file
            bw.write(str);
            bw.flush();
            bw.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public static void print_arff(String fileName,String cls1,
                                  String cls2,ArrayList<String> a1,
                                  ArrayList<String> a2)
    {
        String header =
                "@relation C__Users_Sultan_IdeaProjects_JavaArabic_corpora \n" +

                        "@attribute text string \n" +

                        "@attribute @@class@@ {FamilyWomenRisingKids,ReligionFatwa} \n" +

                        "@data \n";
        try{
            // create new file
            File file = new File(fileName);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            // write in file
            bw.write(header);
            for(String str: a1)
            {
                bw.write(formatTheArffLine(str,cls1));
            }
            for(String str: a2)
            {
                bw.write(formatTheArffLine(str,cls2));
            }
            // close connection
            bw.close();
        }catch(Exception e){
            System.out.println(e);
        }

    }

    public static String formatTheArffLine(String text,String cls)

    {
        return "'"+cleanTextFunc(text)+"'"+","+cls+"\n";
    }

    public static String cleanTextFunc(String str)
    {
        str = str.replaceAll("\\p{Punct}+", " ");
        str = str.replaceAll("؟", " ");
        str = str.replaceAll("،", " ");
        str = str.replaceAll("–", " ");
        str = str.replaceAll(":", " ");
        str = str.replaceAll(";", " ");
        str = str.replaceAll("»", " ");
        str = str.replaceAll("«", " ");
        str = str.replaceAll("\\d+"," ");
        str = str.replaceAll("\\w+"," ");




        String[] words = str.replaceAll("^\\p{L}", "").split("\\s+");
        StringBuilder builder = new StringBuilder();
        for(String s : words) {
            builder.append(s);
            builder.append(" ");

        }
        return builder.toString();
    }
}

