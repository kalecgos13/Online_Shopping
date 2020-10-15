package Forgot_pass;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import database.*;
import Admin.*;

@WebServlet(name = "Reset_pass", urlPatterns = {"/Reset_pass"})
public class Reset_pass extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            String pass = req.getParameter("pass");
            String cpass = req.getParameter("cpass");
            HttpSession usersession = req.getSession();
            String tmp_email = usersession.getAttribute("Tmp_Email").toString();
            RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
            RequestDispatcher rd1 = req.getRequestDispatcher("/message1.jsp");
            if(usersession.getAttribute("check")!=null)
            {
            if (pass.equals(cpass)) {

                try {
                    
                    Algorithm_password a = new Algorithm_password();
                    String salt = a.generate_salt();
                    String new_pass = a.Encrypt_password(pass, salt, 10000, 512);
                    
                    Database_connection obj_connection = new Database_connection();   // change the password
                    Connection cnn = obj_connection.cnn;
                    Statement st = cnn.createStatement();
                    obj_connection.doPreparedUpdate("update tbl_login set l_pass= ? where l_email= ?", new int[]{1,1}, new Object[]{new_pass,tmp_email});
                    obj_connection.doPreparedUpdate("update tbl_login_salt set l_salt = ? where l_id = (select l_id from tbl_login where l_email = ? and l_pass = ?)", new int[] {1,1,1}, new Object[]{salt, tmp_email, new_pass});
                    
                    usersession.invalidate();    // session invalid set 
                    req.setAttribute("message", "password successfully changed");
                    rd1.forward(req, res);
                } catch (Exception ex) {
                    req.setAttribute("message", ex);   // database related problem display
                    rd.forward(req, res);
                }
            } else {
                req.setAttribute("message", "Both Password is Not Match");
                rd.forward(req, res);
            }
        }else
            {
                res.sendRedirect(req.getContextPath()+"/index.jsp");
            }
        } catch (Exception ex) {
            res.sendRedirect(req.getContextPath() + "/Forgot_email_index.jsp");
        }
    }
}