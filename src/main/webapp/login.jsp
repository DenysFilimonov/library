<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
     <meta name="viewport" content="width=device-width, initial-scale=1">
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
     <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin="anonymous"></script>
     <link rel="stylesheet" href="main.css">
    <title>Login</title>
</head>
<body>
    <%@include file="includes/header.jsp" %>
    <div class="container-md w-50 text-center">
        <form method="POST">
            <div class="center">
                <label for="login" class="form-label">Login</label>
                <input type="text" class="form-control" id="login" aria-describedby="loginHelp" name="login">
                <div id="loginHelp" class="form-text">Input login, be carefully - it is case sensitive</div>
            </div>
            <div class="center">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password">
                <br>
            </div>
            <c:if test="${sessionScope.error != null}">
                <div class="center">
                    <p class = "error">User with these credentials doesn't exist</p>
                </div>
            </c:if>
            <input type="submit" class="btn btn-primary">
        </form>
    </div>
</body>


</html>