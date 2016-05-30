package MalletP;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Program {

    public static final String LDA_Folder = "./LDA_Output/" ;
    public static String main_folder = "./";
    public static String corpora_folder = main_folder + "corpora/";
    public static String original_folder = corpora_folder + "English_Corpus/";//"original/" ;// "original/";"movie_reviews/" /// here where you change

    public static String[] folderNames;

    public static void main(String []args)
	{
		String stopfileName = "./Stoplists/english_stoplist.txt";
		//String stopfileName = "./Stoplists/turkesh_stoplist.txt";
		//String stopfileName = "./Stoplists/arabic_stoplist.txt";

        main_folder = "./";
        corpora_folder = main_folder + "corpora/";
        original_folder = corpora_folder + "ph3/";//"original/" ;// "original/";"movie_reviews/" /// here where you change

        int numberOfIteration = 100;
		int[] sizes = {1,2,3,4};
		int numTopics = 50;
		int maxRnak = 40;

		run(original_folder,stopfileName,numberOfIteration,sizes,numTopics,maxRnak);
	}

    public static void run(String org_folder,String stoplistfileName,
                               int numberOfIteration, int[] sizes,
                               int numTopics, int maxRnak )
    {

        original_folder = org_folder;
        folderNames = getFoldersNames();

        try {
            FileUtils.deleteDirectory(new File(LDA_Folder));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initialize_folder(LDA_Folder);
        for(String str:folderNames)
        {
            String a_folder_name = LDA_Folder + str;
            initialize_folder(a_folder_name);
        }
        for(String a_folder:folderNames)
        {
            doTopicModel(a_folder,stoplistfileName,numberOfIteration,sizes,numTopics,maxRnak);
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
    public static void run_old(String folderNameAbs,String stoplistfileName,
						   int numberOfIteration, int[] sizes,
						   int numTopics, int maxRnak )
	{



		doTopicModel(folderNameAbs,stoplistfileName,numberOfIteration,sizes,numTopics,maxRnak);
	}

	public static void doTopicModel(String folder_name,String stoplistfileName,
									int numberOfIteration, int[] sizes,
									int numTopics, int maxRnak)
	{
		DoTopicModel cs4 = new DoTopicModel(original_folder + folder_name,stoplistfileName,numberOfIteration,sizes,numTopics,maxRnak);
		ArrayList<String> topic_list = cs4.run();

		System.out.println("Starting printing topics:");
		for(String str:topic_list)
		{
			System.out.println(str);
		}
		BufferedWriter output = null;
		try {
			File file = new File(LDA_Folder+folder_name+"LDA_output.txt");
                    // "./LDA/output.txt");
			output = new BufferedWriter(new FileWriter(file));
			for(String str:topic_list)
			{
				output.write(str+"\n");
			}
			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
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
}

