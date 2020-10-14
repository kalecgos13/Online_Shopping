package database;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import User_detail.*;


public class check_data {

    public static void main(String args[]) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        FileInputStream inputStream = new FileInputStream("..\\totally_not_credentials.txt");
        String username, password, email;
        try {
            String everything = IOUtils.toString(inputStream);
            username = everything.split("\n")[2];
            password = everything.split("\n")[3];
            email = everything.split("\n")[0];
        } finally {
            inputStream.close();
        }
        Connection cnn = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_shopping", username, password);
        Statement st = cnn.createStatement();
        ResultSet rs = st.executeQuery("select l_email,u_fname from tbl_login,tbl_user_detail where tbl_login.l_id = tbl_user_detail.l_id and tbl_login.l_id =" + 1);
        while (rs.next()) {
            System.out.println(rs.getString(1));
            System.out.println(rs.getString(2));
        }

        Email_check check = new Email_check();
        System.out.println(check.check_email(email));
    }
}
