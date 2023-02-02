<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix = "ex" uri = "WEB-INF/custom.tld"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tf" %>

 <c:set var="language"
 value="${not empty param.language ? param.language : not empty language ? language : 'en'}"

 scope="session" />
 <c:set var="languageAlter"
  value="${language == 'en'? 'ua' : 'en'}"

  scope="session" />
 <fmt:setLocale value="${language}" />
 <fmt:setBundle basename="text" />
<!DOCTYPE html>

<script language="javascript">

window.onload = function(e){
    console.log("window.onload");
    console.log('${commandUrl}');
    console.log('${language}')
    window.history.pushState('page2', 'Library', '${commandUrl}');
    if(sessionStorage.getItem("tab"))  {
        console.log(!sessionStorage.getItem("tab"));
        document.getElementById(sessionStorage.getItem("tab")).click();
        sessionStorage.removeItem("tab");
    }
    try{
        document.getElementById("${wrongBook}").click();
    }catch(e){
    }
}

</script>


<html lang="${language}">
<head>
    <meta charset="UTF-8">
     <meta name="viewport" content="width=device-width, initial-scale=1">
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
     <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
     <link rel="stylesheet" href="main.css">
    <title>Library project</title>
</head>
<body>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>


<nav class="navbar navbar-expand-lg bg-light">
       <div class="container-fluid">
         <%@include file="user_menu.jsp"%>
       </div>

       <div class="userLogo">
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
                <div>
                    <ex:UserName user="${user}"/>
                </div>
       </div>
   </nav>
   <br>