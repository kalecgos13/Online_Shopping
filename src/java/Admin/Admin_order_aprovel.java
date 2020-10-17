package Admin;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.*;
import java.sql.*;
import javax.servlet.http.HttpSession;
import Admin.Admin_status_change;
import java.util.logging.*;
/**
 *
 * @author Vicky
 */



@WebServlet(name = "Admin_order_aprovel", urlPatterns = {"/Admin_order_aprovel"})
public class Admin_order_aprovel extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {

        HttpSession usersession = req.getSession();
        if (usersession.getAttribute("admin") == null) {
            res.sendRedirect(req.getContextPath() + "/index.jsp");
            LOG.info("User is not an Admin. Therefore access denied.");
        } else {

            PrintWriter out = res.getWriter();
            try 
            {
                String[] all_oid = req.getParameterValues("check");
                for (String s : all_oid) {
                    int o_id = Integer.parseInt(s);
                    new Admin_status_change(o_id,"approve").start();
                }


            } catch (Exception ex) {
                LOG.warning("Failed due to Error: " + ex);
            }
            finally
            {
                String referer = req.getHeader("Referer");
                res.sendRedirect(referer);
            }
        }
    }
}
