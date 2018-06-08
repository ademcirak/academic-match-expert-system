package academic.match.loader;

import academic.match.models.Person;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MySQLLoader {

    Connection conn = null;
    AtomicInteger idCount = new AtomicInteger(1);

    public MySQLLoader() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:mysql://192.168.64.2/", "root", "1234");
    }


    public ArrayList<Person> getNames() throws SQLException {

        Statement stmt = conn.createStatement();
        ArrayList<Person> names = new ArrayList<>();

        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM CASCONet.Person order by person_id limit 100, 100");

            while (rs.next()) {
                String fullName = rs.getString(2) + " " + rs.getString(3);
                Person p = new Person();
                p.availability = 0.5;
                p.acceptRate = 0.5;
                p.accuracy = 0.5;
                p.id = idCount.getAndIncrement();
                p.name = rs.getString(2);
                p.surname = rs.getString(3);
                names.add(p);
            }
        } finally {
            stmt.close();
        }

        return names;
    }

}
