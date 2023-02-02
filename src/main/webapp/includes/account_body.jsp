<div class="container">
        <div class="container">
            <div class="formRow">
                <div class="col">
                    <label for="login" class="form-label"><fmt:message key="register.label.login" /></label>
                </div>
                <div clas="col-9">
                    ${user.login}
                </div>
            </div>
            <div class="formRow">
                <div class = "col">
                    <label for="firstName" class="form-label"><fmt:message key="register.label.firstName" /></label>
                </div>
                <div clas="col-9">
                      ${user.firstName}
                </div>
            </div>
            <div class="formRow">
                <div class = "col">
                    <label for="secondName" class="form-label"><fmt:message key="register.label.secondName"/></label>
                </div>
                <div clas="col-9">
                     ${user.secondName}
                </div>
            </div>
            <div class="formRow">
                <div class = "col">
                    <label for="email" class="form-label"><fmt:message key="register.label.email" /></label>
                </div>
                <div clas="col-9">
                     ${user.email}
                </div>
            </div>
            <div class="formRow">
                <div class = "col">
                    <label for="phone" class="form-label"><fmt:message key="register.label.phone" /></label>
                </div>
                <div clas="col-9">
                     ${user.phone}
                </div>
            </div>

        </div>

        <bu>



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