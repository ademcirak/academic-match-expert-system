package academic.match.scraper;

import academic.match.models.Paper;
import academic.match.models.Person;

import java.util.List;

public interface Scraper {

    public List<Paper> getPapers(Person person);
    public Person getPerson(String name);
}
