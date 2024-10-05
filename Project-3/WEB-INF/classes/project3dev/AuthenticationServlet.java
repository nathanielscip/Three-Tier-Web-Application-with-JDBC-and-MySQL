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
import javax.swing.JOptionPane;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;


public class AuthenticationServlet extends HttpServlet {
    private Connection connection = null; // connects to mysql dbms
    private ResultSet lookupResult; // holds response from search of usercredentials table;
    private PreparedStatement pStatement;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String inBoundUsername = request.getParameter("username");
        String inBoundPassword = request.getParameter("password");
        String credentialsSearchQuery = "select * from usercredentials where login_username = ? and login_password = ?";
        String user = null;

        try{
            getDBConnection(); //connects to credentialsDB

            pStatement = connection.prepareStatement(credentialsSearchQuery);
            pStatement.setString(1, inBoundUsername);
            pStatement.setString(2, inBoundPassword);
            lookupResult = pStatement.executeQuery();

            if (lookupResult.next()) {
                user = lookupResult.getString("login_username");
            }
            lookupResult.close();
            pStatement.close();
            connection.close();
            
        } catch(SQLException sqlException){
            JOptionPane.showMessageDialog(null, "Error: SQL Exception", "MAJOR ERROR- ERROR", JOptionPane.ERROR_MESSAGE);
        }

        if(user != null){
            // redirect to correct html page

            switch (user){
                case "root":
                    response.sendRedirect("/Project-3/rootHome.jsp");
                    break;
                case "client":
                    response.sendRedirect("/Project-3/clientHome.jsp");
                    break;
                case "dataentryuser":
                    response.sendRedirect("/Project-3/dataentryHome.jsp");
                    break;
                case "theaccountant":
                    response.sendRedirect("/Project-3/accountantHome.jsp");
                    break;
                default: 
                    response.sendRedirect("/Project-3/errorPage.html");
                    break;
                
            }
        }
        else{
            response.sendRedirect("/Project-3/errorPage.html");
        }

    } //end doGet()

    // getDBConnection() method establishes connection to credentialsDB database. Method is 
    // executed on behalf of system-level application with root user privileges

    private void getDBConnection(){
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;

        //read system app properties file
        try{
            filein = new FileInputStream("/Library/Tomcat10126/webapps/Project-3/WEB-INF/lib/systemapp.properties");
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
    } // end getDBConnection()

} // end AuthenticationServlet class
