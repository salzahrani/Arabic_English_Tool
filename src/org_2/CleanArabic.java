package org_2; /**
 * Created by Sultan on 5/19/2016.
 * update on laptop
 */

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class CleanArabic {

    public static String main_folder = "./";
    public static String corpora_folder = main_folder + "corpora/";
    public static String original_folder = corpora_folder + "original/";//"original/" ;// "original/";"movie_reviews/" /// here where you change
    public static String folder1_original = original_folder + "FamilyWomenRisingKids/";
    public static String folder2_original = original_folder + "ReligionFatwa/";
    public static String arff_file_original = "./weka/docs_original.arff";

    public static String filter_folder = corpora_folder + "filtered/";
    public static String folder1_filter  = filter_folder + "FamilyWomenRisingKids/";
    public static String folder2_filter  = filter_folder + "ReligionFatwa/";
    public static String arff_file_filter = "./weka/docs_filtered.arff";

    public static String stemmed_folder = corpora_folder + "stemmed/";
    public static String folder1_stemmed  = stemmed_folder + "FamilyWomenRisingKids/";
    public static String folder2_stemmed = stemmed_folder + "ReligionFatwa/";
    public static String arff_file_stemmed = "./weka/docs_stemmed.arff";

    public static Set<String> stoplists = new HashSet<String>();
    public static Set<String> stoplists_stemmed = new HashSet<String>();
    public static String stemmerName = "";

    public static void main(String[] args)
    {
        System.out.println("I am in branch 1");
        readCrateSameDirectoriesAsOrgn();
        boolean removeStopWord = true;

        // Stemming StopWords (Arabic) Already done and file generated
        /*
        StemmingStopWords("./StopLists/arabic_stoplist.txt",
                          "./StopLists/arabic_stoplist_stemmed.txt"
                          ,"arabic");
        StemmingStopWords("./StopLists/english_stoplist.txt",
                "./StopLists/english_stoplist_stemmed.txt"
                ,"english");
        StemmingStopWords("./StopLists/turkish_stoplist.txt",
                "./StopLists/turkesh_stoplist_stemmed.txt"
                ,"arabic");

        */
                //stemmerName = "arabic";
        stemmerName = "arabic";
        // Load the designated
        String file_name_of_stopwords_1 = "./Stoplists/" + stemmerName + "_stoplist.txt";
        String file_name_of_stopwords_2 = "./Stoplists/" + stemmerName + "_stoplist_stemmed.txt";

        if(removeStopWord)
            loadStopList(file_name_of_stopwords_1,file_name_of_stopwords_2);

        //Copying file (with filters propose)
        FilterTextFromSrcToDestFolder(folder1_original,folder1_filter);
        FilterTextFromSrcToDestFolder(folder2_original,folder2_filter);

        // Stemming folder

        StemTextFromSrcToDestFolder(folder1_filter,folder1_stemmed,stemmerName);
        StemTextFromSrcToDestFolder(folder2_filter,folder2_stemmed,stemmerName);

        //Generating ARFFs
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

    public static Class stemClass;
    public static Map stemToSetOfWords=  new HashMap();;

    public static void StemmingStopWords(String originalFile,
                                         String targetFile,
                                         String stemmerName)
    {
        try {
            // temporarily

            System.out.println("---------------------------------------------");


            stemClass = Class.forName("org_2.ext2." +
                    stemmerName + "Stemmer");
            SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();


            InputStream instream;
            instream = new FileInputStream(originalFile);
            //instream = System.in;
            Reader reader = new InputStreamReader(instream);
            reader = new BufferedReader(reader);

            OutputStream outstream;
            outstream = new FileOutputStream(targetFile);
            //outstream = System.out;
            Writer output = new OutputStreamWriter(outstream);
            output = new BufferedWriter(output);



            StringBuffer input = new StringBuffer();
            int character;
            while ((character = reader.read()) != -1)
            {
                char ch = (char) character;
                if (Character.isWhitespace(ch)) {
                    String inputString = input.toString();
                    stemmer.setCurrent(inputString);
                    stemmer.stem();
                    String stemmedString = stemmer.getCurrent();
                    output.write(stemmedString);
                    output.write("\n");

                    //System.out.println("inputString " + inputString + " >> Stemmed: " + stemmedString);
                    if (inputString.equals(stemmedString) == false) {
                        Set<String> a_set;

                        if (stemToSetOfWords.containsKey(stemmedString)) {
                            //key exists
                            a_set = (HashSet<String>) stemToSetOfWords.get(stemmedString);
                            a_set.add(inputString);
                            // if(stemmedString.isEmpty()){  output.write(inputString); output.write(' ');}

                        } else {
                            a_set = new HashSet<String>();
                            stemToSetOfWords.put(stemmedString, a_set);
                            a_set.add(inputString);
                        }
                    }

                    input.delete(0, input.length());
                } else {
                    input.append(ch < 127 ? Character.toLowerCase(ch) : ch);
                }
            }
            output.flush();

            generateMapStemFile();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void StemTextFromSrcToDestFolder(String originalFolder,
                                                   String targetFolder,
                                                   String stemmerName)
    {
        try {
            // temporarily
            FilterTextFromSrcToDestFolder(originalFolder, targetFolder);


            System.out.println("---------------------------------------------");

            stemToSetOfWords = new HashMap();

            //Set<String> a_set = new HashSet<String>();

            File folder = new File(originalFolder);
            File[] listOfFiles = folder.listFiles();


            for (int i = 0; i < listOfFiles.length; i++) {

                File file = listOfFiles[i];
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    stemClass = Class.forName("org_2.ext2." +
                            stemmerName + "Stemmer");
                    SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();

                    System.out.println("Stemming file ("+i+") "+ file.getPath());
                    //String content = FileUtils.readFileToString(file,"UTF-8");
                    //String content_after_process = cleanTextFunc(content);
                    String fileName = targetFolder + file.getName();
                    //writeToFile(fileName,content_after_process);


                    InputStream instream;
                    instream = new FileInputStream(file.getPath());
                    //instream = System.in;
                    Reader reader = new InputStreamReader(instream);
                    reader = new BufferedReader(reader);

                    OutputStream outstream;
                    outstream = new FileOutputStream(fileName);
                    //outstream = System.out;
                    Writer output = new OutputStreamWriter(outstream);
                    output = new BufferedWriter(output);



                    StringBuffer input = new StringBuffer();
                    int character;
                    while ((character = reader.read()) != -1) {
                        char ch = (char) character;
                        if (Character.isWhitespace(ch)) {
                            String inputString = input.toString();
                            stemmer.setCurrent(inputString);
                            stemmer.stem();
                            String stemmedString = stemmer.getCurrent();
                            if(!stoplists_stemmed.contains(stemmedString)) // don't write if it is stopword
                            {
                                output.write(stemmedString);
                                output.write(' ');
                            }
                            //System.out.println("inputString " + inputString + " >> Stemmed: " + stemmedString);
                            if (inputString.equals(stemmedString) == false)
                            {
                                Set<String> a_set;

                                if(stemToSetOfWords.containsKey(stemmedString)){
                                    //key exists
                                    a_set = (HashSet<String>) stemToSetOfWords.get(stemmedString);
                                    a_set.add(inputString);
                                    // if(stemmedString.isEmpty()){  output.write(inputString); output.write(' ');}

                                }else{
                                    a_set = new HashSet<String>();
                                    stemToSetOfWords.put(stemmedString,a_set);
                                    a_set.add(inputString);
                                }
                            }

                            input.delete(0, input.length());
                        } else {
                            input.append(ch < 127 ? Character.toLowerCase(ch) : ch);
                        }
                    }
                    output.flush();
                }
            }
            generateMapStemFile();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public static String getSetString(Set<String> a_set)
    {

        StringBuilder result = new StringBuilder();
        for(String string : a_set) {
            result.append(string);
            result.append(" , ");
        }
        return result.length() > 0 ? result.substring(0, result.length() - 2): "";
    }

    public static void generateMapStemFile()
    {

        try
        {
            // create new file
            File file = new File("C:/Users/Sultan/IdeaProjects/JavaArabic/StemMap/map.txt");

            // if file doesnt exists, then create it
            if (!file.exists())
            {
                file.createNewFile();
            }
            System.out.println("Absolute Path: "+file.getAbsolutePath());
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            // write in file

            Iterator entries = stemToSetOfWords.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String)entry.getKey();
                Set<String> value = (HashSet<String>)entry.getValue();
                String string_to_write = "Key = " + key + ", Value = " + getSetString(value);
                System.out.println(string_to_write);
                bw.write(string_to_write + "\n");
            }
            bw.flush();
            bw.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }


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
        initialize_folder(stemmed_folder);
        initialize_folder(folder1_stemmed);
        initialize_folder(folder2_stemmed);

        // first reading textual content in each folder and wirte it as it is to its
        // corresponding folder.


    }


    public static void FilterTextFromSrcToDestFolder(String originalFolder,
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
                    String fileName = targetFolder + file.getName();
                    writeToFile(fileName,content_after_process);

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




        String[] words =null;
        if(stemmerName.equals("arabic"))
        {
            words = str.replaceAll("^\\p{L}", "").split("\\s+");
            str = str.replaceAll("\\w+"," ");

        }
        else
            words = str.replaceAll("^\\W", "").split("\\s+");
        StringBuilder builder = new StringBuilder();
        for(String s : words) {
            if(stoplists.contains(s)) continue;
            builder.append(s);
            builder.append(" ");

        }
        return builder.toString();
    }

}

