/**
 * Created by Sultan on 5/19/2016.
 */

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CleanArabic {
    public static String folder1  = "C:/Users/Sultan/IdeaProjects/JavaArabic/corpora/FamilyWomenRisingKids/";
    public static String folder2  = "C:/Users/Sultan/IdeaProjects/JavaArabic/corpora/ReligionFatwa/";

    public static void main(String[] args)
    {
        System.out.println("Second Hello World!");
        //String file_name_to_read = "./weka/docs.arff";
        //String file_name_to_read = "C:\\Users\\Sultan\\IdeaProjects\\JavaArabic\\weka\\docs.arff";
        //String file_name_to_read = "C:/Users/Sultan/Desktop/tmp/test.txt";
        String file_name_to_read = "C:/Users/Sultan/Desktop/tmp/tes2t.txt";
        String file_name_to_write = "./weka/docs_2.arff";

        //ReadWriteOneLine(file_name_to_read,file_name_to_write);

        ArrayList<String> a_lst_1 = readFolderToList(folder1);
        ArrayList<String> a_lst_2 = readFolderToList(folder2);

        String cls1 = "FamilyWomenRisingKids";
        String cls2 = "ReligionFatwa";

        print_arff("./weka/docs_manual.arff",cls1,cls2,a_lst_1,a_lst_2);


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
        return "'"+text+"'"+","+cls+"\n";
    }

    public static String cleanTextFunc(String str)
    {
        return str;
    }
}

