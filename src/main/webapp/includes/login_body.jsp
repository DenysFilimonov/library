<nav class="navbar navbar-expand-lg bg-light">
       <div class="container-fluid">
         <a class="navbar-brand" href="#">Navbar</a>
       </div>
       <div>
               <img src="login.svg"
                    <c:if test="${user != null}">
                       <c:out value="class = blueLogo" />
                    </c:if>
                    <c:if test="${user == null}">
                       <c:out value="class = greyLogo" />
                    </c:if>
               >
       </div>
   </nav>
   <br>