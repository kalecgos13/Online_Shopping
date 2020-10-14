package Admin;

import database.*;
import java.sql.*;
import java.util.logging.*;

public class Admin_status_change extends Thread {
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public int o_id;
    public String status;
    public Connection cnn;
    public Statement st;
    
    public Admin_status_change(int o_id, String status) {
        this.o_id = o_id;
        this.status = status;
        try {
            Database_connection obj_connection = new Database_connection();
            cnn = obj_connection.cnn;
            st = cnn.createStatement();
            
        } catch (Exception ex) {
            LOG.warning("Admin_status_change() failed due to Error: " + ex);
        }
    }

    @Override
    public void run() 
    {
        try
        {
            //st.execute("update tbl_order set order_status = '"+ status +"' where o_id = "+o_id);
        }
        catch(Exception ex)
        {
            LOG.warning("run() failed due to Error: " + ex);
        }
        
    }
}
