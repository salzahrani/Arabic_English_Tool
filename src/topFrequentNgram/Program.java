package topFrequentNgram;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

/**
 * Created by Sultan on 5/31/2016.
 */
public class Program {
    public static String main_folder = "./";
    public static String corpora_folder = main_folder + "corpora/";
    public static String original_folder = corpora_folder + "English_Corpus/" + "ph3";//"original/" ;// "original/";"movie_reviews/" /// here where you change

    public static String[] folderNames;
    public static String tmp_outputfile_folder = "./topFrequentNgram/";
    public static String outputfile_folder = "./topFrequentNgram_Out/";


    public static Map<String, KeyWord> k_ngrams_map = new HashMap<String, KeyWord>();

    public static void main(String args[]) {


        int[] ngram_option = {1, 2, 3, 4};
        String original_folder =   "./" + "corpora/" + "ph3/";
        run(original_folder, ngram_option);


    }

    public static void run(String org_folder, int[] ngram_option) {
        original_folder = org_folder;

        //
        folderNames = getFoldersNames();
        try {
            FileUtils.deleteDirectory(new File(outputfile_folder));
            FileUtils.deleteDirectory(new File(tmp_outputfile_folder));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initialize_folder(outputfile_folder);
        initialize_folder(tmp_outputfile_folder);

        for (String a_f : folderNames)

        {
            String a_folder_name = outputfile_folder + a_f;
            initialize_folder(a_folder_name);
        }

        for (String a_folder : folderNames) {

            perFolder_Extract_ngtam(a_folder, ngram_option);
        }

        //

    }

    public static void perFolder_Extract_ngtam(String a_folder, int[] ngram_option)
    {
        //

        System.out.println("Generate ngrams  for " +a_folder );

        String file_name = tmp_outputfile_folder+
                a_folder.substring(0,a_folder.length()-1)+
                ".txt";
        //
        Generate_one_file.run(original_folder+a_folder,file_name);
        ArrayList<String> lst = readFileToList(file_name);
        initialize_folder(outputfile_folder + a_folder );
        for (int i = 0; i < ngram_option.length; i++) {
            int n = ngram_option[i];
            k_ngrams_map = new HashMap<String, KeyWord>();
            dongram_for_all_curpos( n, lst);
            // now the k_ngrams_map filled with all keywords ...
            // convert the value of map to list and sort and print to file.
            ArrayList<KeyWord> k_ngrams_lst = new ArrayList<KeyWord>(k_ngrams_map.values());
            do_sort(k_ngrams_lst);

            print_to_file(a_folder, k_ngrams_lst, n);
        }
    }

    public static void print_to_file(String a_folder,ArrayList<KeyWord> lst, int n) {
        String fileName =  outputfile_folder + a_folder + n + ".txt";
        try {
            // create new file
            File file = new File(fileName);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (KeyWord k : lst) {
                if (k.getCount() == 1) // when it hits one.... break
                    break;
                bw.write(k.toString() + "\n");
            }

            // close connection
            bw.close();
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public static void dongram_for_all_curpos(int n , ArrayList<String> lines) {
        for (String aline : lines) {
            String[] line_ngram = ngrams(aline, n);
            if (line_ngram == null) continue;
            for (String str : line_ngram) {
                KeyWord temp_keyword = null;

                if (k_ngrams_map.containsKey(str)) {
                    //get the element
                    temp_keyword = k_ngrams_map.get(str);
                    temp_keyword.incrementCOunt();
                } else {
                    temp_keyword = new KeyWord();
                    temp_keyword.setKeyword(str);
                    temp_keyword.setNgram(n);
                    temp_keyword.setCount(1);
                    k_ngrams_map.put(str, temp_keyword);

                }
            }

        }
    }

    public static String[] ngrams(String s, int len) {
        String[] parts = s.split("\\s+");
        if (parts.length - len < 0)
            return null;
        String[] result = new String[parts.length - len + 1];
        for (int i = 0; i < parts.length - len + 1; i++) {
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < len; k++) {
                if (k > 0) sb.append(' ');
                sb.append(parts[i + k]);
            }
            result[i] = sb.toString();
        }
        return result;
    }

    public static ArrayList<String> readFileToList(String fileName) {
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
        return lst;
    }

    public static void do_sort(ArrayList<KeyWord> lst) {
        Collections.sort(lst, new Comparator<KeyWord>() {
            public int compare(KeyWord o1, KeyWord o2) {
                if (o1.getCount() == o2.getCount())
                    return 0;
                return o1.getCount() > o2.getCount() ? -1 : 1;
            }
        });
    }

    public static String[] getFoldersNames() {
        String[] arr_of_fodler = null;
        ArrayList<String> list_of_folders = new ArrayList<String>();

        File folder = new File(original_folder);
        File[] listOfFiles = folder.listFiles();


        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isDirectory()) {
                String fileName = file.getName() + "/";
                list_of_folders.add(fileName);
            }
        }

        Collections.sort(list_of_folders);
        arr_of_fodler = new String[list_of_folders.size()];
        arr_of_fodler = (String[]) list_of_folders.toArray(arr_of_fodler);
        return arr_of_fodler;
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
