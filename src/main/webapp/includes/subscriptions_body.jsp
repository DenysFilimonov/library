<c:set var="class_"
       value="${not empty user ? 'bookTableUser' : 'bookTableGuest'}"
       scope="session" />
<div class="container-md col-9 text-center">
<div class="container col-12 bgColor rounded h4 text-center white">
    <fmt:message key="subscription.label.header" />
</div>
    <div class="subscriptionTable">
        <div class="tableHeader">
            <fmt:message key="subscriptions.label.title"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="subscriptions.label.author"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="subscriptions.label.status"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="subscriptions.label.issueType"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="subscriptions.label.issueDate"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="subscriptions.label.targetDate"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="subscriptions.label.returnDate"/>
        </div>
        <div class="tableHeader">
            <fmt:message key="subscriptions.label.fine"/>
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
                    class="btn btn-secondary btn-sm ms-3"
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
        urlParams.set('command', 'cancelOrder')
        urlParams.set('orderId', orderId);
        window.location.search = urlParams;
    }

</script>