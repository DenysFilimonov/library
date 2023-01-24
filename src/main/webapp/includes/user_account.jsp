<div class="container">
        <form method="POST">
        <div class="container">
            <div class="formRow">
                <div class="col">
                    <label for="login" class="form-label"><fmt:message key="register.label.login" /></label>
                </div>
                <div clas="col-9">
                    <input type="text"
                           class="form-control"
                           id="login"
                           name="login"
                           value = "${not empty user? user.login: not empty param.login? param.login: ''}"
                           required/>
                    <span class = "error">${errorMessages['login'][language]}</span>
                </div>
            </div>
            <div class="formRow">
                <div class = "col">
                    <label for="firstName" class="form-label"><fmt:message key="register.label.firstName" /></label>
                </div>
                <div clas="col-9">
                     <input type="text"
                     class="form-control"
                     id="firstName"
                     name="firstName"
                     value = "${not empty user? user.firstName: not empty param.firstName? param.firstName: ''}"
                     required/>
                    <span class = "error">${errorMessages['firstName'][language]}</span>
                </div>
            </div>
            <div class="formRow">
                <div class = "col">
                    <label for="secondName" class="form-label"><fmt:message key="register.label.secondName"/></label>
                </div>
                <div clas="col-9">
                    <input type="text"
                    class="form-control"
                    id="secondName"
                    name="secondName"
                    value = "${not empty user? user.secondName: not empty param.secondName? param.secondName: ''}"
                     required/>
                    <span class = "error">${errorMessages['secondName'][language]}</span>

                </div>
            </div>
            <div class="formRow">
                <div class = "col">
                    <label for="email" class="form-label"><fmt:message key="register.label.email" /></label>
                </div>
                <div clas="col-9">
                    <input type="email"
                    class="form-control"
                    id="email"
                    name="email"
                    value = "${not empty user? user.email: not empty param.email? param.email: ''}"
                    required/>
                    <span class = "error">${errorMessages['email'][language]}</span>
                </div>
            </div>
            <div class="formRow">
                <div class = "col">
                    <label for="phone" class="form-label"><fmt:message key="register.label.phone" /></label>
                </div>
                <div clas="col-9">
                   <input type="tel"
                   class="form-control"
                   id="phone"
                   name="phone"
                   value = "${not empty user? user.phone: not empty param.phone? param.phone: ''}"

                   required
                   onInput="phoneCheck()"/>
                   <span id="phoneError"
                         style="display:none"
                         class = "error"
                         ><fmt:message key="register.label.phoneError"/></span>
                   <span class = "error">${errorMessages['phone'][language]}</span>


                </div>
            </div>
            <div class="formRow">
                <div class = "col">
                    <label for="password" class="form-label"><fmt:message key="register.label.password"/></label>
                </div>
                <div clas="col-9">
                    <input type="password"
                           class="form-control"
                           id="password"
                           name="password"
                           required
                           onInput = "comparePassword();"/>
                </div>
                    <span class = "error">${errorMessages['password'][language]}</span>
            </div>
            <div class="formRow">
                <div class = "col">
                    <label for="password" class="form-label"><fmt:message key="register.label.passwordConfirm"/></label>
                </div>
                <div clas="col-9">
                    <input type="password"
                    class="form-control"
                    id="passwordConfirmation"
                    name="passwordConfirmation"
                    required
                    onInput = "comparePassword()";
                    />
                    <span id="passwordError"
                          class = "error"
                          style="display:none"><fmt:message key="register.label.passwordError"/></span>
                </div>
            </div>
        </div>
            <c:if test="${errorMessage != null}">
                <div class="center">
                    <p class = "error"><fmt:message key="register.label.error"/></p>
                </div>
            </c:if>
            <input type="submit"
            class="btn btn-primary disabled"
            value=<fmt:message key="register.label.button"/>
            id="userFormSubmitButton"
            >


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

    function phoneCheck() {
            var phone = document.getElementById('phone').value;
            const regex = new RegExp("^\\+?3?8?(0\\d{9})$", 'gm');
            if(regex.test(phone)==true) {
            document.getElementById('phoneError').style='display:none';
            document.getElementById('userFormSubmitButton').classList.remove("disabled");
            }
            else {
                document.getElementById('phoneError').style='';
                document.getElementById('userFormSubmitButton').classList.add("disabled");
            }
    }
</script>