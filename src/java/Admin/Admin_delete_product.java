package Admin;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import database.*;
import javax.servlet.http.HttpSession;
import Admin.Product_delete_after_inform;
import java.util.logging.*;

@WebServlet(name = "Admin_delete_product", urlPatterns = {"/Admin_delete_product"})
public class Admin_delete_product extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {

        HttpSession usersession = req.getSession();
        PrintWriter out = res.getWriter();
        if (usersession.getAttribute("admin") == null) {

            res.sendRedirect(req.getContextPath() + "/index.jsp");
            LOG.info("Usersession has no admin attribute. Therefore access denied.");
        } else {
            try {
                if (req.getParameter("pid") != null) 
                {
                    int pid = Integer.parseInt(req.getParameter("pid"));
                    //inform this product to out of stock
                    
                new Product_delete_after_inform(pid).start();
                }
                res.sendRedirect(req.getContextPath() + "/Admin_dash_bord.jsp");

            } catch (Exception ex) {
                LOG.warning("Failed due to Error: " + ex);
            }
        }
    }
}
