package MalletP;

public class Program {

	public static void main(String []args)
	{
		//String stopfileName = "./Stoplists/english_stoplist.txt";
		//String stopfileName = "./Stoplists/turkesh_stoplist.txt";
		String stopfileName = "./Stoplists/arabic_stoplist.txt";
		String folderNameAbs = "./corpora/filtered/FamilyWomenRisingKids/";
		//String folderNameAbs = "./corpora/filtered/ReligionFatwa/";


		doTopicModel(folderNameAbs,stopfileName);
	}
	
	public static void doTopicModel(String folderNameAbs,String stoplistfileName)
	{
		DoTopicModel cs4 = new DoTopicModel(folderNameAbs,stoplistfileName);
		cs4.run();
	}
}
