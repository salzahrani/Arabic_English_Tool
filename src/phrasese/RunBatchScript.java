package phrasese;

import java.io.IOException;

/**
 * Created by Sultan on 5/25/2016.
 */
public class RunBatchScript {
    public static void main(String[] args)
    {
        System.out.println("Now I am trying to run an script");
        String path="cmd /c start C:/Users/Sultan/IdeaProjects/JavaArabic/PhraseMiner/topicalPhrases/win_run.bat";
        Runtime rn=Runtime.getRuntime();
        try {
            Process pr=rn.exec(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
