package Forgot_pass;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import database.*;

@WebServlet(name = "Forgot_email", urlPatterns = {"/Forgot_email"})
public class Forgot_email extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        RequestDispatcher rd = req.getRequestDispatcher("/message.jsp");
        boolean check_email = false;
        try {
            String email_id = req.getParameter("email");
            Database_connection obj_connection = new Database_connection();
            ResultSet rs = obj_connection.doPreparedQuery("select * from tbl_login where l_email = ?", new int[]{1}, new Object[]{email_id});
            while (rs.next()) {
                check_email = true;
            }

            if (check_email) {
                Random r = new Random();
                int Low = 0;
                int High = Integer.MAX_VALUE;
                int Result = r.nextInt(High - Low) + Low;
                HttpSession usersession = req.getSession();
                usersession.setAttribute("Tmp_Email", email_id);
                usersession.setAttribute("Code", Result);
                usersession.setMaxInactiveInterval(1 * 60);
                Validation v = new Validation(email_id, Result);
                res.sendRedirect(req.getContextPath() + "/Forgot_email_index_1.jsp");
            } else {
                req.setAttribute("message", "Email id is not Register");
                rd.forward(req, res);
            }
        } catch (Exception ex) {
            req.setAttribute("message", ex);
            rd.forward(req, res);
        }
    }
}

