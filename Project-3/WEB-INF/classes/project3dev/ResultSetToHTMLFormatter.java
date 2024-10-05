/*
Name: Nathaniel Scipio
Course: CNT 4714 Summer 2024
Assignment title: Project 3 – Developing A Three-Tier Distributed Web-Based Application
Date: August 1, 2024
Class: CNT 4714
*/

package project3dev;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetToHTMLFormatter {

    public static synchronized String getHtmlRows(ResultSet results) throws SQLException {
        StringBuffer htmlRows = new StringBuffer();

        ResultSetMetaData metaData = results.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Set table header row
        htmlRows.append("<table border='1' style='border-collapse:collapse;width:75%;text-align:center;margin:auto;'>");
        htmlRows.append("<thead><tr>");
        for (int i = 1; i <= columnCount; i++) {
            htmlRows.append("<th style='padding:8px;'>").append(metaData.getColumnName(i)).append("</th>");
        }
        htmlRows.append("</tr></thead>");

        // Set remainder of the table - row by row
        htmlRows.append("<tbody>");
        int rowCount = 0;
        while (results.next()) { // iterate through each row for zebra coloring
            // use modulo to color every other row gray
            if (rowCount % 2 == 0) {
                htmlRows.append("<tr style='background-color:lightgray;'>");
            } else {
                htmlRows.append("<tr style='background-color:white;'>");
            }
            for (int i = 1; i <= columnCount; i++) {
                htmlRows.append("<td style='padding:8px;color:black;'>").append(results.getString(i)).append("</td>");
            }
            htmlRows.append("</tr>");
            rowCount++;
        }
        htmlRows.append("</tbody></table>");

        // Convert htmlRows object to a string and return to caller
        return htmlRows.toString();
    }
}
