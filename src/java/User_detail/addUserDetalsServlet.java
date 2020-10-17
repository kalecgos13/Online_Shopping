package User_detail;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.util.logging.*;


@WebServlet(name = "addUserDetalsServlet", urlPatterns = {"/addUserDetalsServlet"})
public class addUserDetalsServlet extends HttpServlet 
{
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    @Override
    public void service(HttpServletRequest req , HttpServletResponse res) throws ServletException, IOException
    {
        HttpSession usersession = req.getSession();
        RequestDispatcher rd = req.getRequestDispatcher("/cust_message.jsp");
        try
        {
            Database_connection obj_connection = new Database_connection();
            String query = "update tbl_user_detail set u_fname = ?,u_lname = ?,u_gender=?,u_contact=?,u_add= ?,u_city = ?,u_state= ?,u_country = ?,u_pincode = ? where l_id = ?";
            obj_connection.doPreparedUpdate(query, new int[]{1,1,1,1,1,0,0,0,0}, new Object[]{
            req.getParameter("fname"),
            req.getParameter("lname"),
            req.getParameter("gender"),
            req.getParameter("mobileNum"),
            req.getParameter("address"),
            1,1,1,
            Integer.parseInt(req.getParameter("pincode")),
            usersession.getAttribute("user")});
          //  req.setAttribute("message","Success Fully The Change Detail");
          //  rd.forward(req, res);
            res.sendRedirect(req.getContextPath()+"/User_info.jsp");
        }
        catch(Exception ex)
        {
             LOG.warning("Failed due to Error: " + ex);
             rd.forward(req, res);
        }
    }
}
