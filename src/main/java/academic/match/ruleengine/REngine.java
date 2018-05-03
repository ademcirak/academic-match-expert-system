package academic.match.ruleengine;

import academic.match.models.Person;

import java.util.ArrayList;
import java.util.List;

public class REngine implements RuleEngine {

    public List<Person> filter(List<Person> personList)
    {
        List<Person> persons = new ArrayList<>();
        for(int i=0;i<personList.size();i++)
        {
            Person person1 = new Person();
            // person1= personList(i);
        if(person1.availability<=0 && person1.accuracy<=0 && person1.acceptRate<=0 ) //Belirlenecek değerlere göre filtreleme
            persons.add(person1);
        }
        return persons;
    }
}
