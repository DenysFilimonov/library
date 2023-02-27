<c:set var="class_"
       value="${not empty user ? 'bookTableUser' : 'bookTableGuest'}"
       scope="session" />

 <div class="container-md col-9 text-center">

    <form method = "POST">
        <input type="text" hidden name="formName" value="searchForm" />
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
                      <div class="input-group-text">

                          <a class="btn btn-success"
                          href="controller?command=newBook"
                          > <fmt:message key="bookManager.label.new"/></a>
                      </div>
                </div>
            </div>

        </div>
        </form>

    <div class="bookManagerTable">
        <div class = "tableHeader">
                    <div class="head">
                        ID
                    </div>
                    <div class ="arrows">
                            <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('id', 'asc');"/>
                            <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('id', 'desc');"/>
                    </div>
        </div>

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
                <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('${language=='en'?'first_name':'first_name_ua'}', 'asc');"/>
                <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('${language=='en'?'first_name':'first_name_ua'}', 'desc');"/>
            </div>
        </div>
        <div class = "tableHeader">
            <div class="head">
                <fmt:message key="catalog.label.secondName"/>
            </div>
            <div class ="arrows">
                <img class="arrow" src="SVG/sort-arrow-up.svg" onClick="javascript:sortFunction('${language=='en'?'second_name':'second_name'}', 'asc');"/>
                <img class="arrow" src="SVG/sort-arrow-down.svg" onClick="javascript:sortFunction('${language=='en'?'second_name':'second_name'}', 'desc');"/>
            </div>
        </div>

        <c:if test="${user.role.roleName['en']== 'administrator'}">
            <div class = "tableHeader"><fmt:message key="bookManager.label.edit"/></div>
        </c:if>

        <c:forEach items="${books}" var="book">
            <div class="tableCell">${book.id}</div>
            <div class="tableCell">${book.title[language]}</div>
            <div class="tableCell">${book.author.firstName[language]}</div>
            <div class="tableCell">${book.author.secondName[language]}</div>
            <div class="tableCell">
                    <button class="btn btn-primary"
                        id="${book.id}"
                        onClick = "editBook(
                                    '${language}',
                                    '${languageAlter}',
                                    ${book.id},
                                   '${book.isbn}',
                                   '${fn:replace(book.title[language], "'", "`")}',
                                   '${fn:replace(book.title[languageAlter], "'", "`")}',
                                   '${book.author.id}',
                                   '${book.author.firstName[language]}',
                                   '${book.author.firstName[languageAlter]}',
                                   '${book.author.secondName[language]}',
                                   '${book.author.secondName[languageAlter]}',
                                   '${book.publisher.id}',
                                   '${book.publisher.publisher[language]}',
                                   '${book.publisher.publisher[languageAlter]}',
                                   '${book.publisher.country[language]}',
                                   '${book.publisher.country[languageAlter]}',
                                   '${book.date}',
                                   '${book.quantity}',
                                   '${book.cover}',
                                   '${book.bookStore.caseNum}',
                                   '${book.bookStore.shelfNum}',
                                   '${book.bookStore.cellNum}',
                                   '${book.genre.id}'
                                   );"
                    ><fmt:message key="bookManager.label.edit"/></button>
            </div>
        </c:forEach>
    </div>
    <%@include file="pagination.jsp" %>
 </div>



<script type="text/javascript">

    function setPage(pageNum){
            const urlParams = new URLSearchParams(window.location.search);
            urlParams.set('page', pageNum);
            window.location.search = urlParams;
         }


    function editBook( language,
                       languageAlter,
                       bookId,
                       bookIsbn,
                       bookTitle,
                       bookTitleAlter,
                       bookAuthorId,
                       bookAuthorFirstName,
                       bookAuthorFirstNameAlter,
                       bookAuthorSecondName,
                       bookAuthorSecondNameAlter,
                       bookPublisherId,
                       bookPublisher,
                       bookPublisherAlter,
                       bookPublisherCountry,
                       bookPublisherCountryAlter,
                       bookDate,
                       bookQuantity,
                       bookCover,
                       bookStoreCase,
                       bookStoreShelf,
                       bookStoreCell,
                       bookGenreId
                       ) {
       document.getElementById('idModal').value=bookId;
       document.getElementById('isbnModal').value=bookIsbn;
       document.getElementById('titleModal'+"_"+language).value=bookTitle;
       document.getElementById('titleModal'+"_"+languageAlter).value=bookTitleAlter;
       document.getElementById('authorIdModal').value=bookAuthorId;
       document.getElementById('firstNameModal'+"_"+language).value=bookAuthorFirstName;
       document.getElementById('firstNameModal'+"_"+languageAlter).value=bookAuthorFirstNameAlter;
       document.getElementById('secondNameModal'+"_"+language).value=bookAuthorSecondName;
       document.getElementById('secondNameModal'+"_"+languageAlter).value=bookAuthorSecondNameAlter;
       document.getElementById('publisherIdModal').value=bookPublisherId;
       document.getElementById('publisherModal'+"_"+language).value=bookPublisher;
       document.getElementById('publisherModal'+"_"+languageAlter).value=bookPublisherAlter;
       document.getElementById('countryModal'+"_"+language).value=bookPublisherCountry;
       document.getElementById('countryModal'+"_"+languageAlter).value=bookPublisherCountryAlter;
       document.getElementById('dateModal').value=bookDate;
       document.getElementById('quantityModal').value=bookQuantity;
       document.getElementById('coverImage').src=bookCover;
       document.getElementById('coverModal').value=bookCover;
       document.getElementById("modalOpenButton").click();
       selectElement('genreSelectModal', bookGenreId);
       selectElement('caseSelectModal', bookStoreCase);
       selectElement('shelfSelectModal', bookStoreShelf);
       selectElement('cellSelectModal', bookStoreCell);
       console.log(bookCover);
    }

    function selectElement(id, valueToSelect) {
        let element = document.getElementById(id);
        element.value = valueToSelect;
        console.log(id, valueToSelect);
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

    function setLinesOnPage(){
        const urlParams = new URLSearchParams(window.location.search);
        var lines = document.getElementById('linesOnPage').value;
        urlParams.set('linesOnPage', lines);
        window.location.search = urlParams;
    }



</script>




