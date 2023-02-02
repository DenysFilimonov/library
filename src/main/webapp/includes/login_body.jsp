<div class="container-md col-4 text-center rounded p-3 bgColor">
<div class="h4 text-center white">
    <fmt:message key="login.label.header" />
</div>
        <form method="POST">
            <div class="center">
                <label for="login" class="form-label w6"><fmt:message key="login.label.username" /></label>
                <input type="text" class="form-control" id="login" aria-describedby="loginHelp" name="login">
            </div>
            <div class="center">
                <label for="password" class="form-label w6"><fmt:message key="login.label.password"/></label>
                <input type="password" class="form-control" id="password" name="password">
            </div>
            <c:if test="${errorMessage != null}">
                <div class="center">
                    <p class = "error">${errorMessage[language]}</p>
                </div>
            </c:if>
            <input type="submit" class="btn btn-primary mt-3 mb-1" value=<fmt:message key="login.label.button"/>>
        </form>
        <div class="center">
            <a class="text-dark" href="controller?command=register"><fmt:message key="login.label.register"/></a>
        </div>

    </div>