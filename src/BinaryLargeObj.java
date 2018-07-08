import com.sun.xml.internal.ws.addressing.WsaTubeHelperImpl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

public class BinaryLargeObj {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        String username = "root";
        String password = "wrong_pass"; // Изменить это поле
        String connectionUrl = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useSSL=false";

        Class.forName("com.mysql.cj.jdbc.Driver");

        // writing image in BD
        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password);
             Statement stat = conn.createStatement()) {
            stat.execute("DROP TABLE Users");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS books (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, img BLOB, PRIMARY KEY (id))");
            BufferedImage image = ImageIO.read(new File("smile.jpg"));
            Blob blob = conn.createBlob();
            try ( OutputStream outputStream = blob.setBinaryStream(1)) {
                ImageIO.write(image, "jpg", outputStream);
            }
            PreparedStatement statement = conn.prepareStatement("INSERT INTO books (name, img) VALUES (?,?)");
            statement.setString(1,"inferno");
            statement.setBlob(2, blob);
            statement.execute();

        // reading image from BD
            ResultSet resultSet = stat.executeQuery("SELECT * FROM books");
            while (resultSet.next()){
                Blob blob2 = resultSet.getBlob("img");
                BufferedImage image2 = ImageIO.read(blob2.getBinaryStream());
                File outputFile = new File("savedd.png");
                ImageIO.write(image2, "png", outputFile);
            }
        }
    }
}
