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

public class ClientUserServlet extends HttpServlet {
    private Connection connection;
    private Statement statement; // send commands to DB
    private int mysqlUpdateValue = 0; // value returned by updating command
    private int businessLogicVal = 0;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // get client user command from html/jsp front-end
        String sqlStatement = request.getParameter("sqlStatement"); // inbound user command
        String outboundMessage = ""; // outbound response

        try {
            getDBConnection();
            statement = connection.createStatement();

            String inBoundCommand = sqlStatement.trim();
            if (inBoundCommand.toLowerCase().startsWith("select") || inBoundCommand.toLowerCase().startsWith("insert") || inBoundCommand.toLowerCase().startsWith("update") || inBoundCommand.toLowerCase().startsWith("delete") || inBoundCommand.toLowerCase().startsWith("replace") ) {
                // execute JDBC query/update and get resultset + update value back
                if (inBoundCommand.toLowerCase().startsWith("select")) {
                    ResultSet resultSet = statement.executeQuery(inBoundCommand);

                    String htmlTable = ResultSetToHTMLFormatter.getHtmlRows(resultSet);

                    HttpSession session = request.getSession();
                    session.setAttribute("resultTable", htmlTable);

                    // Forward to the results page (clientHome.jsp) where the table will be displayed
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
                    dispatcher.forward(request, response);
                } else {
                    // brute force business logic
                    if (inBoundCommand.contains("shipments")) {
                        int quantityVal = 0;
                        try {
                            if (inBoundCommand.contains("insert")) {

                                // Find the position of the last comma
                                int lastCommaIndex = inBoundCommand.lastIndexOf(',');

                                // Extract quantity
                                if (lastCommaIndex != -1) {
                                    String quantity = inBoundCommand.substring(lastCommaIndex + 1).trim();

                                    // Remove the trailing `);`
                                    if (quantity.endsWith(");")) {
                                        quantity = quantity.substring(0, quantity.length() - 2).trim();
                                    }

                                    quantityVal = Integer.parseInt(quantity);

                                } else {
                                    String message = "Incorrect input. Try again";
                                    HttpSession session = request.getSession();
                                    session.setAttribute("message", message);
                                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
                                    dispatcher.forward(request, response);
                                    return; // Exit the method if input is incorrect
                                }

                                if (quantityVal >= 100) {
                                    try {
                                        mysqlUpdateValue = statement.executeUpdate(inBoundCommand);
                                        String businessLogicUpdate = "update suppliers set status = status + 5 where snum in (select snum from shipments where quantity >= 100)";
                                        businessLogicVal = statement.executeUpdate(businessLogicUpdate);
                                        String updateMessage = ("<span style='color:green;'>The statement executed successfully. " + mysqlUpdateValue + " row(s) affected.<br>Business Logic Detected! Business Logic updated " + businessLogicVal + " supplier status marks</span>");
                                        HttpSession session = request.getSession();
                                        session.setAttribute("message", updateMessage);
                                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
                                        dispatcher.forward(request, response);
                                    } catch (SQLException e) {
                                        throw new SQLException(e.getMessage());
                                    }
                                } else {
                                    try {
                                        mysqlUpdateValue = statement.executeUpdate(inBoundCommand);
                                        String businessLogicUpdate = "update suppliers set status = status + 5 where snum in (select snum from shipments where quantity >= 100)";
                                        businessLogicVal = statement.executeUpdate(businessLogicUpdate);
                                        String updateMessage = ("<span style='color:green;'>The statement executed successfully. " + mysqlUpdateValue + " row(s) affected.<br>Business Logic Not Detected.</span>");
                                        HttpSession session = request.getSession();
                                        session.setAttribute("message", updateMessage);
                                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
                                        dispatcher.forward(request, response);
                                    } catch (SQLException e) {
                                        throw new SQLException(e.getMessage());
                                    }
                                }

                            } else if (inBoundCommand.contains("update")) {
                                try {
                                    mysqlUpdateValue = statement.executeUpdate(inBoundCommand);
                                    String businessLogicUpdate = "update suppliers set status = status + 5 where snum in (select snum from shipments where quantity >= 100)";
                                    businessLogicVal = statement.executeUpdate(businessLogicUpdate);
                                    String updateMessage = ("<span style='color:green;'>The statement executed successfully. " + mysqlUpdateValue + " row(s) affected.<br>Business Logic Detected! Business Logic updated " + businessLogicVal + " supplier status marks</span>");
                                    HttpSession session = request.getSession();
                                    session.setAttribute("message", updateMessage);
                                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
                                    dispatcher.forward(request, response);
                                } catch (SQLException e) {
                                    throw new SQLException(e.getMessage());
                                }
                            } 
                        } catch (NumberFormatException e) {
                            String message = "Error parsing quantity value.";
                            HttpSession session = request.getSession();
                            session.setAttribute("message", message);
                            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
                            dispatcher.forward(request, response);
                        }
                    } else {
                        try {
                            mysqlUpdateValue = statement.executeUpdate(inBoundCommand);
                            String updateMessage = ("<span style='color:green;'>The statement executed successfully. " + mysqlUpdateValue + " row(s) affected.<br>Business Logic Not Detected.</span>");
                            HttpSession session = request.getSession();
                            session.setAttribute("message", updateMessage);
                            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
                            dispatcher.forward(request, response);
                        } catch (SQLException e) {
                            throw new SQLException(e.getMessage());
                        }
                }
            }
            } /*else {
                outboundMessage = ("Please enter a valid SQL statement.");
                HttpSession session = request.getSession();
                session.setAttribute("message", outboundMessage);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
                dispatcher.forward(request, response);
            }*/
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            String message = "Error executing the SQL statement: " + e.getMessage();
            HttpSession session = request.getSession();
            session.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
            dispatcher.forward(request, response);
        } catch (IOException e){
            String message = "Error executing the SQL statement: " + e.getMessage();
            HttpSession session = request.getSession();
            session.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/clientHome.jsp");
            dispatcher.forward(request, response);
        }
    } // end doPost()

    private void getDBConnection() {
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;

        // read system app properties file
        try {
            filein = new FileInputStream("/Library/Tomcat10126/webapps/Project-3/WEB-INF/lib/client.properties");
            properties.load(filein);
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (filein != null) filein.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // end getDBConnection()
}
