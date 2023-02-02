<div class="container-md col-8 text-center">

<form method = "POST">
   <div class="container col-12 bgColor rounded ">
      <div class="h4 text-center white">
        <fmt:message key="userManager.label.header" />
      </div>
   </div>
      <div class="row">
        <div class="col">
            <div class="input-group pb-2">

                  <input type="text"
                  id = "findUserStr"
                  name="name"
                  class="form-control" placeholder=<fmt:message key="userManager.label.user"/>>
                  <div class="input-group-text">
                    <a class = "btn btn-primary btn-sm"
                       id ="findAuthorButton"
                       href="javascript:findAuthor();">
                       <fmt:message key="userManager.label.search"/>
                    </a>
                  </div>
            </div>
        </div>
        <div class="col">
        </div>
      </div>


</form>


    <div class="tableUsers">
         <div class="tableHeader">
            <fmt:message key="users.label.login"/>
            <div class ="arrows">
                    <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('login', 'asc');"/>
                    <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('login', 'desc');"/>
            </div>
        </div>
        <div class="tableHeader">
            <fmt:message key="users.label.firstName"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="users.label.secondName"/>
            <div class ="arrows">
                    <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('second_name', 'asc');"/>
                    <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('second_name', 'desc');"/>
            </div>
        </div>
        <div class="tableHeader">
            <fmt:message key="users.label.email"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="users.label.phone"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="users.label.status"/>
            <div class ="arrows">
                <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('active', 'asc');"/>
                <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('active', 'desc');"/>
            </div>
        </div>
        <div class="tableHeader">
            <fmt:message key="users.label.role"/>
            <div class ="arrows">
                    <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('role', 'asc');"/>
                    <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('role', 'desc');"/>
            </div>
        </div>



        <c:forEach items="${users}" var="currentUser">
            <div class='tableCell'>
                ${currentUser.login}
            </div>
            <div class='tableCell'>
                ${currentUser.firstName}
            </div>
            <div class='tableCell'>
                ${currentUser.secondName}
            </div>
            <div class='tableCell'>
                ${currentUser.email}
            </div>
            <div class='tableCell'>
                ${currentUser.phone}
            </div>

            <div class='tableCell'>
                <a class=
                <c:if test="${currentUser.active == true}">
                    "btn btn-danger btn-sm"
                </c:if>
                <c:if test="${currentUser.active == false}">
                     "btn btn-primary btn-sm"
                </c:if>

                href="javascript: changeUserStatus(${currentUser.id}, ${currentUser.active});">
                <c:if test="${currentUser.active == true}">
                     <fmt:message key="users.label.deactivate"/>
                </c:if>
                <c:if test="${currentUser.active == false}">
                     <fmt:message key="users.label.activate"/>
                </c:if>
                </a>
            </div>

            <div class='tableCell'>
                 <select name="role"
                 id="${currentUser.id}"
                 onChange="changeRole('${currentUser.id}', '${currentUser.id}')"
                 class="form-control-sm"
                 >
                 <c:forEach items="${roles}" var="role" >
                    <option value="${role.id}"
                    <c:if test="${role.id==currentUser.role.id}">
                        selected="selected"
                    </c:if>
                    >${role.roleName[language]}</option>
                 </c:forEach>
                 </select>
            </div>
        </c:forEach>
    </div>
    <%@include file="pagination.jsp" %>

</div>

<script type="text/javascript">

     function setLinesOnPage(){
        const urlParams = new URLSearchParams(window.location.search);
        var lines = document.getElementById('linesOnPage').value;
        urlParams.set('linesOnPage', lines);
        window.location.search = urlParams;
    }

     function setPage(pageNum){
        const urlParams = new URLSearchParams(window.location.search);
        urlParams.set('page', pageNum);
        window.location.search = urlParams;
     }

     function sortFunction(sort, order){
        const urlParams = new URLSearchParams(window.location.search);
        urlParams.set('sort', sort);
        urlParams.set('order', order);
        console.log(urlParams);
        window.location.search = urlParams;
     }

      function changeRole(userId, id){
         const urlParams = new URLSearchParams(window.location.search);
         var e = document.getElementById(id);
         var value = e.options[e.selectedIndex].value;
         urlParams.set('setRole', value);

         urlParams.set('userId', userId);
         window.location.search = urlParams;
      }

      function changeUserStatus(userId, currentStatus){
        const urlParams = new URLSearchParams(window.location.search);
        urlParams.set('setActive', !currentStatus);
        urlParams.set('userId', userId);
        console.log(urlParams.get('setActive'));
        window.location.search = urlParams;
        }


</script>