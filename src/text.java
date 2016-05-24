import java.io.*;

/**
 * Created by Sultan on 5/23/2016.
 */
public class text {
    public static void main(String[] args)
    {
        System.out.println("Hello world!");
        String file1 = "./test/f1.txt";
        String file2 = "./test/f2.txt";
        copyfiles_arabic(file1,file2);
    }

    public static void copyfiles_arabic(String file1,String file2)
    {
        String content = readFile(file1);
        System.out.println(content);
        String[] words = null;

        content = content.replaceAll("\\p{N}+"," ");
        content = content.replaceAll("\\w+"," ");
        content = content.replace("\\d+"," ");
        words = content.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for(String s : words) {
            builder.append(s);
            builder.append(" ");

        }
        content =  builder.toString();

        writeFile(file2, content);





    }

    public static String readFile(String fileName)
    {
        String everything = "";
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = null;

            line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
             everything = sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return everything;
    }

    public static void writeFile(String fileName, String content)
    {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName)));
            writer.write(content);
        } catch (IOException ex) {
            // report
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }

    }
}
