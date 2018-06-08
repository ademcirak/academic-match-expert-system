package academic.match.scraper;

import academic.match.models.Paper;
import academic.match.models.Person;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONArray;
import org.json.JSONObject;


public class AmScraper implements Scraper  {

    // https://www.journaldev.com/2324/jackson-json-java-parser-api-example-tutorial
    // https://dzone.com/articles/how-to-parse-json-data-from-a-rest-api-using-simpl

    // https://www.mendeley.com/research-papers/api/search/?query=%C3%A7i%C4%9Fdem%20inan%20ac%C4%B1&page=1
    OkHttpClient client = null;
    public AmScraper() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }


    String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    ArrayList<JSONObject> getAll(Person person) throws IOException {
        ArrayList<JSONObject> objects = new ArrayList<>();

        Integer page = 1;

        String url= "https://www.mendeley.com/research-papers/api/search/?query=" + URLEncoder.encode(person.getFullName(), "UTF-8") + "&page=1&limit=100";


        JSONObject responseJson = new JSONObject(this.get(url));

        try {
            JSONArray results = responseJson.getJSONArray("items");
            if(results.length() > 0) {
                for(int i = 0; i < results.length(); i++) {
                    objects.add(results.getJSONObject(i));
                }
            }
            // try to find next pages
            // TODO FUAT fetch next page too
            try {
                String nextPage = responseJson.getJSONObject("pageData").getJSONObject("link").getString("next");
            } catch (Exception e) {
                // means we dont have next page
                // e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return objects;
    }

    private Paper convertFromJson(JSONObject obj, Person person) {
        Paper p = new Paper();
        p.keywords = new ArrayList<>();
        p.abstractText = obj.getString("abstract");
        p.title = obj.getString("title");
        try {
            JSONArray keywordArray = obj.getJSONArray("keywords");
            for(int i = 0; i < keywordArray.length(); i++) {
                p.keywords.add(keywordArray.getString(i));
            }
        } catch (Exception e) {
            System.out.println("No keywords found for paper with title: " + p.title);
        }

        try {
            boolean isInArray = false;
            JSONArray authors = obj.getJSONArray("authors");
            for(int i = 0; i < authors.length(); i++) {
                JSONObject author = authors.getJSONObject(i);

                String firstName = author.getString("first_name");
                String lastName = author.getString("last_name");

                if(firstName.equals(person.name) && lastName.equals(person.surname)) {
                    isInArray = true;
                    try {
                        person.scopusAuthorId = author.getString("scopus_author_id");
                    } catch (Exception e) {
                        System.out.println("Empty scopus_author_id: " + p.title);
                    }
                    // if found stop loop
                    break;
                }
            }

            if(!isInArray)
                return null;

        } catch (Exception e) {
            return null;
        }


        return p;
    }

    @Override
    public ArrayList<Paper> getPapers(Person person) throws IOException {

        ArrayList<Paper> papers = new ArrayList<>();

        ArrayList<JSONObject> notParsedArray = this.getAll(person);

        // TODO FUAT filter by scopus_author_id

        for (JSONObject obj : notParsedArray) {
            try {
                Paper paper = this.convertFromJson(obj, person);

                // skip null
                if(paper == null)
                    continue;

                paper.owner = person.id;
                papers.add(paper);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        // TODO FUAT uncomment
        System.out.println("Found count: " + papers.size());
        System.out.println(papers);
        return papers;
    }

    // TODO FUAT just do it
    @Override
    public ArrayList<Person> getPerson(String name) throws IOException {
        return null;
    }

    public static void main(String[] args) {

        Scraper myScrapper = new AmScraper();

        Person p2 = new Person();
        p2.name = "Çiğdem İnan";
        p2.surname = "Acı";
        p2.id = 2;
        p2.acceptRate = 5d;
        p2.availability = 2d;
        p2.accuracy = 8d;

        try {
            ArrayList<Paper> list = myScrapper.getPapers(p2);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
