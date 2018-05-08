package academic.match.ruleengine;

import academic.match.models.Person;

import java.util.ArrayList;
import java.util.List;

public interface RuleEngine {

    public ArrayList<Person> filter(ArrayList<Person> personList);
}
