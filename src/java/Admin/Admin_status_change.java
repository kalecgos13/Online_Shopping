package Admin;

import database.*;
import java.sql.*;

public class Admin_status_change extends Thread {

    public int o_id;
    public String status;
    public Database_connection obj_connection;
   
    public Admin_status_change(int o_id, String status) {
        this.o_id = o_id;
        this.status = status;
        try {
            obj_connection = new Database_connection();
        } catch (Exception ex) {
        }
    }

    @Override
    public void run() 
    {
        try
        {
            obj_connection.doPreparedUpdate("update tbl_order set order_status = ? where o_id = ?", new int[]{1,0}, new Object[]{status,o_id});
        }
        catch(Exception ex)
        {
            
        }
        
    }
}
