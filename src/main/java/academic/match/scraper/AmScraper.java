package academic.match.scraper;

import academic.match.models.Paper;
import academic.match.models.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AmScraper implements Scraper  {

    // https://www.journaldev.com/2324/jackson-json-java-parser-api-example-tutorial
    // https://dzone.com/articles/how-to-parse-json-data-from-a-rest-api-using-simpl

    // https://www.mendeley.com/research-papers/api/search/?query=%C3%A7i%C4%9Fdem%20inan%20ac%C4%B1&page=1
    @Override
    public List<Paper> getPapers(Person person) {

        ArrayList<Paper> papers = new ArrayList<>();

        Paper paper1 = new Paper();
        paper1.keywords =new ArrayList<String>(Arrays.asList(new String[] { "deep learning", "word2vec", "text classification", "Text categorization", "word vector", "Turkish text classification", "ttc-3600" }));
        paper1.abstractText = "This paper investigates deep learning and word embedding classification performance over Turkish texts using an existing dataset called TTC-3600. TTC-3600 is gathered from Turkish news and articles for researches on Turkish text classification [TTC]. Our work achieved a higher classification accuracy than original study. We used raw text data and steamed versions for calculation. Some pre-processing methods also applied to increase accuracy. ";
        paper1.title = "A Study of Text Classification with Embedding for Turkish Text Categorization";


        papers.add(paper1);
        return papers;
    }

    @Override
    public Person getPerson(String name) {
        return null;
    }

    public static void main(String[] args) {

        Scraper myScrapper = new AmScraper();

        myScrapper.getPerson("Çiğdem İnan Acı");

    }
}
