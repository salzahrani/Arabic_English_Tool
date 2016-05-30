package MalletP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Program {

	public static void main(String []args)
	{
		String stopfileName = "./Stoplists/english_stoplist.txt";
		//String stopfileName = "./Stoplists/turkesh_stoplist.txt";
		//String stopfileName = "./Stoplists/arabic_stoplist.txt";
		String folderName = "./corpora/filtered/FamilyWomenRisingKids/";
		//String folderName = "./corpora/filtered/ReligionFatwa/";
		int numberOfIteration = 500;
		int[] sizes = {1,2,3,4};
		int numTopics = 50;
		int maxRnak = 40;

		run(folderName,stopfileName,numberOfIteration,sizes,numTopics,maxRnak);
	}

	public static void run(String folderNameAbs,String stoplistfileName,
						   int numberOfIteration, int[] sizes,
						   int numTopics, int maxRnak )
	{



		doTopicModel(folderNameAbs,stoplistfileName,numberOfIteration,sizes,numTopics,maxRnak);
	}

	public static void doTopicModel(String folderNameAbs,String stoplistfileName,
									int numberOfIteration, int[] sizes,
									int numTopics, int maxRnak)
	{
		DoTopicModel cs4 = new DoTopicModel(folderNameAbs,stoplistfileName,numberOfIteration,sizes,numTopics,maxRnak);
		ArrayList<String> topic_list = cs4.run();

		System.out.println("Starting printing topics:");
		for(String str:topic_list)
		{
			System.out.println(str);
		}
		BufferedWriter output = null;
		try {
			File file = new File("./LDA/output.txt");
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

}

