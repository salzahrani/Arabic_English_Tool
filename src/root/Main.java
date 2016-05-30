package root;

/**
 * Created by Sultan on 5/28/2016.
 */

public class Main
    {
        public static void main(String[] args)
        {
            // Part1: stopword removel stemming/nonstemming

            /*
            preprocess.Program  prep_p = new preprocess.Program();

            String stemmerName = "english";
            //stemmerName = "arabic";

            String org_folder = "English_Corpus/";
            //org_folder = "Arabic_Corpus/";

            boolean removeStopWord = true;
            boolean stemwords = false;

            prep_p.run(org_folder,stemmerName, removeStopWord,stemwords);



            */

            //LDA configuration...

            /*
            String stopfileName = "";
            if(stemmerName.equals("english"))
                stopfileName = "./Stoplists/english_stoplist.txt";
            else
                stopfileName = "./Stoplists/arabic_stoplist.txt";
            int numberOfIteration = 500;
            int[] sizes = {1,2,3,4};
            int numTopics = 50;
            int maxRnak = 40;

            String main_folder = "./";
            String corpora_folder = main_folder + "corpora/";
            String original_folder = corpora_folder + "ph3/";//"original/" ;// "original/";"movie_reviews/" /// here where you change


            malletLDA.Program m_p = new malletLDA.Program();
            m_p.run(original_folder,stopfileName,numberOfIteration,sizes,numTopics,maxRnak);
            */

            // PhraseMiner:

            phraseMiner.Program p_phm = new phraseMiner.Program();
            String main_folder = "./";
            String corpora_folder = main_folder + "corpora/";
            String original_folder = corpora_folder + "ph3/";//"original/" ;// "original/";"movie_reviews/" /// here where you change

            p_phm.run(original_folder);
            p_phm.run("");

        }
}
