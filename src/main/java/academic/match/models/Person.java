package academic.match.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

public class Person implements Serializable {
    public Integer id;
    public String mendeleyId;
    public String scopusAuthorId = "";
    public String name;
    public String surname;
    public String title;
    public float score;
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
    public String area;


    public String getFullName() {
        return this.name + " " + this.surname;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + (id==null ? "" : id) +
                ", mendeleyId='" + (mendeleyId==null ? "" : mendeleyId) + '\'' +
                ", name='" + (name==null ? "" : name) + '\'' +
                ", surname='" + (surname==null ? "" : surname) + '\'' +
                ", title='" + (title==null ? "" : title) + '\'' +
                ", score=" + score +
                ", availability=" + (availability==null ? "" : availability) +
                ", accuracy=" + (accuracy==null ? "" : accuracy) +
                ", acceptRate=" + (acceptRate==null ? "" : acceptRate) +
                ", schools=" + (schools==null ? "" : schools) +
                ", relatedPersons=" + (relatedPersons==null ? "" : relatedPersons) +
                ", keywords=" + (keywords==null ? "" : keywords) +
                ", paperCount=" + (papers==null ? "0" : papers.size()) +
                '}';
    }
}
