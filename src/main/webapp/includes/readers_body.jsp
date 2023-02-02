<div class="container-md col-9 text-center">
<div class="container col-12 bgColor rounded ">
      <div class="h4 text-center white">
        <fmt:message key="returnBook.label.header" />
      </div>
</div>
    <form method = "POST">
        <div class="row">
            <div class="col">
                <div class="input-group pb-2">
                       <div class="input-group-text">
                        <fmt:message key="readers.label.title"/>
                       </div>
                       <input type="text" class="form-control form-control-sm"
                       value = "${not empty param.title? param.title: ''}"
                       name  = "title" placeholder=<fmt:message key="readers.label.title"/>>
                       <div class="input-group-text">
                       <fmt:message key="catalog.label.reader"/>
                       </div>
                       <input type="text" class="form-control form-control-sm"
                       value = "${not empty param.reader? param.reader: ''}"
                       name = "reader" placeholder=<fmt:message key="reader.label.reader"/>>
                      <div class="input-group-text">
                       <input type="submit" class="btn btn-primary btn-sm" value=<fmt:message key="login.label.find"/>>
                      </div>
                </div>
            </div>

        </div>
        </form>


    <div class="readersTable">
        <div class="tableHeader">
            <fmt:message key="reader.label.reader"/>
            <div class ="arrows">
                <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('reader', 'asc');"/>
                <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('reader', 'desc');"/>
            </div>
        </div>
        <div class="tableHeader">
            <fmt:message key="orders.label.title"/>
              <div class ="arrows">
                     <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('${language=='en'?'title':'title_ua'}', 'asc');"/>
                     <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('${language=='en'?'title':'title_ua'}', 'desc');"/>
             </div>
        </div>
        <div class="tableHeader">
            <fmt:message key="orders.label.author"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="orders.label.issueType"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="reader.label.issueDate"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="orders.label.targetDate"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="reader.label.fine"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="reader.label.returnBook"/>
        </div>

        <c:forEach items="${usersBooks}" var="userBook">
            <div class='tableCell'>
                ${users[userBook.userId].firstName} ${users[userBook.userId].secondName}
            </div>
            <div class='tableCell'>
                ${userBook.title[language]}
            </div>
             <div class='tableCell'>
                ${userBook.author[language]}
            </div>
            <div class='tableCell'>
                ${userBook.issueType.issueType[language]}
            </div>
            <div class='tableCell'>
               <ex:DateFormat date="${userBook.issueDate}"/>
            </div>
            <div class='tableCell'>
               <ex:DateFormat date="${userBook.targetDate}"/>
            </div>
            <div class='tableCell'>
               ${userBook.getFineDays()}
            </div>
            <div class='tableCell'>
                <a class="btn btn-primary btn-sm" href="javascript: returnBook(${userBook.id})"><fmt:message key="orders.label.return"/></a>
            </div>
        </c:forEach>
    </div>
    <%@include file="pagination.jsp" %>
</div>

<script type="text/javascript">

    function returnBook(orderId){
        const urlParams = new URLSearchParams(window.location.search);
        urlParams.set('returnBook', orderId);
        console.log(urlParams);
        window.location.search = urlParams;
    }

     function sortFunction(sort, order){
           const urlParams = new URLSearchParams(window.location.search);
           urlParams.set('sort', sort);
           urlParams.set('order', order);
           console.log(urlParams);
           window.location.search = urlParams;
        }


</script>