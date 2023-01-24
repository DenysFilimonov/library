<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 <c:set var="language"
 value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
 scope="session" />
 <fmt:setLocale value="${language}" />
 <fmt:setBundle basename="text" />
<!DOCTYPE html>

<script language="javascript">

window.onload = function(e){
    console.log("window.onload");
    console.log('${commandUrl}');
    window.history.pushState('page2', 'Title', '${commandUrl}');
}

</script>


<html lang="${language}">
<head>
    <meta charset="UTF-8">
     <meta name="viewport" content="width=device-width, initial-scale=1">
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
     <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
     <link rel="stylesheet" href="main.css">
    <title>Login page</title>
</head>
<body>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>



<nav class="navbar navbar-expand-lg bg-light">
       <div class="container-fluid">
         <a class="navbar-brand" href="#">Navbar</a>
       </div>
      <div>
               <form method = "post" action= "${url}" >
                   <select id="language" name="language" onchange="submit()">
                       <option value="en" ${language == 'en' ? 'selected' : ''}>EN</option>
                       <option value="ua" ${language == 'ua' ? 'selected' : ''}>UA</option>
                   </select>
               </form>
      </div>
       <div>
       <c:if test ="${user==null}">
            <a href="controller?command=login">
       </c:if>
       <c:if test ="${user!=null}">
            <a href="controller?command=logout">
       </c:if>
               <img src="SVG/login.svg"
                    <c:if test="${user != null}">
                       <c:out value="class = blueLogo" />
                    </c:if>
                    <c:if test="${user == null}">
                       <c:out value="class = greyLogo" />
                    </c:if>
               >
       </a>
       </div>
   </nav>
   <br>