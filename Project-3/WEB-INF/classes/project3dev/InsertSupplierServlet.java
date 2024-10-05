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

public class InsertSupplierServlet extends HttpServlet {
    private Connection connection;
    private PreparedStatement statement;
    private int mysqlReturnValue = 0;
    

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        // supplier record format: (snum, sname, status, city)  Data types: (string, string, int, string)
        String snum = request.getParameter("suSnum").trim();
        String sname = request.getParameter("sname").trim();
        String statusInput = request.getParameter("status").trim();
        String city = request.getParameter("suCity").trim();
        int status = 0;

        if(snum.equals("") || sname.equals("") || statusInput.equals("") || city.equals("")){
            String message = "Error entering data: Please fill in all values to insert into table";
            HttpSession session = request.getSession();
            session.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dataentryHome.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            status = Integer.parseInt(statusInput);

        } catch (NumberFormatException e) {
            String message = "Error entering data: Status must be an integer value, please enter a valid number.";

            HttpSession session = request.getSession();
            session.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dataentryHome.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try{
            getDBConnection();

            String updateCommand = "insert into suppliers values (?, ?, ?, ?)";

            statement = connection.prepareStatement(updateCommand);
            statement.setString(1, snum);
            statement.setString(2, sname);
            statement.setInt(3, status);
            statement.setString(4, city);

            mysqlReturnValue = statement.executeUpdate();

            if(mysqlReturnValue != 0){
                String insertMessage = String.format("<span style='color:green;'>New parts record: (%s, %s, %d, %s) successfully entered into database.</span>", snum, sname, status, city);
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
