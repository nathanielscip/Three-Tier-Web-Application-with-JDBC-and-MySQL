<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Data Entry Application</title>
    <style>
        body {
            background-color: black;
            color: white;
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
        .insert-field {
            margin: 20px auto;
            padding: 20px;
            border: 3px lightblue;
            width: 80%;
            background-color: black;
        }
        .insert-field h3 {
            color: lightblue;
            margin-bottom: 20px;
        }
        .insert-row {
            display: flex;
            justify-content: center;
            margin-bottom: 20px;
        }
        .insert-row div {
            margin: 0 10px;
            border: 3px lightblue;
            padding: 10px;
        }
        label {
            display: block;
            font-size: 20px;
            margin-bottom: 5px;
        }
        input[type="text"] {
            width: 200px;
            padding: 10px;
            font-size: 18px;
        }
        .buttons {
            margin-top: 20px;
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
        function resetForm(formID) {
            formID.querySelectorAll('input[type="text"]').forEach(input => input.value = '');
            //form.querySelectorAll('textarea').forEach(textarea => textarea.value = '');
            clearResults();
        }
    </script>
</head>
<body>
    <h1>Welcome to the Summer 2024 Project 3 Enterprise System</h1>
    <h2>Data Entry Application</h2>
    <div class="subtitle">
        You are connected to the Project 3 Enterprise System database as a <span style="color: red;">data-entry-level</span> user.<br>
        Enter the data values in a form below to add a new record to the corresponding database table.
    </div>

    <!-- Suppliers -->
    <div class="insert-field">
        <h3>Suppliers Record Insert</h3>
        <form id = "supplier" action="insertSupplier" method="post">
            <div class="insert-row">
                <div>
                    <label for="snum">snum</label>
                    <input type="text" id="suSnum" name="suSnum" value="<%= request.getParameter("suSnum") != null ? request.getParameter("suSnum") : "" %>">
                </div>
                <div>
                    <label for="sname">sname</label>
                    <input type="text" id="sname" name="sname" value="<%= request.getParameter("sname") != null ? request.getParameter("sname") : "" %>">
                </div>
                <div>
                    <label for="status">status</label>
                    <input type="text" id="status" name="status" value="<%= request.getParameter("status") != null ? request.getParameter("status") : "" %>">
                </div>
                <div>
                    <label for="city">city</label>
                    <input type="text" id="suCity" name="suCity" value="<%= request.getParameter("suCity") != null ? request.getParameter("suCity") : "" %>">
                </div>
            </div>
            <div class="buttons">
                <button type="submit">Enter Supplier Record Into Database</button>
                <button type="button" class="clear" onclick="resetForm(supplier)">Clear Data and Results</button>
            </div>
        </form>
    </div>

    <!-- Parts -->
    <div class="insert-field">
        <h3>Parts Record Insert</h3>
        <form id="partForm" action="insertPart" method="post">
            <div class="insert-row">
                <div>
                    <label for="pnum">pnum</label>
                    <input type="text" id="paPnum" name="paPnum" value="<%= request.getParameter("paPnum") != null ? request.getParameter("paPnum") : "" %>">
                </div>
                <div>
                    <label for="pname">pname</label>
                    <input type="text" id="pname" name="pname" value="<%= request.getParameter("pname") != null ? request.getParameter("pname") : "" %>">
                </div>
                <div>
                    <label for="color">color</label>
                    <input type="text" id="color" name="color" value="<%= request.getParameter("color") != null ? request.getParameter("color") : "" %>">
                </div>
                <div>
                    <label for="weight">weight</label>
                    <input type="text" id="weight" name="weight" value="<%= request.getParameter("weight") != null ? request.getParameter("weight") : "" %>">
                </div>
                <div>
                    <label for="city">city</label>
                    <input type="text" id="paCity" name="paCity" value="<%= request.getParameter("paCity") != null ? request.getParameter("paCity") : "" %>">
                </div>
            </div>
            <div class="buttons">
                <button type="submit">Enter Part Record Into Database</button>
                <button type="button" class="clear" onclick="resetForm(partForm)">Clear Data and Results</button>
            </div>
        </form>
    </div>

    <!-- Jobs -->
    <div class="insert-field">
        <h3>Jobs Record Insert</h3>
        <form id="job" action="insertJob" method="post">
            <div class="insert-row">
                <div>
                    <label for="jnum">jnum</label>
                    <input type="text" id="jJnum" name="jJnum" value="<%= request.getParameter("jJnum") != null ? request.getParameter("jJnum") : "" %>">
                </div>
                <div>
                    <label for="jname">jname</label>
                    <input type="text" id="jname" name="jname" value="<%= request.getParameter("jname") != null ? request.getParameter("jname") : "" %>">
                </div>
                <div>
                    <label for="numworkers">numworkers</label>
                    <input type="text" id="numworkers" name="numworkers" value="<%= request.getParameter("numworkers") != null ? request.getParameter("numworkers") : "" %>">
                </div>
                <div>
                    <label for="city">city</label>
                    <input type="text" id="jCity" name="jCity" value="<%= request.getParameter("jCity") != null ? request.getParameter("jCity") : "" %>">
                </div>
            </div>
            <div class="buttons">
                <button type="submit">Enter Job Record Into Database</button>
                <button type="button" class="clear" onclick="resetForm(job)">Clear Data and Results</button>
            </div>
        </form>
    </div>

    <!-- Shipments -->
    <div class="insert-field">
        <h3>Shipments Record Insert</h3>
        <form id="shipment" action="insertShipment" method="post">
            <div class="insert-row">
                <div>
                    <label for="shSnum">snum</label>
                    <input type="text" id="shSnum" name="shSnum" value="<%= request.getParameter("shSnum") != null ? request.getParameter("shSnum") : "" %>">
                </div>
                <div>
                    <label for="shPnum">pnum</label>
                    <input type="text" id="shPnum" name="shPnum" value="<%= request.getParameter("shPnum") != null ? request.getParameter("shPnum") : "" %>">
                </div>
                <div>
                    <label for="shJnum">jnum</label>
                    <input type="text" id="shJnum" name="shJnum" value="<%= request.getParameter("shJnum") != null ? request.getParameter("shJnum") : "" %>">
                </div>
                <div>
                    <label for="quantity">quantity</label>
                    <input type="text" id="quantity" name="quantity" value="<%= request.getParameter("quantity") != null ? request.getParameter("quantity") : "" %>">
                </div>
            </div>
            <div class="buttons">
                <button type="submit">Enter Shipment Record Into Database</button>
                <button type="button" class="clear" onclick="resetForm(shipment)">Clear Data and Results</button>
            </div>
        </form>
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

