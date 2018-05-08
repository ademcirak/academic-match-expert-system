package academic.match.ruleengine;

import academic.match.models.Person;

import java.util.ArrayList;
import java.util.List;

public class REngine implements RuleEngine {

    private static float SCORE_THRESHOLD = 0.2f;

    public ArrayList<Person> filter(ArrayList<Person> personList) {
        ArrayList<Person> filteredList = new ArrayList<>();

        for (Person person : personList) {

            // remove from list if any of listed is 0
            if (person.availability <= 0
                || person.accuracy <= 0
                || person.acceptRate <= 0
                )
                continue;

            // similarity threshold
            if(person.score <= SCORE_THRESHOLD)
                continue;

            // TODO BİRKAN add other rules to here


            // if all rules passed add to filtered list
            filteredList.add(person);
        }
        return filteredList;
    }
}
