<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Accountant Application</title>
    <style>
        body {
            background-color: black;
            color: lightblue;
            font-family: Arial, sans-serif;
            text-align: center;
            font-size: 20px;
            padding-top: 20px;
        }
        h1 {
            color: lightblue;
            font-size: 36px;
        }
        h2 {
            color: white;
            font-size: 28px;
        }
        .subtitle {
            color: white;
            font-size: 20px;
            margin-top: 20px;
        }
        .operation-list {
            background-color: darkgray;
            padding: 20px;
            margin: 20px auto;
            width: 80%;
            border-radius: 10px;
            text-align: left;
        }
        .operation-list ul {
            list-style-type: none;
            padding: 0;
            color: white;
        }
        .operation-list li {
            margin: 10px 0;
            font-size: 20px;
            display: flex;
            align-items: center;
        }
        .operation-list input[type="radio"] {
            margin-right: 10px;
        }
        .buttons {
            margin: 20px;
        }
        .buttons button {
            background-color: green;
            color: black;
            padding: 15px 30px;
            margin: 5px;
            border: none;
            cursor: pointer;
            font-size: 20px;
        }
        .buttons button.clear {
            background-color: red;
        }
        .buttons button:hover {
            opacity: 0.8;
        }
        .line {
            border-top: 5px solid white;
            margin: 20px 0;
        }
        .results {
            color: white;
            margin-top: 40px;
            font-size: 24px;
        }
        .desc {
            margin-left: 10px; /* Adjust the margin as needed */
        }
        .error-message {
            color: red;
            font-weight: bold;
            margin-top: 20px;
        }

    </style>
    <script>
        function clearResults() {
            document.querySelector('.results').innerHTML = '';
            document.querySelector('.error-message').innerHTML = '';
        }
    </script>
</head>
<body>
    <h1>Welcome to the Summer 2024 Project 3 Enterprise System</h1>
    <h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>
    <div class="subtitle">
        You are connected to the Project 3 Enterprise System database as an <span style="color: red;">accountant-level</span> user.<br>
        Please select the operation you would like to perform from the list below.
    </div>
    <div class="line"></div>

    <div class="operation-list">
        <form action="accountant" method="post">
            <ul>
                <li><input type="radio" name="operation" value="supMaxStatus"> <span style="color: rebeccapurple;">Get The Maximum Status Value Of All Suppliers </span><span class="desc"> (Returns a maximum value)</span></li>
                <li><input type="radio" name="operation" value="partTotalWeight"> <span style="color: rebeccapurple;">Get The Total Weight Of All Parts </span> <span class="desc">(Returns a sum)</span></li>
                <li><input type="radio" name="operation" value="totalShipments"> <span style="color: rebeccapurple;">Get The Total Number of Shipments </span><span class="desc">(Returns the current number of shipments in total)</span></li>
                <li><input type="radio" name="operation" value="jobMaxWorkers"> <span style="color: rebeccapurple;">Get The Name And Number Of Workers Of The Job With The Most Workers </span><span class="desc">(Returns two values)</span></li>
                <li><input type="radio" name="operation" value="listSuppliers"><span style="color: rebeccapurple;"> List The Name And Status Of Every Supplier</span><span class="desc">(Returns a list of supplier names with status)</span></li>
            </ul>
            <div class="buttons">
                <button type="submit">Execute Command</button>
                <button type="button" class="clear" onclick="clearResults()">Clear Results</button>
            </div>
        </form>
    </div>

    <div class="subtitle">
        All execution results will appear below this line.
    </div>
    <div class="line"></div>
    <div class="results">
        Execution Results:
        <br>
        <%
             String resultTable = (String) session.getAttribute("resultTable");
            String message = (String) session.getAttribute("message");
            if (resultTable != null) {
                out.println(resultTable);
                session.removeAttribute("resultTable");
            }
            if (message != null) {
                out.println("<div class='error-message'>" + message + "</div>");
                session.removeAttribute("message");
            }
        %>
    </div>
</body>
</html>
