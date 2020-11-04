package check_out_process;

import Add_To_Cart.Cart;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import database.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import java.util.logging.*;

@WebServlet(name = "Buy_items", urlPatterns = {"/Buy_items"})
public class Buy_items extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession usersession = req.getSession();
        boolean result = true;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        HashMap<Integer, Cart> hm = (HashMap<Integer, Cart>) usersession.getAttribute("Cart");
        PrintWriter out = res.getWriter();
        String Erorr = "";
        int billno = 0;
              
        try {
            Database_connection obj_connection = new Database_connection();
            Connection cnn = obj_connection.cnn;
            Statement st = cnn.createStatement();
            CallableStatement cs = cnn.prepareCall("{call user_buy_item1(?,?,?,?)}");
            ResultSet rs = obj_connection.doPreparedQuery("select u_fname from tbl_user_detail where l_id=?", new int[]{0}, new Object[]{usersession.getAttribute("user")});
            while (rs.next()) {
                String f_name = rs.getString(1);
                if (f_name == null) {
                    result = false;
                }
            }
            if (result) // User INFO is Alerady Feel OUT Then Directly Pursech 
            {

                int total = 0;
                for (int i : hm.keySet()) {
                    Cart full_cart = (Cart) hm.get(i);
                    total += (full_cart.p_qty * full_cart.p_price);
                }
                obj_connection.doPreparedUpdate("insert into tbl_order values(null,?,?,'Pending',?)", new int[]{0,1,0}, new Object[]{usersession.getAttribute("user"),dateFormat.format(date),total});
                rs = st.executeQuery("select max(o_id) from tbl_order");
                while (rs.next()) {
                    billno = rs.getInt(1);
                }
                for (int i : hm.keySet()) {
                    Cart full_cart = (Cart) hm.get(i);
                    //     st.execute("insert into tbl_order_detail values(null,"+billno+","+full_cart.pid+","+full_cart.p_qty+","+(full_cart.p_qty * full_cart.p_price)+")");
                    int check_qty = 0;
                    rs = obj_connection.doPreparedQuery("select p_qty from tbl_product where p_id = ?", new int[]{0}, new Object[]{full_cart.pid});
                    while (rs.next()) {
                        check_qty = rs.getInt(1);
                    }

                    if (check_qty >= full_cart.p_qty) {
                        cs.setInt(1, billno);
                        cs.setInt(2, full_cart.pid);
                        cs.setInt(3, full_cart.p_qty);
                        cs.setInt(4, (full_cart.p_qty * full_cart.p_price));
                        cs.execute();
                    } else {
                        Erorr += full_cart.p_name + ",";
                    }
                }

            } else //First Detail Add in user_table and After Purches the product 
            {
                String user_first_name = req.getParameter("name");
                String user_mobile = req.getParameter("mobile");
                String user_address = req.getParameter("address");
                int pincode = Integer.parseInt(req.getParameter("pincode"));
                obj_connection.doPreparedUpdate("update tbl_user_detail set u_fname = ?,u_contact= ?,u_add= ?,u_pincode= ? where l_id= ?", new int[]{1,1,1,0,0}, new Object[]{user_first_name,user_mobile,user_address,pincode,usersession.getAttribute("user")});
                int total = 0;
                for (int i : hm.keySet()) {
                    Cart full_cart = (Cart) hm.get(i);
                    total += (full_cart.p_qty * full_cart.p_price);
                }
                obj_connection.doPreparedUpdate("insert into tbl_order values(null,?,?,'Pending',?)", new int[]{0,1,0}, new Object[]{usersession.getAttribute("user"),dateFormat.format(date),total});
                rs = st.executeQuery("select max(o_id) from tbl_order");
                while (rs.next()) {
                    billno = rs.getInt(1);
                }
                for (int i : hm.keySet()) {
                    Cart full_cart = (Cart) hm.get(i);
                    //         st.execute("insert into tbl_order_detail values(null,"+billno+","+full_cart.pid+","+full_cart.p_qty+","+(full_cart.p_qty * full_cart.p_price)+")");
                    int check_qty = 0;
                    rs = obj_connection.doPreparedQuery("select p_qty from tbl_product where p_id = ?", new int[]{0}, new Object[]{full_cart.pid});
                    while (rs.next()) {
                        check_qty = rs.getInt(1);
                    }

                    if (check_qty >= full_cart.p_qty) {
                        cs.setInt(1, billno);
                        cs.setInt(2, full_cart.pid);
                        cs.setInt(3, full_cart.p_qty);
                        cs.setInt(4, (full_cart.p_qty * full_cart.p_price));
                        cs.execute();
                    } else {
                        Erorr += full_cart.p_name + ",";
                    }

                }
            }
            hm.clear();
            usersession.setAttribute("Cart", hm);
            RequestDispatcher rd = req.getRequestDispatcher("/message1.jsp");
            if (Erorr.equals("")) 
            {
                req.setAttribute("message", "Order will be placed Check Account");
				LOG.info("User " + usersession.getAttribute("user") + " has SUCCESFULLY placed an order for: " + hm);
                
            } else {
                
                String tmpmsg = "Order will be placed Check Account";
                int tmpcheck = 0;
                rs = st.executeQuery("select count(*) from tbl_order_detail where o_id = "+ billno);
                while(rs.next())
                {
                    tmpcheck = rs.getInt(1);
                }
                
                if(tmpcheck > 0)
                {
					LOG.info("User " + usersession.getAttribute("user") + " has placed an order for: " + hm);
					LOG.info("The products for this order are out of stock");
                    req.setAttribute("message",tmpmsg);
                    req.setAttribute("message1", "But " + Erorr + " Out OF Stock");
                }
                else
                {
                    st.execute("delete from tbl_order where o_id = "+billno);
                    tmpmsg = "Your Order Will be Cancle";
					LOG.info("User " + usersession.getAttribute("user") + " has placed an order for: " + hm);
					LOG.info("The order has been canceled.");
                    req.setAttribute("message",tmpmsg);
                    req.setAttribute("message1", "Because" + Erorr + " Out OF Stock");
                }
            }
            rd.forward(req, res);

        } catch (Exception ex) {
            LOG.warning("Failed due to Error: " + ex);
        }
    }
}