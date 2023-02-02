<div class="d-flex justify-content-start">
    <c:if test="${user==null}">
        <a class ="
            <c:if test="${fn:containsIgnoreCase(commandUrl, 'catalog')}">
                    <c:out value='btn btn-secondary btn-sm disabled'/>
            </c:if>
            <c:if test="${!fn:containsIgnoreCase(commandUrl, 'catalog')}">
                    <c:out value='btn btn-secondary btn-sm'/>
            </c:if>"
        href="controller?command=catalog"><fmt:message key="header.menu.catalog"/></a>
        &nbsp
        <a class ="
                <c:if test="${fn:containsIgnoreCase(commandUrl, 'login')}">
                        <c:out value='btn btn-secondary btn-sm disabled'/>
                </c:if>
                <c:if test="${!fn:containsIgnoreCase(commandUrl, 'login')}">
                        <c:out value='btn btn-secondary btn-sm'/>
                </c:if>"
                href="controller?command=login"><fmt:message key="header.menu.login"/></a>
        </c:if>

    <c:if test="${user.role.roleName['en']=='user'}">
    <a class ="
            <c:if test="${fn:containsIgnoreCase(commandUrl, 'catalog')}">
                    <c:out value='btn btn-secondary btn-sm disabled'/>
            </c:if>
            <c:if test="${!fn:containsIgnoreCase(commandUrl, 'catalog')}">
                    <c:out value='btn btn-secondary btn-sm'/>
            </c:if>"
            href="controller?command=catalog"><fmt:message key="header.menu.catalog"/></a>
     &nbsp
    <a class ="
            <c:if test="${fn:containsIgnoreCase(commandUrl, 'subscriptions')}">
                    <c:out value='btn btn-secondary btn-sm disabled'/>
            </c:if>
            <c:if test="${!fn:containsIgnoreCase(commandUrl, 'subscriptions')}">
                    <c:out value='btn btn-secondary btn-sm'/>
            </c:if>"
            href="controller?command=subscriptions"><fmt:message key="header.menu.subscriptions"/></a>
     &nbsp
    <a class ="
                    <c:if test="${fn:containsIgnoreCase(commandUrl, 'account')}">
                            <c:out value='btn btn-secondary btn-sm disabled'/>
                    </c:if>
                    <c:if test="${!fn:containsIgnoreCase(commandUrl, 'account')}">
                            <c:out value='btn btn-secondary btn-sm'/>
                    </c:if>"
                    href="controller?command=account"><fmt:message key="header.menu.account"/></a>
    &nbsp
    <a class ="
                <c:if test="${fn:containsIgnoreCase(commandUrl, 'logout')}">
                        <c:out value='btn btn-secondary btn-sm disabled'/>
                </c:if>
                <c:if test="${!fn:containsIgnoreCase(commandUrl, 'logout')}">
                        <c:out value='btn btn-secondary btn-sm'/>
                </c:if>"
                href="controller?command=logout"><fmt:message key="header.menu.logout"/></a>

    </c:if>

    <c:if test="${user.role.roleName['en']=='librarian'}">
    <a class ="
                <c:if test="${fn:containsIgnoreCase(commandUrl, 'catalog')}">
                        <c:out value='btn btn-secondary btn-sm disabled'/>
                </c:if>
                <c:if test="${!fn:containsIgnoreCase(commandUrl, 'catalog')}">
                        <c:out value='btn btn-secondary btn-sm'/>
                </c:if>"
                href="controller?command=catalog"><fmt:message key="header.menu.catalog"/></a>
     &nbsp
     <a class ="
                 <c:if test="${fn:containsIgnoreCase(commandUrl, 'subscriptions')}">
                         <c:out value='btn btn-secondary btn-sm disabled'/>
                 </c:if>
                 <c:if test="${!fn:containsIgnoreCase(commandUrl, 'subscriptions')}">
                         <c:out value='btn btn-secondary btn-sm'/>
                 </c:if>"
                 href="controller?command=subscriptions"><fmt:message key="header.menu.subscriptions"/></a>
          &nbsp
    <a class ="
                <c:if test="${fn:containsIgnoreCase(commandUrl, 'orders')}">
                        <c:out value='btn btn-secondary btn-sm disabled'/>
                </c:if>
                <c:if test="${!fn:containsIgnoreCase(commandUrl, 'orders')}">
                        <c:out value='btn btn-secondary btn-sm'/>
                </c:if>"
                href="controller?command=orders"><fmt:message key="header.menu.orders"/></a>
     &nbsp
    <a class ="
                <c:if test="${fn:containsIgnoreCase(commandUrl, 'reader')}">
                        <c:out value='btn btn-secondary btn-sm disabled'/>
                </c:if>
                <c:if test="${!fn:containsIgnoreCase(commandUrl, 'reader')}">
                        <c:out value='btn btn-secondary btn-sm'/>
                </c:if>"
                href="controller?command=readers"><fmt:message key="header.menu.reader"/></a>
     &nbsp
    <a class ="
                 <c:if test="${fn:containsIgnoreCase(commandUrl, 'account')}">
                         <c:out value='btn btn-secondary btn-sm disabled'/>
                 </c:if>
                 <c:if test="${!fn:containsIgnoreCase(commandUrl, 'account')}">
                         <c:out value='btn btn-secondary btn-sm'/>
                 </c:if>"
                 href="controller?command=account"><fmt:message key="header.menu.account"/></a>
    &nbsp

    <a class ="
                <c:if test="${fn:containsIgnoreCase(commandUrl, 'logout')}">
                        <c:out value='btn btn-secondary btn-sm disabled'/>
                </c:if>
                <c:if test="${!fn:containsIgnoreCase(commandUrl, 'logout')}">
                        <c:out value='btn btn-secondary btn-sm'/>
                </c:if>"
                href="controller?command=logout"><fmt:message key="header.menu.logout"/></a>
    </c:if>

    <c:if test="${user.role.roleName['en']=='administrator'}">
    <a class ="
            <c:if test="${fn:containsIgnoreCase(commandUrl, 'catalog')}">
                    <c:out value='btn btn-secondary btn-sm disabled'/>
            </c:if>
            <c:if test="${!fn:containsIgnoreCase(commandUrl, 'catalog')}">
                    <c:out value='btn btn-secondary btn-sm'/>
            </c:if>"
        href="controller?command=catalog"><fmt:message key="header.menu.catalog"/></a>
    &nbsp
    <a class ="
            <c:if test="${fn:containsIgnoreCase(commandUrl, 'subscriptions')}">
                    <c:out value='btn btn-secondary btn-sm disabled'/>
            </c:if>
            <c:if test="${!fn:containsIgnoreCase(commandUrl, 'subscriptions')}">
                    <c:out value='btn btn-secondary btn-sm'/>
            </c:if>"
            href="controller?command=subscriptions"><fmt:message key="header.menu.subscriptions"/></a>
     &nbsp
    <a class ="
                <c:if test="${fn:containsIgnoreCase(commandUrl, 'booksManager')}">
                        <c:out value='btn btn-secondary btn-sm disabled'/>
                </c:if>
                <c:if test="${!fn:containsIgnoreCase(commandUrl, 'booksManager')}">
                        <c:out value='btn btn-secondary btn-sm'/>
                </c:if>"
                href="controller?command=booksManager"><fmt:message key="header.menu.books"/></a>
     &nbsp
    <a class ="
                <c:if test="${fn:containsIgnoreCase(commandUrl, 'userManager')}">
                        <c:out value='btn btn-secondary btn-sm disabled'/>
                </c:if>
                <c:if test="${!fn:containsIgnoreCase(commandUrl, 'userManager')}">
                        <c:out value='btn btn-secondary btn-sm'/>
                </c:if>"
                href="controller?command=userManager"><fmt:message key="header.menu.userManager"/></a>
     &nbsp

    <a class ="
                     <c:if test="${fn:containsIgnoreCase(commandUrl, 'account')}">
                             <c:out value='btn btn-secondary btn-sm disabled'/>
                     </c:if>
                     <c:if test="${!fn:containsIgnoreCase(commandUrl, 'account')}">
                             <c:out value='btn btn-secondary btn-sm'/>
                     </c:if>"
                     href="controller?command=account"><fmt:message key="header.menu.account"/></a>
    &nbsp

    <a class ="
                    <c:if test="${fn:containsIgnoreCase(commandUrl, 'logout')}">
                            <c:out value='btn btn-secondary btn-sm disabled'/>
                    </c:if>
                    <c:if test="${!fn:containsIgnoreCase(commandUrl, 'logout')}">
                            <c:out value='btn btn-secondary btn-sm'/>
                    </c:if>"
                    href="controller?command=logout"><fmt:message key="header.menu.logout"/></a>
    </c:if>
    &nbsp

    <div>
        <form method = "GET">
           <select id="language" name="language" class="form-control" onchange="changeLanguage()">
               <option value="en" ${language == 'en' ? 'selected' : ''}>EN</option>
               <option value="ua" ${language == 'ua' ? 'selected' : ''}>UA</option>
           </select>
        </form>
    </div>

</div>

<script type="text/javascript">

    function changeLanguage(){
            const urlParams = new URLSearchParams(window.location.search);
            urlParams.set('language', document.getElementById('language').value);
            window.location.search = urlParams;
            }

</script>