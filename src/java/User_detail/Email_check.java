package User_detail;

import database.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Email_check {

    public Database_connection obj_connection;

    public Email_check() {
        try {
            obj_connection = new Database_connection();
        } catch (Exception ex) {
        }
    }

    public boolean check_email(String email_id) {
        boolean b = false;
        try {
            ResultSet rs = obj_connection.doPreparedQuery("select * from tbl_login where l_email = ?", new int[]{1}, new Object[]{email_id});
            while (rs.next()) {
                b = true;
            }
        }
         catch (Exception ex) {
        }
        return b;
    }
}
