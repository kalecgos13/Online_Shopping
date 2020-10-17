package Admin;

import cutomer_email_send.validation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.*;
import java.sql.*;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author Vicky
 */
@WebServlet(name = "Admin_add", urlPatterns = {"/Admin_add"})
public class Admin_add extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();  
        try {
            RequestDispatcher rd = req.getRequestDispatcher("Admin_new_add.jsp");
            String email_id = req.getParameter("email");
            String name = req.getParameter("fname");
            Random r = new Random();
            int Low = 65;
            int High = 90;
            String password = "";

            for (int i = 0; i < 10; i++) {
                int Result = r.nextInt(High - Low) + Low;
                password += (char) Result;
            }
            
            Database_connection obj_connection = new Database_connection();          
            Connection cnn = obj_connection.cnn;
            ResultSet rs = obj_connection.doPreparedQuery("select * from tbl_login where l_email = ?", new int[]{1}, new Object[]{email_id});
            boolean check = false;
            while(rs.next())
            {
                check = true;
            }
            
            if(check)
            {
                req.setAttribute("msg","Email is already registered");
                rd.forward(req, res);
            }
            else
            {
                Algorithm_password a = new Algorithm_password();
                String salt = a.generate_salt();
                String new_pass = a.Encrypt_password(password, salt, 10000, 512);
                
                CallableStatement cb = cnn.prepareCall("{ call st_new_user(?,?,?,?)}");
                cb.setString(1,email_id);
                cb.setString(2,new_pass);
                cb.setString(3,"private");
                cb.setString(4, name);
                cb.execute();
                
                obj_connection.doPreparedUpdate("insert into tbl_login_salt(l_salt,l_id) values(?, (select l_id from tbl_login where l_email = ? and l_pass = ?))",new int[]{1,1,1}, new Object[]{salt,email_id,new_pass});
                validation v = new validation(email_id,"hiii "+ email_id +" your password "+ password);
                req.setAttribute("msg","Password successfully send Check your mail");
                rd.forward(req, res);
            }
            
        } catch (Exception ex)
        {
            out.println(ex);
        }
    }
}
