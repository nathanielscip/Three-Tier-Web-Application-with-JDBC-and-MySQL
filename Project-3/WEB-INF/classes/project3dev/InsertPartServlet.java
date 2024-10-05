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

public class InsertPartServlet extends HttpServlet {
    private Connection connection;
    private PreparedStatement statement;
    private int mysqlReturnValue = 0;
    

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        // job record format: (pnum, pname, color, weight, city)  Data types: (string, string, string, int, string)
        String pnum = request.getParameter("paPnum").trim();
        String pname = request.getParameter("pname").trim();
        String color = request.getParameter("color").trim();
        String weightInput = request.getParameter("weight").trim();
        String city = request.getParameter("paCity").trim();
        int weight = 0;

        if(pnum.equals("") || pname.equals("") || color.equals("") || weightInput.equals("") || city.equals("")){
            String message = "Error entering data: Please fill in all values to insert into table";
            HttpSession session = request.getSession();
            session.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dataentryHome.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            weight = Integer.parseInt(weightInput);

        } catch (NumberFormatException e) {
            String message = "Error entering data: Weight must be an integer value, please enter a valid number.";

            HttpSession session = request.getSession();
            session.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dataentryHome.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try{
            getDBConnection();

            String updateCommand = "insert into parts values (?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(updateCommand);
            statement.setString(1, pnum);
            statement.setString(2, pname);
            statement.setString(3, color);
            statement.setInt(4, weight);
            statement.setString(5, city);

            mysqlReturnValue = statement.executeUpdate();

            if(mysqlReturnValue != 0){
                String insertMessage = String.format("<span style='color:green;'>New parts record: (%s, %s, %s, %d, %s) successfully entered into database.</span>", pnum, pname, color, weight, city);
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