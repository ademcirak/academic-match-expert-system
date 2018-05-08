package academic.match.models;

import java.io.Serializable;
import java.util.List;

public class Paper implements Serializable {

    public String title;
    public String abstractText;
    public List<String> keywords;
    public Integer owner;

    @Override
    public String toString() {
        return "Paper{" +
                "title='" + title + '\'' +
                ", abstractText='" + abstractText.substring(0, 20) + '\'' +
                ", keywords=" + keywords +
                ", owner=" + owner +
                '}';
    }
}
