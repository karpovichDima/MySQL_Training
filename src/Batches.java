import java.sql.*;

public class Batches {
    public static void main(String[] args) {
        String username = "root";
        String password = "Karpovich9";
        String connectionUrl = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useSSL=false";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password);
             Statement stat = conn.createStatement()) {

            conn.setAutoCommit(false);

            stat.addBatch("DROP TABLE if EXISTS books");
            stat.addBatch("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, PRIMARY KEY (id))");
            stat.addBatch("INSERT INTO books (name) VALUES ('Inferno')");
            stat.addBatch("INSERT INTO books SET name = 'Solomon key'");

            if (stat.executeBatch().length == 4){
                conn.commit();
            }else{
                conn.rollback();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
