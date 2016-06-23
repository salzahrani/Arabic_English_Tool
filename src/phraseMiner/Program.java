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
    public static String rawFile_folder = "./PhraseMiner/topicalPhrases/rawFiles/";
    public static String outputfile_folder = "./PhraseMiner/topicalPhrases/output/outputFiles/";
    public static String batchesmainFolder = "./PhraseMiner/topicalPhrases/";
    public static String root_PhraseMinder_output_folder = "./PhraseMiner_Output/";
    public static String map_object_folder = "./ObjectSerDesr/";
    public static boolean isBatch = true;
    public static String batch_shellScript_ext_file = ".bat";
    public static String[] folderNames;
    public static String stemmCLass = "";

    public static void main(String[] args) {
        main_folder = "./";
        corpora_folder = main_folder + "corpora/";
        original_folder = corpora_folder + "ph3/";//"original/" ;// "original/";"movie_reviews/" /// here where you change
        String stemmClass = "english";
        stemmClass = "arabic";
        run(original_folder,stemmClass);
    }

    public static void run(String org_folder,String pstemmClass) {
        original_folder = org_folder;


        stemmCLass = pstemmClass;
        original_folder = org_folder;
        folderNames = getFoldersNames();
        if(stemmCLass.equals("arabic"))
            EncodeTextMain.init_all();
        try {
            FileUtils.deleteDirectory(new File(PhraseMiner_Folder));
            FileUtils.deleteDirectory(new File(rawFile_folder));
            FileUtils.deleteDirectory(new File(outputfile_folder));
            deleteFileWithAnExtension(batchesmainFolder,".bat");
            deleteFileWithAnExtension(batchesmainFolder,".sh");


        } catch (IOException e) {
            e.printStackTrace();
        }

        initialize_folder(PhraseMiner_Folder);
        initialize_folder(rawFile_folder);
        initialize_folder(outputfile_folder);
        initialize_folder(map_object_folder);

        for(String str:folderNames)
        {
            String a_folder_name = PhraseMiner_Folder + str;
            initialize_folder(a_folder_name);
        }
        for(String a_folder:folderNames)
        {

            doPhraseMining(a_folder);
        }
    }

    public static void deleteFileWithAnExtension(String folderName,String an_extension)

    {

        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();


        for (int i = 0; i < listOfFiles.length; i++)
        {

            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(an_extension)) {
                file.delete();
            }
        }
    }
    public static void doPhraseMining(String a_folder)
    {
        System.out.println("Phrase Mining for " +a_folder );
        String fldr_name  = original_folder + a_folder;
        String file_name = "./PhraseMiner/topicalPhrases/rawFiles/"+
                a_folder.substring(0,a_folder.length()-1)+
                ".txt";
        Generate_one_file.run(fldr_name,file_name);
        //
        //
        if(stemmCLass.equals("arabic"))
        {
            String file_s = "./PhraseMiner/topicalPhrases/rawFiles/"+
                    a_folder.substring(0,a_folder.length()-1)+
                    ".txt";
            String file_encoeded = "./PhraseMiner/topicalPhrases/rawFiles/"+
                    a_folder.substring(0,a_folder.length()-1)+
                    "_encoded"+".txt";
            String tmp_file = "./tmp/ph2.txt";
            EncodeTextMain.encode_file(file_s,tmp_file,  file_encoeded);
            EncodeTextMain.SerializeMaps();

        }
        //
        //
        file_name = "./PhraseMiner/topicalPhrases/rawFiles/"+
                a_folder.substring(0,a_folder.length()-1)+
                (stemmCLass.equals("arabic")?"_encoded":"")+
                ".txt";

        String fullpath_file_name = (new File(file_name)).getAbsolutePath();
        fullpath_file_name = fullpath_file_name.replace("\\.","");
        System.out.println(fullpath_file_name);
        batch_shellScript_ext_file = isBatch ? ".bat" : ".sh" ;

        create_batch_file(a_folder,fullpath_file_name);
        // Execuiting:

        System.out.println("Now running an script");
        batch_shellScript_ext_file = isBatch ? ".bat" : ".sh" ;
        String relfileName = batchesmainFolder+a_folder.substring(0,a_folder.length()-1)+batch_shellScript_ext_file;
        File file = new File(relfileName);
        String batchFileNameFull = file.getAbsolutePath();
        batchFileNameFull = batchFileNameFull.replace("\\.","");
        //String path="cmd /c start C:/Users/Sultan/IdeaProjects/JavaArabic/PhraseMiner/topicalPhrases/win_run.bat";

        //String path = "cmd /c start /wait " + batchFileNameFull;
        String path =  batchFileNameFull;
        path = path.replace("/./","/");
        System.out.println("Execuiting: " + path);
        Runtime rn = Runtime.getRuntime();
        try {
            if(isBatch) {
                final Process pr = rn.exec(path);
                pr.waitFor();
            }
            else // unix
            {
                //path = "chmod u+x  "+path;
                //ProcessBuilder pb = new ProcessBuilder(
                //        "/bin/bash "+path);
                //Process p = pb.start();     // Start the process.
                //p.waitFor();
                StringBuffer output = new StringBuffer();


                final Process pr1 = rn.exec("chmod 777 "+path);
                pr1.waitFor();
                System.out.print("just finish 1 !");

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(pr1.getErrorStream()));

                String line = "";
                while ((line = reader.readLine())!= null) {
                    output.append(line + "\n");
                }

                System.out.println("Error p1: " + output.toString());


                output = new StringBuffer();

//                String command[] = {"/bin/sh", "-c",
//                        "gnome-terminal --execute "+path};
                //String command[] = {"gnome-terminal", "-x", "/bin/bash", "-c", path};                //final Process pr2 = rn.exec("/bin/bash "+path);
                String command[] = {"/bin/bash", "-c", path};                //final Process pr2 = rn.exec("/bin/bash "+path);
                final Process pr2 = rn.exec(command);
                pr2.waitFor();

                System.out.print("just finish 2 !");
                reader =
                        new BufferedReader(new InputStreamReader(pr2.getInputStream()));

                line = "";
                while ((line = reader.readLine())!= null) {
                    output.append(line + "\n");
                }

                System.out.println("Error p2: " + output.toString());



            }
            //System.out.println("exitVal = " + exitVal)
            initialize_folder(root_PhraseMinder_output_folder+a_folder);
            if(stemmCLass.equals("arabic"))
            {
                DecodeAFolder.deserializeMaps();
                initialize_folder(outputfile_folder+"decodedFiles/");
                DecodeAFolder.decoded_folder(outputfile_folder);
            }
            copyTextFromSrcToDestFolder(outputfile_folder+
                            (stemmCLass.equals("arabic")?"decodedFiles/":"")
                    ,root_PhraseMinder_output_folder+a_folder);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



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

        String a_batch_file_name = batchesmainFolder+a_folder.substring(0,a_folder.length()-1)+batch_shellScript_ext_file;
        ArrayList<String> lines_r = readFileToList("./tmp/base"+batch_shellScript_ext_file);
        ArrayList<String> lines = new ArrayList<String>();
        int count = 1;
        for(String str:lines_r)
        {
            System.out.println(count + "--" +str);
            String repstr = "";


            if(count == 6)
            {
                if(isBatch == true)
                {
                    repstr = "@set inputFile=" + fullpath_file_name;
                    System.out.println(count + "--" + repstr);
                    lines.add(repstr);
                }
                if(isBatch == false)
                {
                    repstr = "inputFile=" + fullpath_file_name;
                    System.out.println(count + "--" + repstr);
                    lines.add(repstr);

                }
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

    public static void copyTextFromSrcToDestFolder(String originalFolder,
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
                    String fileName = targetFolder + file.getName();
                    writeToFile(fileName,content);

                } catch (IOException e) {
                    e.printStackTrace();
                }
    /* do somthing with content */
            }
        }

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
}