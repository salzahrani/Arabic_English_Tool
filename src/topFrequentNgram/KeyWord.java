package topFrequentNgram;

/**
 * Created by Sultan on 5/31/2016.
 */
public class KeyWord {

    public String keyword = null;
    public int ngram = -1;
    public int count = -1;



    public void setCount(int count) {
        this.count = count;
    }

    public void setKeyword(String keyword) {
        this.keyword = new String(keyword);
    }

    public void setNgram(int ngram) {
        this.ngram = ngram;
    }



    public String getKeyword() {
        return keyword;
    }

    public int getNgram() {
        return ngram;
    }

    public int getCount()
    {
        return count;
    }

    public void incrementCOunt()
    {
        this.count++;
    }
    @Override
    public int hashCode()
    {
        return keyword.hashCode();
    }
    public String toString()
    {
        return getKeyword()+", "+getCount();
    }



}
