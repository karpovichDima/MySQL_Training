import java.sql.*;

public class ScrollablesSet {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String username = "root";
        String password = "wrong_pass";
        String connectionUrl = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useSSL=false";
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Scroll in BD (methods)
        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password);
             Statement stat = conn.createStatement()) {
            stat.execute("DROP TABLE IF EXISTS books");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, PRIMARY KEY (id))");
            stat.executeUpdate("INSERT INTO books (name) VALUES ('Inferno')");
            stat.executeUpdate("INSERT INTO books (name) VALUES ('DaVinci code')", Statement.RETURN_GENERATED_KEYS);
            stat.executeUpdate("INSERT INTO books (name) VALUES ('Solomon key')");

            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // PreparedStatement preparedStatement = conn.prepareStatement("", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            if (resultSet.next()) System.out.println(resultSet.getString("name"));
            if (resultSet.next()) System.out.println(resultSet.getString("name"));
            if (resultSet.previous()) System.out.println(resultSet.getString("name"));
            if (resultSet.relative(2)) System.out.println(resultSet.getString("name"));
            if (resultSet.relative(-2)) System.out.println(resultSet.getString("name"));
            if (resultSet.absolute(2)) System.out.println(resultSet.getString("name"));
            if (resultSet.first()) System.out.println(resultSet.getString("name"));
            if (resultSet.last()) System.out.println(resultSet.getString("name"));
        }
    }
}
