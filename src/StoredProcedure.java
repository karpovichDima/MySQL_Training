import java.sql.*;

public class StoredProcedure {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String username = "root";
        String password = "wrongPass"; // Изменить это поле
        String connectionUrl = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useSSL=false";
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password);
             Statement stat = conn.createStatement()) {
            stat.execute("DROP TABLE IF EXISTS books");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, dt DATE, PRIMARY KEY (id))");
            stat.executeUpdate("INSERT INTO books (name) VALUES ('Inferno')");
            stat.executeUpdate("INSERT INTO books (name) VALUES ('Solomon key')");

            CallableStatement callableStatement = conn.prepareCall("{call Counter(?)}");
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.execute();

            System.out.println(callableStatement.getInt(1));
            System.out.println("------------");
        }
    }
}