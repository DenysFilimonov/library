<div class="container-md w-60 text-center">
    <div class="usersTable">
         <div class="tableHeader">
            <fmt:message key="users.label.login"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="users.label.firstName"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="users.label.secondName"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="users.label.email"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="users.label.email"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="users.label.status"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="subscriptions.label.changeStatus"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="subscriptions.label.role"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="subscriptions.label.changeRole"/>
        </div>


        <c:forEach items="${usersBooks}" var="book">
            <div class='tableCell'>
                ${book.title[language]}
            </div>
            <div class='tableCell'>
                ${book.author[language]}
            </div>
            <div class='tableCell'>
                ${book.status.status[language]}
                <c:if test="${book.status.status['en']=='order'}">
                <button
                    class="btn btn-secondary btn-sm"
                    onClick="cancelOrder(${book.id});"
                ><fmt:message key="subscriptions.label.cancel"/></button>
                </c:if>
            </div>
            <div class='tableCell'>
                ${book.issueType.issueType[language]}
            </div>
            <div class='tableCell'>
                ${book.issueDate}
            </div>
            <div class='tableCell'>
                ${book.targetDate}
            </div>
            <div class='tableCell'>
                ${book.returnDate}
            </div>
            <div class='tableCell'>
                ${book.getFineDays()}
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

</script>