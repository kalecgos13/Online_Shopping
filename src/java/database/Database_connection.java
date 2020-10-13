package database;

import java.sql.*;

public class Database_connection {

    public Class c;
    public Connection cnn;

    public Database_connection() throws ClassNotFoundException, SQLException {
        c.forName("com.mysql.jdbc.Driver");
        cnn = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_shopping?noAccessToProcedureBodies=true", "onlineshopping", "bot123456789");
        //   cnn = DriverManager.getConnection("jdbc:mysql://mariadb15859-saikiran.cloudhosting.rsaweb.co.za/online_db", "root", "BDGkro86104");      
    }
    
    public ResultSet doPrepared(String query, int[] types, String[] arguments) throws SQLException {
		PreparedStatement st = cnn.prepareStatement(query);
		for(int i = 0;i<types.length;i++) {
			/*
			 * 0 = int
			 * 1 = string
			 */
			if(types[i] == 0) {
				st.setInt(1+i, Integer.parseInt(arguments[i]));
			}
			else if (types[i] == 1) {
				st.setString(1+i, arguments[i]);
			}
		}
		return st.executeQuery();
	}
}
