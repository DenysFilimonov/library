<c:set var="display"
 value="${ not empty errorMessages[oldPassword]  ? 'block': 'none' }"
 scope="request" />

<div class="container col-6 rounded bgColor p-3">
    <div class="h4 text-center white">
        <fmt:message key="editAccount.label.header" />
    </div>
         <form method="POST" id="userForm">
         <div class="container">

         <div class="input-group">
            <div class="input-group-text w15">
                <label for="login" class="form-label"><fmt:message key="register.label.login" /></label>
            </div>
            <input type="text"
                   class="form-control"
                   id="login"
                   name="login"
                   pattern="[^-'.%=+&?]+"
                   value = "${not empty user? user.login: not empty param.login? param.login: ''}"
                   disabled
                   required/>
            <input type = "text" hidden name="id" value="${user.id}"/>
         </div>
         <div class="input-group pt-1">
            <span class = "error">${errorMessages['login'][language]}</span>
         </div>

    <div class="input-group">
        <div class="input-group-text w15">
            <label for="firstName" class="form-label"><fmt:message key="register.label.firstName" /></label>
        </div>
        <input type="text"
             class="form-control"
             id="firstName"
             name="firstName"
             disabled
             value = "${not empty param.firstName? param.firstName: not empty user? user.firstName : ''}"
             required/>
    </div>
    <div class="input-group pt-1">
        <span class = "error">${errorMessages['firstName'][language]}</span>
    </div>

     <div class="input-group">
        <div class="input-group-text w15">
            <label for="secondName" class="form-label"><fmt:message key="register.label.secondName"/></label>
        </div>
        <input type="text"
            class="form-control"
            id="secondName"
            name="secondName"
            value =  "${not empty param.secondName? param.secondName: not empty user? user.secondName : ''}"
            disabled
            required/>
     </div>
     <div class="input-group pt-1">
            <span class = "error">${errorMessages['secondName'][language]}</span>
     </div>

    <div class="input-group">
         <div class="input-group-text w15">
            <label for="email" class="form-label"><fmt:message key="register.label.email" /></label>
         </div>
         <input type="email"
             class="form-control"
             id="email"
             name="email"
             value =  "${not empty param.email? param.email: not empty user? user.email : ''}"
             disabled
             required/>
      </div>
    <div class="input-group pt-1">
            <span class = "error">${errorMessages['email'][language]}</span>
    </div>

    <div class="input-group">
         <div class="input-group-text w15">
            <label for="phone" class="form-label"><fmt:message key="register.label.phone" /></label>
         </div>
         <input type="tel"
                class="form-control"
                id="phone"
                name="phone"
                value = "${not empty param.phone? param.phone: not empty user? user.phone : ''}"
                disabled
                required
                onInput="phoneCheck()"/>
      </div>
    <div class="input-group pt-1">
             <span id="phoneError"
                 style="display:none"
                 class = "error"
                 ><fmt:message key="register.label.phoneError"/></span>
              <span id ="phoneErrorGlobal" class = "error">${errorMessages['phone'][language]}</span>
    </div>

    <div id="passwordGroup" style="display:${display}">
        <div class="input-group">
                 <div class="input-group-text w15">
                    <label for="oldPassword" class="form-label"><fmt:message key="account.label.passwordOld"/></label>
                 </div>
                 <input type="password"
                     class="form-control"
                     id="oldPassword"
                     name="oldPassword"
                     required
                 />
        </div>
        <div class="input-group pt-1">
                <span class = "error">${errorMessages['oldPassword'][language]}</span>
        </div>

        <div class="input-group">
                 <div class="input-group-text w15">
                    <label for="password" class="form-label"><fmt:message key="register.label.password"/></label>
                 </div>
                 <input type="password"
                        class="form-control"
                        id="password"
                        name="password"
                        required
                        onInput = "comparePassword();"
                 />
        </div>
        <div class="input-group pt-1">
            <span class = "error">${errorMessages['password'][language]}</span>
        </div>

        <div class="input-group">
                 <div class="input-group-text w15">
                    <label for="passwordConfirmation" class="form-label"><fmt:message key="register.label.passwordConfirm"/></label>
                 </div>
                 <input type="password"
                    class="form-control"
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
        </div>
    </div>
    <div class="mt-3">
        <div style="text-align:center">
                    <button
                        class="btn btn-primary"
                        id="userFormActivate"
                        onClick="activateForm();"
                    ><fmt:message key="account.label.edit"/>
                    </button>
                    <button
                        class="btn btn-primary"
                        hidden
                        id="cancelButton"
                        onClick="activateForm();"
                    ><fmt:message key="account.label.cancel"/>
                    </button>
                    <button
                        class="btn btn-primary"
                        id="passwordChange"
                        hidden
                        onClick="editPassword();"
                    ><fmt:message key="account.label.editPassword"/>
                    </button>
                    <input type="submit"
                        hidden
                        class="btn btn-primary"
                        value=<fmt:message key="account.label.submit"/>
                        id="submitButton"
                        onClick="submitForm();"
                    />
                </div>
    </div>
    </form>
</div>


<script type="text/javascript">
    function comparePassword() {
        var password = document.getElementById('password').value;
        var passwordCheck = document.getElementById('passwordConfirmation').value;
        if (password!=passwordCheck) {
            document.getElementById('passwordError').style = "";
            document.getElementById('submitButton').classList.add("disabled");
        }
        else{
            document.getElementById('passwordError').style = "display:none";
            document.getElementById('submitButton').classList.remove("disabled");
        }

    }

    function phoneCheck() {
            var phone = document.getElementById('phone').value;
            const regex = new RegExp("^\\+?3?8?(0\\d{9})$", 'gm');
            document.getElementById('phoneErrorGlobal').style='display:none';
            if(regex.test(phone)==true) {
            document.getElementById('phoneError').style='display:none';
            document.getElementById('submitButton').classList.remove("disabled");
            }
            else {
                document.getElementById('phoneError').style='';

                document.getElementById('submitButton').classList.add("disabled");
            }
    }

    function activateForm(){
        var idList=['login', 'firstName', 'secondName', 'email', 'phone'];

        idList.map(x=>{
            document.getElementById(x).disabled = !document.getElementById(x).disabled;
        });
        document.getElementById('userFormActivate').hidden= !document.getElementById('userFormActivate').hidden;
        document.getElementById('cancelButton').hidden=!document.getElementById('cancelButton').hidden;
        if(document.getElementById('passwordChange').hidden)
            document.getElementById('passwordChange').hidden=!document.getElementById('passwordChange').hidden;
        document.getElementById('submitButton').hidden=!document.getElementById('submitButton').hidden;
        document.getElementById('passwordGroup').style.display='none';
        setPassNull();
    }

    function editPassword(){
        console.log(document.getElementById('passwordGroup').style);
        if (document.getElementById('passwordGroup').style.display=='none')
            document.getElementById('passwordGroup').style.display='block';
        else {document.getElementById('passwordGroup').style.display ='none'
              setPassNull();
        };
        document.getElementById('passwordChange').hidden = true;
    }

    function submitForm(){
        document.getElementById("userForm").submit();
    }

    function setPassNull(){
        document.getElementById('password').value="";
        document.getElementById('passwordConfirmation').value="";
        document.getElementById('oldPassword').value="";
    }



</script>