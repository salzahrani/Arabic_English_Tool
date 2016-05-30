package main;

/**
 * Created by Sultan on 5/28/2016.
 */
import MalletP.*;
public class Root
    {
        public static void main(String[] args)
        {
            // Phasae1: stopword removel stemming/nonstemming



            //LDA configuration...

            String stopfileName = "./Stoplists/english_stoplist.txt";
            //String stopfileName = "./Stoplists/turkesh_stoplist.txt";
            //String stopfileName = "./Stoplists/arabic_stoplist.txt";
            String folderName = "./corpora/filtered/FamilyWomenRisingKids/";
            //String folderName = "./corpora/filtered/ReligionFatwa/";
            int numberOfIteration = 500;
            int[] sizes = {1,2,3,4};
            int numTopics = 50;
            int maxRnak = 40;

            MalletP.Program m_p = new MalletP.Program();

            m_p.run(folderName,stopfileName,numberOfIteration,sizes,numTopics,maxRnak);


        }
}
