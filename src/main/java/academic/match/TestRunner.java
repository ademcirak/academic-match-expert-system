package academic.match;

import academic.match.analyzer.Lucene;
import academic.match.loader.MySQLLoader;
import academic.match.models.Paper;
import academic.match.models.Person;
import academic.match.scraper.AmScraper;
import academic.match.scraper.Scraper;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestRunner {


    static Lucene lucene;
    static String CSV_PATH="/Users/ademcirak/Desktop/am-data.csv";
    static AtomicInteger idCount = new AtomicInteger(1);

    public static void main(String[] args) {

        try {
            Arrays.stream(new File(Config.FILE_PATH).listFiles()).forEach(File::delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            lucene = Lucene.build();
        } catch (Exception e) {
            System.out.println("Lucene initiation error: " + e.getMessage());
            e.printStackTrace();
        }


        ArrayList<Person> personList = new ArrayList<>();

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(CSV_PATH), ';');
            String[] line;
            int currentLine = 0;
            while ((line = reader.readNext()) != null) {
                if(reader.getLinesRead() == 1) // skip first line
                    continue;

                for (String l : line) {
                    System.out.print(reader.getLinesRead() + " - " + l + '|');
                }
                System.out.print('\n');

                String name = line[0];
                String surname = line[1];
                String university = line[2];
                int publishDate = Integer.parseInt(line[3]);
                String area = line[4];
                String title = line[5];
                String abstractText = line[6];
                String[] keywords = line[7].split(",");

                Paper p = new Paper();
                p.year = publishDate;
                p.abstractText = abstractText;
                p.title = title;
                p.keywords = Arrays.asList(keywords);


                Optional<Person> result = personList.stream().filter(person -> person.name.equals(name) && person.surname.equals(surname)).findAny();

                if(result.isPresent()) {
                    Person person = result.get();
                    person.papers.add(p);
                } else {
                    Person person = new Person();

                    person.name = name;
                    person.surname = surname;
                    person.schools = new ArrayList<>();
                    person.schools.add(university);
                    person.area = area;
                    person.papers = new ArrayList<>();
                    person.availability = 0.5;
                    person.acceptRate = 0.5;
                    person.accuracy = 0.5;
                    person.id = idCount.getAndIncrement();

                    person.papers.add(p);

                    personList.add(person);
                }

            }


            ArrayList<Person> testList = new ArrayList<>();


            Person removed1 = findAndRemoveRandomPerson(personList, "Elektrik Elektronik Mühendisliği");
            Person removed2 = findAndRemoveRandomPerson(personList, "Bilgisayar Mühendisliği");
            Person removed3 = findAndRemoveRandomPerson(personList, "Biyoloji");
            Person removed4 = findAndRemoveRandomPerson(personList, "Gıda Mühendisliği");

            testList.add(removed1);
            testList.add(removed2);
            testList.add(removed3);
            testList.add(removed4);

            for(Person p : personList) {
                lucene.indexPerson(p);
            }

            lucene.closeWriter();
            lucene.reopenReader();

            System.out.println("Document count: " + lucene.getDocumentCount());

            for (Person testPerson : testList) {

                List<Person> suggested = lucene.search(testPerson.papers.get(0));

                Map<String, Integer> map = new HashMap<>();

                for(Person p : suggested) {

                    Integer count = map.get(p.area);

                    if(count==null)
                        map.put(p.area, 1);
                    else
                        map.put(p.area, ++count);
                }
                System.out.println("results for: " + testPerson.getFullName() + " Major: " + testPerson.area);
                System.out.println(map);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    static Person findAndRemoveRandomPerson(ArrayList<Person> personList, String area) {

        List<Integer> indexes = IntStream.range(0, personList.size())
                .filter(i -> personList.get(i).area.equals(area))
                .mapToObj(i -> i)
                .collect(Collectors.toList());

        int rando = (int) ((Math.random() * indexes.size()));
        Integer randomInt = indexes.get(rando);
        return personList.remove(randomInt.intValue());
    }


}
