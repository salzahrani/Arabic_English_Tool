package phraseMiner;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Sultan on 5/25/2016.
 */
public class MapsObject  implements Serializable {

    public MapsObject(Map word_to_code,Map code_to_word)
    {
        this.word_to_code = word_to_code;
        this.code_to_word = code_to_word;

    }

    public Map word_to_code;
    public Map code_to_word;

    public Map getWord_to_code() {
        return word_to_code;
    }

    public Map getCode_to_word() {
        return code_to_word;
    }
}
