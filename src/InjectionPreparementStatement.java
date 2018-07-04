import java.sql.*;

public class InjectionPreparementStatement {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String username = "root";
        String password = "wrong_pass"; // Изменить это поле
        String connectionUrl = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useSSL=false";

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(connectionUrl, username, password);
             Statement statement = connection.createStatement()){
            statement.executeUpdate("DROP TABLE Users"); // удаляем имеющуюся таблицу Users
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Users (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, password CHAR(30) NOT NULL, PRIMARY KEY (id))"); // создаем Users заново, задаем поля
            statement.executeUpdate("INSERT INTO Users (name, password) VALUES ('Max','123')"); // Заполняем ячейки name, password
            statement.executeUpdate("INSERT INTO Users SET name = 'otherGuy', password = '1234'"); // Заполняем ячейку name, другой способ

            String userId = "1";
            //String userId = "1' or 1 = '1'";

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE id = ?");
            preparedStatement.setString(1, userId); // Первый параметр помещается на место знака вопроса, в предыдущий sql запрос. Способ защиты от инъекций.
            ResultSet resultSet = preparedStatement.executeQuery(); // Выполняем предыдущие две строки.

            while (resultSet.next()){
                System.out.println("username" + " " + resultSet.getString("name"));
                System.out.println("password" + " " + resultSet.getString("password"));
        }

    }
    }
}

/*
* PreparedStatement - обработка sql запроса и приведение его к безопасному виду (обертка), Использование PreparedStatement может защищатить от инъекций.*/