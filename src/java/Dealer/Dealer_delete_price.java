package Dealer;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.*;
import java.sql.Connection;
import java.sql.Statement;
import javax.servlet.http.HttpSession;
import java.util.logging.*;
/**
 *
 * @author Vicky
 */
@WebServlet(name = "Dealer_delete_price", urlPatterns = {"/Dealer_delete_price"})
public class Dealer_delete_price extends HttpServlet 
{
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public void service(HttpServletRequest req,HttpServletResponse res)
    {
        HttpSession usersession = req.getSession();
        try
        {
            Database_connection obj_connection = new Database_connection();
            obj_connection.doPreparedUpdate("delete from tbl_dealer where l_id = ? and p_id = ?", new int[]{0,0}, new Object[]{usersession.getAttribute("dealer"),Integer.parseInt(req.getParameter("pid"))});
             String referer = req.getHeader("Referer");
            res.sendRedirect(referer);
        }
        catch(Exception ex)
        {
            LOG.warning("Failed due to Error: " + ex);
        }
    }
}
