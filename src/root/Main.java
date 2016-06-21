package root;

/**
 * Created by Sultan on 5/28/2016.
 */

public class Main
    {
        public static void main(String[] args)
        {
            // Part1: stopword removel stemming/nonstemming


            preprocess.Program  prep_p = new preprocess.Program();

            //String stemmerName = "english";
            String stemmerName = "arabic";

            String org_folder = "English_Corpus/";
            //String org_folder = "Arabic_Corpus/";

            String main_folder = "./";
            String corpora_folder = main_folder + "corpora/";
            String original_folder = corpora_folder + "ph3/";//"original/" ;// "original/";"movie_reviews/" /// here where you change


            boolean removeStopWord = true;
            boolean stemwords = false;

            prep_p.run(org_folder,stemmerName, removeStopWord,stemwords);

            // top frequent words unigram bigram trigram and so on:
            int[] ngram_option = {1, 2, 3, 4,5};
            topFrequentNgram.Program p_freq = new topFrequentNgram.Program();

            p_freq.run(original_folder, ngram_option);


            // PhraseMiner:

            phraseMiner.Program p_phm = new phraseMiner.Program();

            p_phm.isBatch = false; // running shell in unix
            p_phm.run(original_folder,stemmerName);



            //LDA configuration...
            /*
            String stopfileName = "";
            if(stemmerName.equals("english"))
                stopfileName = "./Stoplists/english_stoplist.txt";
            else
                stopfileName = "./Stoplists/arabic_stoplist.txt";
            int numberOfIteration = 1000;
            int[] sizes = {1,2,3,4};
            int numTopics = 20;
            int maxRnak = 80;



            malletLDA.Program m_p = new malletLDA.Program();
            m_p.run(original_folder,stopfileName,numberOfIteration,sizes,numTopics,maxRnak);

            */


        }
}
