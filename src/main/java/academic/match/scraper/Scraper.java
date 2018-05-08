package academic.match.scraper;

import academic.match.models.Paper;
import academic.match.models.Person;

import java.io.IOException;
import java.util.ArrayList;

public interface Scraper {

    public ArrayList<Paper> getPapers(Person person) throws IOException;
    public ArrayList<Person> getPerson(String name) throws IOException;
   /* public Person getPerson(Integer id);
    public Person getPerson(String mendeleyId);
    public Person getPerson(String title);

    public List<Person> getPerson(Person schools);
    public List<Person> getPerson(Person relatedPersons);
    public List<Person> getPerson(Person keywords);
    public List<Person> getPerson(Person papers);*/

}