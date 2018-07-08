import java.sql.*;

public class DateCl {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String username = "root";
        String password = "wrong_pass"; // Изменить это поле
        String connectionUrl = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useSSL=false";
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password);
             Statement stat = conn.createStatement()) {
            stat.execute("DROP TABLE IF EXISTS books");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, dt DATE, PRIMARY KEY (id))");

            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO books (name, dt)VALUES ('someName',?)");
            preparedStatement.setDate(1, new Date(1531035331901L)); // now in millisecond
            preparedStatement.execute();

            // getting date from BD
            ResultSet resultSet = stat.executeQuery("SELECT * FROM books");
            while (resultSet.next()){
                System.out.println(resultSet.getDate("dt"));
            }
        }
    }
}
