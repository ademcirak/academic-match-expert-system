package academic.match.ruleengine;

import academic.match.models.Person;

public class REngine implements RuleEngine {

    public List<Person> filter(List<Person> personList)
    {
        List<Person> persons = new List<>();
        for(i=0;i<personList.size();i++)
        {
            Person person1 = new Person();
            person1=personList(i);
        if(person1.availability<= && person1.accuracy<= && person1.acceptRate<= ) //Belirlenecek değerlere göre filtreleme
            persons.add(person1);
        }
        return persons;
    }
}
