package academic.match.scraper;

import academic.match.models.Paper;
import academic.match.models.Person;

import java.util.List;

public interface Scraper {

    public List<Paper> getPapers(Person person);
    public Person getPerson(String name);

    public Person getPerson1(String surname);
   /* public Person getPerson(Integer id);
    public Person getPerson(String mendeleyId);
    public Person getPerson(String title);

    public List<Person> getPerson(Person schools);
    public List<Person> getPerson(Person relatedPersons);
    public List<Person> getPerson(Person keywords);
    public List<Person> getPerson(Person papers);*/

}