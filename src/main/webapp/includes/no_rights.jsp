<c:set var="display"
 value="${ not empty errorMessages[oldPassword]  ? 'block': 'none' }"
 scope="request" />

<div class="container col-4 bgColor rounded white p-3 text-center">
    <h4>
    <fmt:message key="command.label.noRights" />
    </h4>
    <div style="margin: 0 auto;">
    <br>



</div>
