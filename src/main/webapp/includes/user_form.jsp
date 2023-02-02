<div class="container col-6 rounded bgColor p-3">
<div class="h4 text-center white">
    <fmt:message key="register.label.header" />
</div>
    <form method="POST">
         <div class="input-group">
              <div class="input-group-text w15">
                    <label for="login" class="form-label"><fmt:message key="register.label.login" /></label>
              </div>
               <input type="text"
                     class="form-control rounded-right"
                     id="login"
                     name="login"
                     value = "${not empty user? user.login: not empty param.login? param.login: ''}"
                     required/>
         </div>
         <div class="input-group pt-1">
               <span class = "error">${errorMessages['login'][language]}</span>
        </div>

    <div class="input-group pt-1">
           <div class="input-group-text w15">
                <label for="firstName" class="form-label"><fmt:message key="register.label.firstName" /></label>
           </div>
           <input type="text"
                class="form-control rounded-right"
                id="firstName"
                name="firstName"
                value = "${not empty user? user.firstName: not empty param.firstName? param.firstName: ''}"
                required/>
        </div>
        <div class="input-group pt-1">
           <span class = "error">${errorMessages['firstName'][language]}</span>
        </div>

    <div class="input-group pt-1">
       <div class="input-group-text w15">
            <label for="secondName" class="form-label"><fmt:message key="register.label.secondName"/></label>
       </div>

        <input type="text"
           class="form-control rounded-right"
           id="secondName"
           name="secondName"
           value = "${not empty user? user.secondName: not empty param.secondName? param.secondName: ''}"
            required/>
    </div>
    <div class="input-group pt-1">
           <span class = "error">${errorMessages['secondName'][language]}</span>
    </div>

    <div class="input-group pt-1">
       <div class="input-group-text w15">
            <label for="email" class="form-label"><fmt:message key="register.label.email" /></label>
       </div>

        <input type="email"
            class="form-control rounded-right"
            id="email"
            name="email"
            value = "${not empty user? user.email: not empty param.email? param.email: ''}"
            required/>
    </div>
    <div class="input-group pt-1">
        <span class = "error">${errorMessages['email'][language]}</span>
    </div>

    <div class="input-group pt-1">
       <div class="input-group-text w15">
            <label for="phone" class="form-label"><fmt:message key="register.label.phone" /></label>
       </div>

        <input type="tel"
           class="form-control rounded-right"
           id="phone"
           name="phone"
           value = "${not empty user? user.phone: not empty param.phone? param.phone: ''}"

           required
           onInput="phoneCheck()"/>
       <span id="phoneError"
             style="display:none"
             class = "error"
             ><fmt:message key="register.label.phoneError"/></span>
    </div>
    <div class="input-group pt-1">
       <span class = "error">${errorMessages['phone'][language]}</span>
    </div>

    <div class="input-group pt-1">
       <div class="input-group-text w15">
            <label for="password" class="form-label"><fmt:message key="register.label.password"/></label>
       </div>

         <input type="password"
                   class="form-control rounded-right"
                   id="password"
                   name="password"
                   required
                   onInput = "comparePassword();"/>
    </div>
    <div class="input-group pt-1">
        <span class = "error">${errorMessages['password'][language]}</span>
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
    <div class="container text-center pt-1">
            <input type="submit"
            class="btn btn-primary disabled"
            value=<fmt:message key="register.label.button"/>
            id="userFormSubmitButton"
            >
    </div>

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