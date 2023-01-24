<c:set var="class_"
       value="${not empty user ? 'bookTableUser' : 'bookTableGuest'}"
       scope="session" />
<div class="container-md w-60 text-center">
    <form method = "POST">
        <div class="row">
            <div class="col">
                <input type="text" class="form-control" name  = "title" placeholder=<fmt:message key="catalog.label.title"/>>
            </div>
            <div class="col">
                <input type="text" class="form-control" name = "author" placeholder=<fmt:message key="catalog.label.Author"/>>
            </div>

            <div class="col">
                <input type="submit" class="btn btn-primary" value=<fmt:message key="login.label.button"/>>
            </div>
        </div>
    </form>


    <div class="${class_}">
        <div class = "tableHeader">
            <div class="head">
                <fmt:message key="catalog.label.title"/>
            </div>
            <div class ="arrows">
                    <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('title', 'asc');"/>
                    <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('title', 'desc');"/>
            </div>
        </div>
        <div class = "tableHeader">
            <div class="head">
                <fmt:message key="catalog.label.firstName"/>
            </div>
            <div class ="arrows">
                <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('first_name', 'asc');"/>
                <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('first_name', 'desc');"/>
            </div>
        </div>
        <div class = "tableHeader">
            <div class="head">
                <fmt:message key="catalog.label.secondName"/>
            </div>
            <div class ="arrows">
                <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('second_name', 'asc');"/>
                <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('second_name', 'desc');"/>
            </div>
        </div>
        <div class = "tableHeader">
           <div class="head">
                <fmt:message key="catalog.label.publisher"/>
           </div>
           <div class ="arrows">
               <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('publisher', 'asc');"/>
               <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('publisher', 'desc');"/>
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
            <div class="tableCell">${book.date} ${orders[book.id].status.status}</div>
            <c:if test="${(user!= null)}">
                <div class="tableCell">
            </c:if>
                <c:if test="${user!= null and book.availableQuantity>0 and orders[book.id]==null}">
                    <button class="btn btn-primary"
                        onClick = "a1_onclick(${book.id}, '${book.title[language]}', '${book.author.firstName[[language]]}', '${book.author.secondName[language]}')"
                    ><fmt:message key="catalog.label.order"/></button>
                    <button type="button"
                        id= "${book.id}"
                        class="btn btn-primary"
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
<div class="col">
    <label class="mr-sm-2 margintop" for="inlineFormCustomSelect">Books on page</label>
    <select class="custom-select mr-sm-2" id="linesOnPage" name ="linesOnPage" onChange="setLinesOnPage();">
        <option selected value="1">1</option>
        <option selected value="5">5</option>
        <option value="10">10</option>
    </select>
</div>
<div>
    <fmt:message key="catalog.label.page"/>
    <c:if test="${pagination!= null}">
        <c:forEach var = "i" begin = "1" end = "${pagination.totalPages}">
            <a href="/Library/controller?command=catalog&page=${i}"
               class =  <c:if test="${pagination.currentPage==i}">"currentPage"</c:if>
    >${i}&nbsp</a>
    </c:forEach>
    </c:if>
</div>

<script type="text/javascript">
    function a1_onclick(id, title, firstName, secondName) {
        document.getElementById(id).click();
        document.getElementById('titleModal').innerHTML=title;
        document.getElementById('firstNameModal').innerHTML=firstName+" "+secondName;
        document.getElementById('book').value=id;
    }

    function sortFunction(sort, order){
       const urlParams = new URLSearchParams(window.location.search);
       urlParams.set('sort', sort);
       urlParams.set('order', order);
       console.log(urlParams);
       window.location.search = urlParams;
    }

    function setLinesOnPage(){
        const urlParams = new URLSearchParams(window.location.search);
        var lines = document.getElementById('linesOnPage').value;
        urlParams.set('linesOnPage', lines);
       window.location.search = urlParams;
    }

</script>




