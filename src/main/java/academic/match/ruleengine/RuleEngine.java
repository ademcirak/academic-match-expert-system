package academic.match.ruleengine;

import academic.match.models.Person;

import java.util.List;

public interface RuleEngine {

    public List<Person> filter(List<Person> personList);
}
