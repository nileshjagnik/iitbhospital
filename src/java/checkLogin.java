/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Pulkit
 */
@WebServlet(name = "checkLogin", urlPatterns = {"/checkLogin"})
public class checkLogin extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private static final String DBNAME = "mydb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "vallap";
    private static final String DBSERVER = "localhost";
    private static final String LOGIN_PATIENT_QUERY = "select * from patient where patient_id=?";
    private static final String LOGIN_STUDENT_QUERY = "select * from student where roll_no=? and password=?";
    private static final String LOGIN_STAFF_QUERY = "select * from staff where staff_id=? and password=?";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet checkLogin</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet checkLogin at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            //response.sendRedirect("interfaces/appointments.html");
                String strUserName = request.getParameter("username").toString();
                String strPassword = request.getParameter("password").toString();
                System.out.println(strUserName);
                String strErrMsg = null;
                HttpSession session = request.getSession();
                boolean isValidLogon = false;
                boolean isValid=false;
                String patient_type ="";
                Connection con;
                con = null;
               
         try { 
                con = connect();
                PreparedStatement prepStmt = con.prepareStatement(LOGIN_PATIENT_QUERY);
                prepStmt.setString(1, strUserName);
                //prepStmt.setString(2, strPassword);
                ResultSet rs = prepStmt.executeQuery();
                if(rs.next()) {
                //System.out.println("User login is valid in DB");
                isValid = true;
                patient_type=rs.getString(2);
                  }
        } catch (Exception ex) {
            Logger.getLogger(checkLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
         finally {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(checkLogin.class.getName()).log(Level.SEVERE, null, ex);
                    }
         }
         
         if(isValid)
         {
             if(patient_type.equals("student"))
             {
                 try {
                     con = connect();
                      PreparedStatement prepStmt = con.prepareStatement(LOGIN_STUDENT_QUERY);
                    prepStmt.setString(1, strUserName);
                    prepStmt.setString(2, strPassword);
                    ResultSet rs = prepStmt.executeQuery();
                    if(rs.next()) {
                    //System.out.println("User login is valid in DB");
                    isValidLogon = true;
                      }
                 } catch (Exception ex) {
                     Logger.getLogger(checkLogin.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 finally {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(checkLogin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
             }
             else if(patient_type.equals("staff"))
             {
                 try {
                     con = connect();
                      PreparedStatement prepStmt = con.prepareStatement(LOGIN_STAFF_QUERY);
                    prepStmt.setString(1, strUserName);
                    prepStmt.setString(2, strPassword);
                    ResultSet rs = prepStmt.executeQuery();
                    if(rs.next()) {
                    //System.out.println("User login is valid in DB");
                    isValidLogon = true;
                      }
                 } catch (Exception ex) {
                     Logger.getLogger(checkLogin.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 finally {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(checkLogin.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
             }
             else
             {
                 response.sendRedirect("interfaces/index.html");
             }
             
             
             if(isValidLogon)
             {
                 session.setAttribute("username", strUserName);
                 session.setAttribute("password", strPassword);
                 response.sendRedirect("interfaces/menu.jsp");
             }
             else
             {
                 response.sendRedirect("interfaces/index.html");
             }
         }
         else
         {
             response.sendRedirect("interfaces/index.html");
         }
    }
    
    
    Connection connect() throws Exception
    {
        Connection con=null;
        try
        {
            String url = "jdbc:mysql://"+DBSERVER+"/"+DBNAME+"?user="+DB_USERNAME+"&password="+DB_PASSWORD;
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url);
        } 
        catch (SQLException sqle) 
        {
            System.out.println("SQLException: Unable to open connection to db: "+sqle.getMessage());
            throw sqle;
        }
         catch(Exception e)
        {
            System.out.println("Exception: Unable to open connection to db: "+e.getMessage());
            throw e;
        }
        
        return con;
        
        
    }


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
