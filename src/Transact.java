import java.sql.*;

public class Transact {
    public static void main(String[] args) {
        String username = "root";
        String password = "wrong_pass";
        String connectionUrl = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useSSL=false";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password);
             Statement stat = conn.createStatement()) {
            conn.setAutoCommit(false);
            stat.execute("DROP TABLE if EXISTS books");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, PRIMARY KEY (id))");
            stat.executeUpdate("INSERT INTO books (name) VALUES ('Inferno')");
            Savepoint savepoint = conn.setSavepoint();
            stat.executeUpdate("INSERT INTO books SET name = 'Solomon key'");

            conn.rollback(savepoint);
            conn.commit();
            conn.releaseSavepoint(savepoint);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
