package database;

import java.sql.*;

public class Database_connection {

    public Class c;
    public Connection cnn;

    public Database_connection() throws ClassNotFoundException, SQLException {
        FileInputStream inputStream = new FileInputStream("..\\totally_not_credentials.txt");
        String username, password;
        try {
            String everything = IOUtils.toString(inputStream);
            username = everything.split("\n")[4];
            password = everything.split("\n")[5];
        } finally {
            inputStream.close();
        }
        c.forName("com.mysql.jdbc.Driver");
        cnn = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_shopping?noAccessToProcedureBodies=true", username, password);
    }
}
