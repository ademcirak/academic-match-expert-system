package academic.match.scraper;

import academic.match.models.Paper;
import academic.match.models.Person;

import java.io.IOException;
import java.util.List;

public interface Scraper {

    public List<Paper> getPapers(Person person) throws IOException;
   /* public Person getPerson(Integer id);
    public Person getPerson(String mendeleyId);
    public Person getPerson(String title);

    public List<Person> getPerson(Person schools);
    public List<Person> getPerson(Person relatedPersons);
    public List<Person> getPerson(Person keywords);
    public List<Person> getPerson(Person papers);*/

}