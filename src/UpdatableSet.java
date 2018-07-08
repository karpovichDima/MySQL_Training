import java.sql.*;

public class UpdatableSet {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String username = "root";
        String password = "wrong_pass";
        String connectionUrl = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useSSL=false";
        Class.forName("com.mysql.cj.jdbc.Driver");

        // change the data in the table "on the fly"
        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password);
             Statement stat = conn.createStatement()) {
            stat.execute("DROP TABLE IF EXISTS books");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, PRIMARY KEY (id))");
            stat.executeUpdate("INSERT INTO books (name) VALUES ('Inferno')");
            stat.executeUpdate("INSERT INTO books (name) VALUES ('DaVinci code')", Statement.RETURN_GENERATED_KEYS);
            stat.executeUpdate("INSERT INTO books (name) VALUES ('Solomon key')");

            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            // PreparedStatement preparedStatement = conn.prepareStatement("sql", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");

            while (resultSet.next()){
                System.out.print(resultSet.getInt("id"));
                System.out.println(resultSet.getString("name"));
            }
            System.out.println( "__________" );

            resultSet.last();
            resultSet.updateString("name", "new Value");
            resultSet.updateRow();

            resultSet.moveToInsertRow();
            resultSet.updateString("name", "inserted row");
            resultSet.insertRow();

            resultSet.absolute(2);
            resultSet.deleteRow();

            resultSet.beforeFirst();

            while (resultSet.next()){
                System.out.println(resultSet.getInt("id"));
                System.out.println(resultSet.getString("name"));
            }
        }
    }
}
