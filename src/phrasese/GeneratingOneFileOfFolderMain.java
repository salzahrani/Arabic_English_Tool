package phrasese;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sultan on 5/23/2016.
 */
public class GeneratingOneFileOfFolderMain
{
    public static String main_folder = "./";
    public static String corpora_folder = main_folder + "corpora/";
    public static String original_folder = corpora_folder + "original_Arabic/" ;  //"./movie_reviews/";// change here"original/";
    public static String folder1_original = original_folder + "FamilyWomenRisingKids/";
    public static String folder2_original = original_folder + "ReligionFatwa/";
    public static String arff_file_original = "./weka/docs_original.txt";

    public static String filter_folder = corpora_folder + "filtered/";
    public static String folder1_filter  = filter_folder + "FamilyWomenRisingKids/";
    public static String folder2_filter  = filter_folder + "ReligionFatwa/";
    public static String arff_file_filter = "./weka/docs_filtered.txt";

    public static String stemmed_folder = corpora_folder + "stemmed/";
    public static String folder1_stemmed  = stemmed_folder + "FamilyWomenRisingKids/";
    public static String folder2_stemmed = stemmed_folder + "ReligionFatwa/";
    public static String arff_file_stemmed = "./weka/docs_stemmed.txt";
    public static Set<String> stoplists = new HashSet<String>();
    public static Set<String> stoplists_stemmed = new HashSet<String>();

    public static String stemmerName = "";


    public static void main(String[] args)
    {
        stemmerName = "arabic";
        //stemmerName = "english";
        String file_name_of_stopwords_1 = "./Stoplists/" + stemmerName + "_stoplist.txt";
        String file_name_of_stopwords_2 = "./Stoplists/" + stemmerName + "_stoplist_stemmed.txt";
        boolean removeStopWord = true;

        if(removeStopWord)
            loadStopList(file_name_of_stopwords_1,file_name_of_stopwords_2);

        System.out.println("I'm in phrases");
        createARFF(folder1_original,folder2_original,arff_file_original);
        createARFF(folder1_filter,folder2_filter,arff_file_filter);
        createARFF(folder1_stemmed,folder2_stemmed,arff_file_stemmed);
    }

    public static void loadStopList(String fileName_1,String fileName_2)
    {
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName_1))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    stoplists.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName_2))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    stoplists_stemmed.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createARFF(String folder1, String folder2,String arff_file)
    {
        System.out.println("Creating ARFF file");

        ArrayList<String> a_lst_1 = readFolderToList(folder1);
        ArrayList<String> a_lst_2 = readFolderToList(folder2);

        String cls1 = "FamilyWomenRisingKids";
        String cls2 = "ReligionFatwa";

        print_arff(arff_file,cls1,cls2,a_lst_1,a_lst_2);


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

    public static void print_arff(String fileName,String cls1,
                                  String cls2,ArrayList<String> a1,
                                  ArrayList<String> a2)
    {
        /*
        String header =
                "@relation C__Users_Sultan_IdeaProjects_JavaArabic_corpora \n" +

                        "@attribute text string \n" +

                        "@attribute @@class@@ {FamilyWomenRisingKids,ReligionFatwa} \n" +

                        "@data \n";
        */
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
            //bw.write(header);
            for(String str: a1)
            {
                bw.write(cleanTextFunc(str)+"\n");
            }
            for(String str: a2)
            {
                bw.write(cleanTextFunc(str)+"\n");
            }
            // close connection
            bw.close();
        }catch(Exception e){
            System.out.println(e);
        }

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

        String[] words =null;
        if(stemmerName.equals("arabic"))
        {
            words = str.replaceAll("^\\p{L}", "").split("\\s+");
            str = str.replaceAll("\\w+"," ");

        }
        else
            words = str.replaceAll("^\\W", "").split("\\s+");StringBuilder builder = new StringBuilder();

        for(String s : words) {
            if(stoplists.contains(s)) continue;
            builder.append(s);
            builder.append(" ");

        }
        return builder.toString();
    }


}
