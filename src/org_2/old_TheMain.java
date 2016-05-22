package org_2;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sultan on 5/18/2016.
 */
public class old_TheMain
{
    public static String folder1  = "C:\\Users\\Sultan\\IdeaProjects\\JavaArabic\\corpora\\FamilyWomenRisingKids\\";
    public static String fodler2  = "C:\\Users\\Sultan\\IdeaProjects\\JavaArabic\\corpora\\ReligionFatwa\\";

    public static ArrayList<String> a1 =  new ArrayList<String>();
    public static ArrayList<String> a2 =  new ArrayList<String>();

    public static void main(String[] args)
    {
        String arabic_test_string = "الله اكبر";
        System.out.println("Working on the first folder");
        a1 = readFolderToList(folder1);
        System.out.println("Working on the second folder");
        a2 = readFolderToList(fodler2);
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

}
