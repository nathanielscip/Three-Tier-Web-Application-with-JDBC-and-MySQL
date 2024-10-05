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

public class accountantUserServlet extends HttpServlet{
    private Connection connection;
    private CallableStatement statement;
    private boolean result; // true if resulstset, false if integer

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
    
        String command = "";
        String userCommand = request.getParameter("operation");

        try{
            getDBConnection();

            if(userCommand != null){
            switch (userCommand) {
                case "supMaxStatus":
                    command = "{call Get_The_Maximum_Status_Of_All_Suppliers()}";
                    break;
                case "partTotalWeight":
                    command = "{call Get_The_Sum_Of_All_Parts_Weights()}";
                    break;
                case "totalShipments":
                    command = "{call Get_The_Total_Number_Of_Shipments()}";
                    break;
                case "jobMaxWorkers":
                    command = "{call Get_The_Name_Of_The_Job_With_The_Most_Workers()}";
                    break;
                case "listSuppliers":
                    command = "{call List_The_Name_And_Status_Of_All_Suppliers()}";
                    break;
                default:
                    command = "{call ERROR()}";
                    break;
            }
        } else{
            String message = "Please select an operation to perform.";
            HttpSession session = request.getSession();
            session.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/accountantHome.jsp");
            dispatcher.forward(request, response);
        }

        if(command != null){
                statement = connection.prepareCall(command);
                result = statement.execute(command);
            
            statement = connection.prepareCall(command);
            result = statement.execute(command);

            if(result){ //checks if result set is returned
                ResultSet resultSet = statement.getResultSet();
                String htmlTable = ResultSetToHTMLFormatter.getHtmlRows(resultSet);
                
                HttpSession session = request.getSession();
                session.setAttribute("resultTable", htmlTable);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/accountantHome.jsp");
                dispatcher.forward(request, response);
            }
            else{
                String message = "Error executing command. Please try again.";
                HttpSession session = request.getSession();
                session.setAttribute("message", message);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/accountantHome.jsp");
                dispatcher.forward(request, response);
            }
        }

        } catch(SQLException e){
            e.printStackTrace();
            String message = "Error executing the SQL statement: " + e.getMessage();
            HttpSession session = request.getSession();
            session.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/accountantHome.jsp");
            dispatcher.forward(request, response);
        }

    }

    private void getDBConnection(){
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;

        //read system app properties file
        try{
            filein = new FileInputStream("/Library/Tomcat10126/webapps/Project-3/WEB-INF/lib/theaccountant.properties");
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
