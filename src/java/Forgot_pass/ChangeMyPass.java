package Forgot_pass;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.*;
import java.sql.*;
import Admin.*;


@WebServlet(name = "ChangeMyPass", urlPatterns = {"/ChangeMyPass"})
public class ChangeMyPass extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String pass = req.getParameter("cpass");
        String email = req.getParameter("cemail");
        PrintWriter out = res.getWriter();
        try {
            Database_connection obj_connection = new Database_connection();
            Algorithm_password a = new Algorithm_password();
            String new_pass = a.Encrypt_password(pass);
            
            obj_connection.doPreparedUpdate("update tbl_login set l_pass= ? where l_email= ?", new int[]{1,1}, new Object[]{new_pass,email});
            res.sendRedirect(req.getContextPath()+"/User_info.jsp");
            
        } catch (Exception ex) 
        {
            out.println(ex);
        }
    }
}
