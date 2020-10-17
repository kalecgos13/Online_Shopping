package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;


public class Database_connection {

    public Class c;
    public Connection cnn;

    public Database_connection() throws ClassNotFoundException, SQLException {
              FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("..\\totally_not_credentials.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Database_connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        String username = null, password = null;
        try {
            String everything = IOUtils.toString(inputStream);
            username = everything.split("\n")[4];
            password = everything.split("\n")[5];
        } catch (IOException ex) {
            Logger.getLogger(Database_connection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Database_connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        c.forName("com.mysql.jdbc.Driver");
        cnn = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_shopping?noAccessToProcedureBodies=true", username, password);
    }
    
    public ResultSet doPreparedQuery(String query, int[] types, Object[] arguments) throws SQLException {
		PreparedStatement st = cnn.prepareStatement(query);
		for(int i = 0;i<types.length;i++) {
			/*
			 * 0 = int
			 * 1 = string
                         * 2 = bool
			 */
			if(types[i] == 0) {
				st.setInt(1+i, (Integer) arguments[i]);
			}
			else if (types[i] == 1) {
				st.setString(1+i, (String) arguments[i]);
			}
                        else if(types[i] == 2) {
                            st.setBoolean(1+i, (Boolean) arguments[i]);
                        }
		}
		return st.executeQuery();
	}
    public void doPreparedUpdate(String query, int[] types, Object[] arguments) throws SQLException {
		PreparedStatement st = cnn.prepareStatement(query);
		for(int i = 0;i<types.length;i++) {
			/*
			 * 0 = int
			 * 1 = string
                         * 2 = bool
			 */
			if(types[i] == 0) {
				st.setInt(1+i, (Integer) arguments[i]);
			}
			else if (types[i] == 1) {
				st.setString(1+i, (String) arguments[i]);
			}
                        else if(types[i] == 2) {
                            st.setBoolean(1+i, (Boolean) arguments[i]);
                        }
		}
		st.executeUpdate();
	}
}
