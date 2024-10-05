<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Project 3 Enterprise System</title>
    <style>
        body {
            background-color: black;
            color: lightblue;
            font-family: Arial, sans-serif;
            text-align: center;
            font-size: 20px; /* Increased font size */
            padding-top: 20px;
        }
        h1 {
            color: lightblue;
            font-size: 36px; /* Increased font size */
        }
        h2 {
            color: white;
            font-size: 28px; /* Increased font size */
        }
        .subtitle {
            color: white;
            font-size: 20px;
            margin-top: 20px;
        }
        .input {
            background-color: blue;
            padding: 20px;
            margin: 20px auto;
            width: 80%;
            border-radius: 10px;
        }
        textarea {
            width: 100%;
            height: 350px;
            padding: 10px;
            font-size: 18px;
            margin-bottom: 20px;
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
        .buttons button.reset {
            background-color: red;
        }
        .buttons button.clear {
            background-color: gold;
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
        function resetForm() {
            document.querySelector('textarea[name="sqlStatement"]').value = '';
        }
    </script>
</head>
<body>
    <h1>Welcome to the Summer 2024 Project 3 Enterprise System</h1>
    <h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>
    <div class="line"></div>
    <div class="subtitle">
        You are connected to the Project 3 Enterprise System database as a <span style="color: red;">root-level</span> user.<br>
        Please enter any SQL query or update command in the box below.
    </div>
    <form action="root" method="post">
        <div class="input">
            <textarea name="sqlStatement"><%= (request.getParameter("sqlStatement") != null) ? request.getParameter("sqlStatement") : "" %></textarea></textarea>
            </div>
        <div class="buttons">
            <button type="submit">Execute Command</button>
            <button type="button" class="reset" onclick="resetForm()">Reset Form</button>
            <button type="button" class="clear" onclick="clearResults()">Clear Results</button>
        </div>
    </form>
    <div class="subtitle">All execution results will appear below this line.</div>
    <div class="line"></div>
    <div class="subtitle"><b>Execution Results:</b></div>
    <div class="results">
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