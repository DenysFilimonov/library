<div class="container-md col-4 text-center rounded p-3 bgColor">
<div class="h4 text-center white">
    <fmt:message key="restore.label.header" />
</div>
        <form method="POST">
            <div class="center white">
                <fmt:message key="login.label.emailRestore"/>
                <input type="email" class="form-control mt-2" id="email" aria-describedby="loginHelp" name="email" value='${param.email}'>
            </div>
             <c:if test="${not empty errors}">
                <div class ="center mt-2 text-warning">
                     <c:forEach items="${errors.keySet()}" var="error">
                         ${errors[error][language]}
                     </c:forEach>
                </div>
             </c:if>
            <input type="submit" class="btn btn-primary mt-2 mb-1" value=<fmt:message key="login.label.button"/>>
        </form>
    </div>