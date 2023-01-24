<div class="container-md w-60 text-center">
        <form method="POST">
            <div class="center">
                <label for="login" class="form-label"><fmt:message key="login.label.username" /></label>
                <input type="text" class="form-control" id="login" aria-describedby="loginHelp" name="login">
                <div id="loginHelp" class="form-text"><fmt:message key="login.label.login_tip" /></div>
            </div>
            <div class="center">
                <label for="password" class="form-label"><fmt:message key="login.label.password"/></label>
                <input type="password" class="form-control" id="password" name="password">
                <br>
            </div>
            <c:if test="${errorMessage != null}">
                <div class="center">
                    <p class = "error"><fmt:message key="login.label.error"/></p>
                </div>
            </c:if>
            <input type="submit" class="btn btn-primary" value=<fmt:message key="login.label.button"/>>
        </form>

        <div class="center">
            <a href="controller?command=register"><fmt:message key="login.label.register"/></a>
        </div>

    </div>