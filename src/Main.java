import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class Main {

    static String username = "root";
    static String password = "wrong_pass";
    static String connectionUrl = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useSSL=false";

    public static void main(String[] args) throws SQLException {

        ResultSet resultSet = null;
        try {
            resultSet = getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (resultSet.next()){
            System.out.println(resultSet.getInt("id"));
            System.out.println(resultSet.getString("name"));
        }
        System.out.println("____________");

        // CachedRowSet = ResultSet, he has all the same methods,
        // which means that we can also change the data in it

        CachedRowSet cachedRowSet = (CachedRowSet) resultSet;

        // This data (3 lines) is needed to ensure CachedRowSet that
        // it can be updated, in case of a change in the database

        cachedRowSet.setUrl(connectionUrl);
        cachedRowSet.setUsername(username);
        cachedRowSet.setPassword(password);

        cachedRowSet.setCommand("SELECT * FROM books WHERE id = ?");
        cachedRowSet.setInt(1,1);
        cachedRowSet.setPageSize(20);
        cachedRowSet.execute();

        // For example, we can change the local version of our database,
        // and then when we need to display all these changes from the
        // local database to the real one, we just call this method
        // cachedRowSet.acceptChanges();

        do {
            while (cachedRowSet.next()){
                System.out.println(cachedRowSet.getInt("id"));
                System.out.println(cachedRowSet.getString("name"));
            }
        } while (cachedRowSet.nextPage());
    }

    static ResultSet getData() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password);
             Statement stat = conn.createStatement()) {
            stat.execute("DROP TABLE IF EXISTS books");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, PRIMARY KEY (id))");
            stat.executeUpdate("INSERT INTO books (name) VALUES ('Inferno')", Statement.RETURN_GENERATED_KEYS);
            stat.executeUpdate("INSERT INTO books (name) VALUES ('DaVinci code')", Statement.RETURN_GENERATED_KEYS);
            stat.executeUpdate("INSERT INTO books (name) VALUES ('Solomon key')", Statement.RETURN_GENERATED_KEYS);

            // CachedRowSet cachedRowSet = new CachedRowSetImpl();
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet cachedRowSet = factory.createCachedRowSet();

            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            cachedRowSet.populate(resultSet);
        return cachedRowSet;
        }
    }
}
