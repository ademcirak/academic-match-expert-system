package academic.match.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

public class Person implements Serializable {
    public Integer id;
    public String mendeleyId;
    public String name;
    public String surname;
    public String title;
    @Min(0)
    @Max(10)
    public Double availability;
    @Min(0)
    @Max(10)
    public Double accuracy;
    @Min(0)
    @Max(10)
    public Double acceptRate;
    public List<String> schools;
    public List<String> relatedPersons;
    public List<String> keywords;
    public List<Paper> papers;
}
