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
 * @author
 * Pulkit
 */
@WebServlet(name = "bookAppointment", urlPatterns = {"/bookAppointment"})
public class bookAppointment extends HttpServlet {

    /**
     * Processes
     * requests
     * for
     * both
     * HTTP
     * <code>GET</code>
     * and
     * <code>POST</code>
     * methods.
     *
     * @param
     * request
     * servlet
     * request
     * @param
     * response
     * servlet
     * response
     * @throws
     * ServletException
     * if
     * a
     * servlet-specific
     * error
     * occurs
     * @throws
     * IOException
     * if
     * an
     * I/O
     * error
     * occurs
     */
    private static final String DBNAME = "mydb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "vallap";
    private static final String DBSERVER = "localhost";
    private static final String BY_DOCTOR_QUERY="Select doc_id,doctor.name ,qualifications,designation,age from doctor,department where doctor.department_id=department.department_id and department.name=?";
    private static final String BY_TIMESLOT_QUERY="Select * from timeslot";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet bookAppointment</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet bookAppointment at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles
     * the
     * HTTP
     * <code>GET</code>
     * method.
     *
     * @param
     * request
     * servlet
     * request
     * @param
     * response
     * servlet
     * response
     * @throws
     * ServletException
     * if
     * a
     * servlet-specific
     * error
     * occurs
     * @throws
     * IOException
     * if
     * an
     * I/O
     * error
     * occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         HttpSession session = request.getSession();
                String username=(String)session.getAttribute("username");
                String password=(String)session.getAttribute("password");
                checkLoginObj clo = new checkLoginObj();
                int isLoggedIn=clo.isLoggedIn(username, password);
                if(isLoggedIn==0)
                {
                    response.sendRedirect("interfaces/index.html");
                }
                else
                {
                    processRequest(request, response);
                }
    }

    /**
     * Handles
     * the
     * HTTP
     * <code>POST</code>
     * method.
     *
     * @param
     * request
     * servlet
     * request
     * @param
     * response
     * servlet
     * response
     * @throws
     * ServletException
     * if
     * a
     * servlet-specific
     * error
     * occurs
     * @throws
     * IOException
     * if
     * an
     * I/O
     * error
     * occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         HttpSession session = request.getSession();
                String username=(String)session.getAttribute("username");
                String password=(String)session.getAttribute("password");
                checkLoginObj clo = new checkLoginObj();
                int isLoggedIn=clo.isLoggedIn(username, password);
                if(isLoggedIn==0)
                {
                    response.sendRedirect("interfaces/index.html");
                }
                else
                {
                  //  processRequest(request, response);
                
        Connection con=null;
        String option = request.getParameter("option").toString();
        String department = request.getParameter("dept").toString();
        if(option.equals("by-doctor"))
        {
                    try {
                        con = connect();
                        PreparedStatement prepStmt = con.prepareStatement(BY_DOCTOR_QUERY);
                        prepStmt.setString(1, department);
                        ResultSet rs = prepStmt.executeQuery();
                        request.setAttribute("doctors",rs);
                        request.setAttribute("department",department);
                        request.getRequestDispatcher("interfaces/book_by_doctor1.jsp").forward(request, response);
                    } catch (Exception ex) {
                        Logger.getLogger(bookAppointment.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally{try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(bookAppointment.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
            
};
        }
        else
        {
            try {
                        con = connect();
                        PreparedStatement prepStmt = con.prepareStatement(BY_TIMESLOT_QUERY);
                        //prepStmt.setString(1, department);
                        ResultSet rs = prepStmt.executeQuery();
                        request.setAttribute("timeslots",rs);
                        request.setAttribute("department",department);
                        request.getRequestDispatcher("interfaces/book_by_timeslot1.jsp").forward(request, response);
                    } catch (Exception ex) {
                        Logger.getLogger(bookAppointment.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally{try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(bookAppointment.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
            
                        };
        }
    }}

    /**
     * Returns
     * a
     * short
     * description
     * of
     * the
     * servlet.
     *
     * @return
     * a
     * String
     * containing
     * servlet
     * description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
