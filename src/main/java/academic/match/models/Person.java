package academic.match.models;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable {
    public Integer id;
    public String mendeleyId;
    public String name;
    public String surname;
    public String title;
    public List<String> schools;
    public List<String> relatedPersons;
    public List<String> keywords;
    public List<Paper> papers;
}
