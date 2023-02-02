<div class="container-md col-9 text-center">
 <div class="container col-12 bgColor rounded ">
      <div class="h4 text-center white">
        <fmt:message key="IssueBook.label.header" />
      </div>
</div>
    <div class="ordersTable">
        <div class="tableHeader">
            <fmt:message key="orders.label.user"/>
            <div class ="arrows">
                <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('reader', 'asc');"/>
                <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('reader', 'desc');"/>
            </div>
        </div>
        <div class="tableHeader">
            <fmt:message key="orders.label.title"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="orders.label.author"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="orders.label.issueType"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="orders.label.bookStore"/>
            <div class ="arrows">
                <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('case_num', 'asc');"/>
                <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('case_num', 'desc');"/>
            </div>
        </div>
        <div class="tableHeader">
            <fmt:message key="orders.label.issueOrder"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="orders.label.cancelOrder"/>
        </div>

        <c:forEach items="${usersBooks}" var="book">
            <div class='tableCell'>
                ${users[book.userId].firstName} ${users[book.userId].secondName}

            </div>
            <div class='tableCell'>
                ${book.title[language]}
            </div>
            <div class='tableCell'>
                ${book.author[language]}
            </div>
            <div class='tableCell'>
                ${book.issueType.issueType[language]}
            </div>
            <div class='tableCell'>
                ${book.bookStore.caseNum}/${book.bookStore.shelfNum}/${book.bookStore.cellNum}
            </div>
            <div class='tableCell'>
                <a class="btn btn-primary btn-sm" href="javascript: issueOrder(${book.id})"><fmt:message key="orders.label.issue"/></a>
            </div>
            <div class='tableCell'>
                <a class="btn btn-primary btn-sm" href="javascript: cancelOrder(${book.id})"><fmt:message key="orders.label.cancel"/></a>
            </div>
        </c:forEach>
    </div>
</div>

<script type="text/javascript">
    function cancelOrder(orderId){
        const urlParams = new URLSearchParams(window.location.search);
        urlParams.set('cancelOrderId', orderId);
        console.log(urlParams);
        window.location.search = urlParams;
    }

    function issueOrder(orderId){
        const urlParams = new URLSearchParams(window.location.search);
        urlParams.set('userBookId', orderId);
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