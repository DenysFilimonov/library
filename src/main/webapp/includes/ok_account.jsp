<c:set var="display"
 value="${ not empty errorMessages[oldPassword]  ? 'block': 'none' }"
 scope="request" />

<div class="container bgColor rounded p-3 col-4 white text-center">
    <h4>
    <fmt:message key="account.label.okUpdate" />
    </h4>
    <div style="margin: 0 auto;">
    <br>
    <a class="btn btn-primary" href="controller?command=account">
    <fmt:message key="account.label.ok" />
    </a>


</div>
