/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.servlet.RequestDispatcher;
import java.util.logging.*;
/**
 *
 * @author Vicky
 */
@WebServlet(name = "Admin_delete_img", urlPatterns = {"/Admin_delete_img"})
public class Admin_delete_img extends HttpServlet 
{
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    @Override
    public void service(HttpServletRequest req,HttpServletResponse res) throws IOException
    {
        PrintWriter out = res.getWriter();
        try
        {
            Database_connection obj_connection = new Database_connection();
            String query = "delete from tbl_display_img where p_id = ? and d_img_id = ?";
            out.println(req.getParameter("pid"));
            out.println(req.getParameter("imgid"));
            obj_connection.doPreparedUpdate(query, new int[]{0,0},new Object[]{Integer.parseInt(req.getParameter("pid")),Integer.parseInt(req.getParameter("imgid"))});
           
            RequestDispatcher rd = req.getRequestDispatcher("Admin_image_upload.jsp");
            req.setAttribute("p_id",req.getParameter("pid"));
            rd.forward(req, res);
        }
        catch(Exception ex)
        {
            LOG.warning("Failed due to Error: " + ex);
        }
    }
}
