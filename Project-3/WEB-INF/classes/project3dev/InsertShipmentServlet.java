/*
Name: Nathaniel Scipio
Course: CNT 4714 Summer 2024
Assignment title: Project 3 – Developing A Three-Tier Distributed Web-Based Application
Date: August 1, 2024
Class: CNT 4714
*/

package project3dev;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;

public class InsertShipmentServlet extends HttpServlet {
    private Connection connection;
    private PreparedStatement statement;
    private int mysqlReturnValue = 0;
    private int businessLogicVal = 0;
    private String insertMessage = "";
    

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        // jobs record format: (snum, pnum, jnum, quantity)  Data types: (string, string, string, int)
        String snum = request.getParameter("shSnum").trim();
        String pnum = request.getParameter("shPnum").trim();
        String jnum = request.getParameter("shJnum").trim();
        String quantityInput = request.getParameter("quantity").trim();
        int quantity = 0;

        if(snum.equals("") || pnum.equals("") || jnum.equals("") || quantityInput.equals("")){
            String message = "Error entering data: Please fill in all values to insert into table";
            HttpSession session = request.getSession();
            session.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dataentryHome.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            quantity = Integer.parseInt(quantityInput);

        } catch (NumberFormatException e) {
            String message = "Error entering data: Quantity must be an integer value, please enter a valid number.";

            HttpSession session = request.getSession();
            session.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dataentryHome.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try{
            getDBConnection();

            String updateCommand = "insert into shipments values (?, ?, ?, ?)";

            statement = connection.prepareStatement(updateCommand);
            statement.setString(1, snum);
            statement.setString(2, pnum);
            statement.setString(3, jnum);
            statement.setInt(4, quantity);

            mysqlReturnValue = statement.executeUpdate();

            if(mysqlReturnValue != 0){
                if(quantity >= 100){
                    String businessLogicUpdate = "update suppliers set status = status + 5 where snum in (select snum from shipments where quantity >= 100)";
                    businessLogicVal = statement.executeUpdate(businessLogicUpdate);
                    insertMessage = String.format("<span style='color:green;'>New shipments record: (%s, %s, %s, %d) successfully entered into database.<br>Business Logic Detected! Business Logic updated " + businessLogicVal + " supplier status marks</span>", snum, pnum, jnum, quantity);
                }
                else{
                    insertMessage = String.format("<span style='color:green;'>New shipments record: (%s, %s, %s, %d) successfully entered into database.<br>Business logic not triggered.</span>", snum, pnum, jnum, quantity);
                }
                HttpSession session = request.getSession();
                session.setAttribute("message", insertMessage);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dataentryHome.jsp");
                dispatcher.forward(request, response);

            }
            else{
                String errorMessage = ("Error occurred while inserting into database. Please try again.");
                HttpSession session = request.getSession();
                session.setAttribute("message", errorMessage);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dataentryHome.jsp");
                dispatcher.forward(request, response);
            }

            statement.close();
            
        }catch(SQLException e){
            e.printStackTrace();
            String message = "Error executing the SQL statement: " + e.getMessage();
            HttpSession session = request.getSession();
            session.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dataentryHome.jsp");
            dispatcher.forward(request, response);
        }
    }

    
    private void getDBConnection(){
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;

        //read system app properties file
        try{
            filein = new FileInputStream("/Library/Tomcat10126/webapps/Project-3/WEB-INF/lib/dataentry.properties");
            properties.load(filein);
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            connection = dataSource.getConnection();
        } catch (SQLException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        } 
        finally{
            try{
                if(filein != null) filein.close();
            } catch(IOException e){
                e.printStackTrace();;
            }
        
        }
    } 
}

