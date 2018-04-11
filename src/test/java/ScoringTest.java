import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import academic.match.analyzer.Lucene;
import academic.match.models.Paper;
import academic.match.models.Person;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class ScoringTest {

    ArrayList<Person> getTestList() {
        ArrayList<Person> persons = new ArrayList<>();

        Person p1 = new Person();
        p1.name = "Adem";
        p1.surname = "Çırak";
        p1.id = 1;
        p1.acceptRate = 5d;
        p1.availability = 5d;
        p1.accuracy = 5d;
        p1.relatedPersons = new ArrayList<String>(Arrays.asList(new String[] { "2", "3" }));

        Paper paper1 = new Paper();
        paper1.keywords =new ArrayList<String>(Arrays.asList(new String[] { "deep learning", "word2vec", "text classification", "Text categorization", "word vector", "Turkish text classification", "ttc-3600" }));
        paper1.abstractText = "This paper investigates deep learning and word embedding classification performance over Turkish texts using an existing dataset called TTC-3600. TTC-3600 is gathered from Turkish news and articles for researches on Turkish text classification [TTC]. Our work achieved a higher classification accuracy than original study. We used raw text data and steamed versions for calculation. Some pre-processing methods also applied to increase accuracy. ";
        paper1.title = "A Study of Text Classification with Embedding for Turkish Text Categorization";
        p1.papers = new ArrayList<>();
        p1.papers.add(paper1);
        p1.schools = new ArrayList<String>(Arrays.asList(new String[] { "Mersin University" }));
        persons.add(p1);


        Person p2 = new Person();
        p2.name = "Çiğem AAA = 528";
        p2.surname = "Acı";
        p2.id = 2;
        p2.acceptRate = 5d;
        p2.availability = 2d;
        p2.accuracy = 8d;
        p2.relatedPersons = new ArrayList<String>(Arrays.asList(new String[] { "1", "3" }));

        Paper paper2 = new Paper();
        paper2.keywords =new ArrayList<String>(Arrays.asList(new String[] { "Broadcast-based interconnection networks",
                "Congestion control",
                "Multiple input queue",
                "Simulation" }));
        paper2.abstractText = "The main purpose of this paper is to propose a hybrid congestion control algorithm to prevent congestion in 2-D broadcast-based multiprocessor architectures with multiple input queues. Our algorithm utilizes a node's both input queue and output channel parameters to detect and prevent congestion. The intermediate node selection procedure and the bypass operation have also been developed as part of the proposed algorithm. The performance of the algorithm is tested with several synthetic traffic patterns on the 2-D simultaneous optical multiprocessor exchange bus. The performance of the algorithm is compared with that of the algorithms which use only input and only output parameters and it is shown that the proposed congestion control algorithm using hybrid parameters performs better than the other algorithms. The proposed algorithm is able to decrease the average network response time by 33.63 %, average input waiting time by 29.13 % and increase average processor utilization by 7.57 % on the average.";
        paper2.title = "A hybrid congestion control algorithm for broadcast-based architectures with multiple input queues";

        p2.papers = new ArrayList<>();
        p2.papers.add(paper1);
        p2.papers.add(paper2);

        p2.schools = new ArrayList<String>(Arrays.asList(new String[] { "Mersin University", "Cukurova University" }));
        persons.add(p2);


        Person p3 = new Person();
        p3.name = "Çiğem AAA = 575";
        p3.surname = "Acı";
        p3.id = 3;
        p3.acceptRate = 5d;
        p3.availability = 7d;
        p3.accuracy = 5d;
        p3.relatedPersons = new ArrayList<String>(Arrays.asList(new String[] { "1", "2" }));

        p3.papers = new ArrayList<>();
        p3.papers.add(paper1);
        p3.papers.add(paper2);

        p3.schools = new ArrayList<String>(Arrays.asList(new String[] { "Mersin University", "Cukurova University" }));
        persons.add(p3);


        Person p4 = new Person();
        p4.name = "Mehmet AAA = 101010";
        p4.surname = "Acı";
        p4.id = 4;
        p4.acceptRate = 10d;
        p4.availability = 10d;
        p4.accuracy = 10d;
        p4.relatedPersons = new ArrayList<String>(Arrays.asList(new String[] { "2", "3" }));

        Paper paper3 = new Paper();
        paper3.keywords =new ArrayList<String>(Arrays.asList(new String[] { "Bayesian", "Classification", "Expectation maximization algorithm", "Hybrid method", "K-nearest neighbor method" }));
        paper3.abstractText = "K nearest neighbor and Bayesian methods are effective methods of machine learning. Expectation maximization is an effective Bayesian classifier. In this work a data elimination approach is proposed to improve data clustering. The proposed method is based on hybridization of k nearest neighbor and expectation maximization algorithms. The k nearest neighbor algorithm is considered as the preprocessor for expectation maximization algorithm to reduce the amount of training data making it difficult to learn. The suggested method is tested on well-known machine learning data sets iris, wine, breast cancer, glass and yeast. Simulations are done in MATLAB environment and performance results are concluded.";
        paper3.title = "K nearest neighbor reinforced expectation maximization method";


        p4.papers = new ArrayList<>();
        p4.papers.add(paper3);

        p4.schools = new ArrayList<String>(Arrays.asList(new String[] { "Mersin University", "Cukurova University" }));
        persons.add(p3);


        return persons;
    }


    Paper testPaper() {
        Paper paper = new Paper();
        paper.title = "Research on Chinese text classification based on Word2vec";
        paper.abstractText = "The set of features which the traditional feature selection algorithm of chi-square selected is not complete. This causes the low performance for the final text classification. Therefore, this paper proposes a method. The method utilizes word2vec to generate word vector to improve feature selection algorithm of the chi square. The algorithm applies the word vector generated by word2vec to the process of the traditional feature selection and uses these words to supplement the set of features as appropriate. Ultimately, the set of features obtained by this method has better discriminatory power. Because, the feature words with the better discriminatory power has the strong ability of distinguishing categories as its semantically similar words. On this base, multiple experiments have been carried out in this paper. The experimental results show that the performance of text classification can increase after extension of feature words.";
        paper.keywords = new ArrayList<String>(Arrays.asList(new String[] { "cbow", "chi square", "Chinese text classification", "word2vec", "text classification", "Text categorization", "word vector" }));
        paper.owner = 4;
        return paper;
    }

    @Test
    void myFirstTest() throws Exception {


        Lucene instance = Lucene.tempBuild();


        ArrayList<Person> persons = this.getTestList();
        Paper testPaper = this.testPaper();

        for(Person p : persons) {
            instance.indexPerson(p);
        }

        List<Person> results = instance.search(testPaper);

        assertFalse(results.isEmpty());
    }

}