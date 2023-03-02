<div class="container-md col-4 text-center rounded p-3 bgColor">
<div class="h4 text-center white">
    <fmt:message key="restore.label.header" />
</div>
        <form method="POST">
            <div class="input-group pt-1">
                   <div class="input-group-text w15">
                        <label for="password" class="form-label"><fmt:message key="register.label.password"/></label>
                   </div>

                   <input type="password"
                               class="form-control rounded-right"
                               id="password"
                               name="password"
                               required
                               onInput = "comparePassword();"
                               value="${params.password}"
                               />
            </div>
            <div class="input-group pt-1">
                   <div class="input-group-text w15">
                        <label for="passwordConfirmation" class="form-label"><fmt:message key="register.label.passwordConfirm"/></label>
                   </div>

                   <input type="password"
                         class="form-control rounded-right"
                         id="passwordConfirmation"
                         name="passwordConfirmation"
                         required
                         onInput = "comparePassword()";
                         value="${params.passwordConfirmation}";
                         />
            </div>
            <div class="input-group pt-1">
                     <span id="passwordError"
                           class = "error"
                           style="display:none"><fmt:message key="register.label.passwordError"/></span>

                            <c:if test="${errorMessage != null}">
                            <div class="center">
                                <p class = "error"><fmt:message key="register.label.error"/></p>
                            </div>
                        </c:if>
                </div>
            <input type="submit" id ='userFormSubmitButton' class="btn btn-primary mt-2 mb-1" value=<fmt:message key="restore.label.change"/>>
        </form>
    </div>

    <script type="text/javascript">
        function comparePassword() {
            var password = document.getElementById('password').value;
            var passwordCheck = document.getElementById('passwordConfirmation').value;
            if (password!=passwordCheck) {
                document.getElementById('passwordError').style = "";
                document.getElementById('userFormSubmitButton').classList.add("disabled");
            }
            else{
                document.getElementById('passwordError').style = "display:none";
                document.getElementById('userFormSubmitButton').classList.remove("disabled");
            }
        }
    </script>