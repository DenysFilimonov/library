<c:set var="class_"
       value="${not empty user ? 'bookTableUser' : 'bookTableGuest'}"
       scope="session" />

<div class="container-md col-9 text-center">

<div class="container col-12 bgColor rounded h4 text-center white">
    <fmt:message key="catalog.label.header" />
</div>
    <form method = "POST">
    <div class="row">
        <div class="col">
            <div class="input-group pb-2">
                   <div class="input-group-text">
                    <fmt:message key="catalog.label.title"/>
                   </div>
                   <input type="text" class="form-control form-control-sm"
                   value = "${not empty param.title? param.title: ''}"
                   name  = "title"
                   id="titleSearchInputField"
                   placeholder=<fmt:message key="catalog.label.title"/>>
                   <div class="input-group-text">
                   <fmt:message key="catalog.label.author"/>
                   </div>
                   <input type="text" class="form-control form-control-sm"
                   value = "${not empty param.author? param.author: ''}"
                   name = "author"
                   id="authorSearchInputField"
                   placeholder=<fmt:message key="catalog.label.Author"/>>
                  <div class="input-group-text">
                   <input type="submit" class="btn btn-primary btn-sm" value=<fmt:message key="login.label.find"/>>
                  </div>
            </div>
        </div>

    </div>
    </form>


    <div class="${class_}">
        <div class = "tableHeader">
            <div class="head">
                <fmt:message key="catalog.label.title"/>
            </div>
            <div class ="arrows">
                    <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('${language=='en'?'title':'title_ua'}', 'asc');"/>
                    <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('${language=='en'?'title':'title_ua'}', 'desc');"/>
            </div>
        </div>
        <div class = "tableHeader">
            <div class="head">
                <fmt:message key="catalog.label.firstName"/>
            </div>
            <div class ="arrows">
                <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('${language=='en'?'first_name':'first_name'}', 'asc');"/>
                <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('${language=='en'?'first_name':'first_name'}', 'desc');"/>
            </div>
        </div>
        <div class = "tableHeader">
            <div class="head">
                <fmt:message key="catalog.label.secondName"/>
            </div>
            <div class ="arrows">
                <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('${language=='en'?'second_name':'second_name_ua'}', 'asc');"/>
                <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('second_name''${language=='en'?'second_name_ua':'second_name'}', 'desc');"/>
            </div>
        </div>
        <div class = "tableHeader">
           <div class="head">
                <fmt:message key="catalog.label.publisher"/>
           </div>
           <div class ="arrows">
               <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('${language=='en'?'publisher':'publisher_ua'}', 'asc');"/>
               <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('${language=='en'?'publisher':'publisher_ua'}', 'desc');"/>
           </div>
        </div>
        <div class = "tableHeader">
            <div class="head">
                <fmt:message key="catalog.label.date"/>
            </div>
            <div class ="arrows">
               <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('publishing_date', 'asc');"/>
               <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('publishing_date', 'desc');"/>
            </div>
        </div>
        <c:if test="${user!= null}">
            <div class = "tableHeader"><fmt:message key="catalog.label.order"/></div>
        </c:if>

        <c:forEach items="${books}" var="book">
            <div class="tableCell">${book.title[language]}</div>
            <div class="tableCell">${book.author.firstName[language]}</div>
            <div class="tableCell">${book.author.secondName[language]}</div>
            <div class="tableCell">${book.publisher.publisher[language]}</div>
            <div class="tableCell">${book.date}</div>
            <c:if test="${(user!= null)}">
                <div class="tableCell">
            </c:if>
                <c:if test="${user!= null and book.availableQuantity>0 and orders[book.id]==null}">
                    <button class="btn btn-primary"
                        onClick = "setModalParams(${book.id}, '${book.title[language]}',
                        '${book.author.firstName[language]}', '${book.author.secondName[language]}',
                        '${book.cover}',  '${book.publisher.publisher[language]}',  '${book.publisher.country[language]}',
                         '${book.date}')"
                    ><fmt:message key="catalog.label.order"/></button>
                    <button type="button"
                        id= "${book.id}"
                        class="btn btn-primary btn-sm"
                        data-bs-toggle="modal"
                        data-bs-target="#orderBook"
                        hidden
                    >
                    </button>

                </c:if>
                <c:if test="${user!= null and book.availableQuantity<=0 and orders[book.id]==null}">
                    <fmt:message key="catalog.label.na"/>
                </c:if>
                <c:if test="${user!=null and orders[book.id].status.status['en']=='order'}">
                    <fmt:message key="catalog.label.ordered"/>
                </c:if>
                <c:if test="${user!=null and orders[book.id].status.status['en']=='issued'}">
                    <fmt:message key="catalog.label.issued"/>
                </c:if>
                <c:if test="${(user!= null)}">
                </div>

    </c:if>
    </c:forEach>
</div>

<%@include file="pagination.jsp" %>



<script type="text/javascript">



    function setModalParams(id, title, firstName, secondName, cover, publisher, country, date) {
        document.getElementById(id).click();
        document.getElementById('titleModal').innerHTML=title;
        document.getElementById('firstNameModal').innerHTML=firstName+" "+secondName;
        var d = new Date(date);
        document.getElementById('publisherModal').innerHTML=publisher +", "+ country +", "+d.getFullYear();
        document.getElementById('book').value=id;
        document.getElementById('coverImage').src=cover;
    }

    function sortFunction(sort, order){
            const urlParams = new URLSearchParams(window.location.search);
            urlParams.set('sort', sort);
            urlParams.set('order', order);
            if(document.getElementById("authorSearchInputField").value){
               urlParams.set('author', document.getElementById("authorSearchInputField").value);
            }
            if(document.getElementById("titleSearchInputField").value){
                urlParams.set('title', document.getElementById("titleSearchInputField").value);
            }
            window.location.search = urlParams;
         }

</script>




