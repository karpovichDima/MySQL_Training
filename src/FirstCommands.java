import java.sql.*;

public class FirstCommands {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String username = "root";
        String password = "wrong_pass"; // Изменить это поле
        String connectionUrl = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useSSL=false";

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(connectionUrl, username, password);
             Statement statement = connection.createStatement()){
                statement.executeUpdate("DROP TABLE books"); // удаляем имеющуюся таблицу books
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS Books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, PRIMARY KEY (id))"); // создаем Books заново, задаем поля
                statement.executeUpdate("INSERT INTO Books (name) VALUES ('Inferno')"); // Заполняем ячейку name
                statement.executeUpdate("INSERT INTO Books SET name = 'Solomon key'"); // Заполняем ячейку name, другой способ
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Books");  // достаем всю информацию из таблицы

                while (resultSet.next()){
                    System.out.println(resultSet.getInt("id")); // получаем значения по именам столбцов
                    System.out.println(resultSet.getString("name"));
                    System.out.println("-------------");
                }

                System.out.println("_______________");
                ResultSet rs2 = statement.executeQuery("SELECT name FROM Books WHERE id = 1");
                while (rs2.next()){
                    System.out.println(rs2.getString(1));
                }
        }

    }
}
