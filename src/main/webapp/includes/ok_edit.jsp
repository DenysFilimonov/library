<c:set var="display"
 value="${ not empty errorMessages[oldPassword]  ? 'block': 'none' }"
 scope="request" />

<div class="container col-4 bgColor white rounded p-3 text-center">
    <h4>
    <fmt:message key="account.label.okEdit" />
    </h4>
    <div style="margin: 0 auto;">
    <br>
    <a class="btn btn-primary" href="controller?command=booksManager">
    <fmt:message key="account.label.ok" />
    </a>


</div>
