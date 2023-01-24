<div class = "container">
    <div class="col-2">
    </div>
    <div class="col-8">

            <ul class="nav nav-tabs pt-4" id="myTab" role="tablist">
              <li class="nav-item" role="presentation">
                <button class="nav-link active" id="general-tab"
                data-bs-toggle="tab" data-bs-target="#home" type="button"
                role="tab" aria-controls="home" aria-selected="true">
                <fmt:message key="bookManager.label.generalBookData"/></button>
              </li>
              <li class="nav-item" role="presentation">
                <button class="nav-link" id="author-tab"
                data-bs-toggle="tab" data-bs-target="#profile" type="button"
                role="tab" aria-controls="profile" aria-selected="false">
                <fmt:message key="bookManager.label.AuthorBookData"/></button>
              </li>
              <li class="nav-item" role="presentation">
                <button class="nav-link" id="publisher-tab"
                data-bs-toggle="tab" data-bs-target="#contact" type="button"
                role="tab" aria-controls="contact" aria-selected="false">
                <fmt:message key="bookManager.label.PublisherBookData"/></button>
              </li>
              <li class="nav-item" role="presentation">
                <button class="nav-link" id="store-tab"
                data-bs-toggle="tab" data-bs-target="#store" type="button"
                role="tab" aria-controls="store" aria-selected="false">
                <fmt:message key="bookManager.label.bookStore"/></button>
              </li>
            </ul>
            <form method="POST" action="/Library/controller?command=newBook" name='general' enctype='multipart/form-data'>

                    <div class="tab-content pt-3 ps-3" id="myTabContent">
                        <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                            <div class="formTwoRow">
                                <label for="isbnModal" class="form-label"><fmt:message key="bookManager.label.isbn"/></label>
                                <input type = "text" name ="isbn" id="isbnModal"  class="form-control" required value  ="${not empty param.isbn? param.isbn: ''}"/>
                                <label for="titleModal_en" class="form-label"><fmt:message key="bookManager.label.titleEN"/></label>
                                <input type = "text" name ="titleEn" id="titleModal_en"  class="form-control" required value="${not empty param.titleEn? param.titleEn: ''}"/>
                                <label for="titleModal_ua" class="form-label"><fmt:message key="bookManager.label.titleUA"/></label>
                                <input type = "text" name ="titleUa" id="titleModal_ua"  class="form-control" required value="${not empty param.titleUa? param.titleUa: ''}"/>
                                <label for="genreSelectModal" class="form-label"><fmt:message key="bookManager.label.genreModal"/></label>
                                <div class="input-group">
                                      <select name="genreId"
                                              id="genreSelectModal"
                                              value="${not empty param.genreId? param.genreId: ''}"
                                              required
                                              class = "form-control"
                                              >
                                          <option value="">--Please choose an option--</option>
                                           <c:forEach items="${genres.keySet()}" var="genre">
                                              <option value="${genres[genre].id}">${genres[genre].genre[language]}</option>
                                           </c:forEach>

                                      </select>
                                      <div class="input-group-text">
                                        <a class = "btn btn-warning btn-sm"
                                            id = "newGenreButton"
                                            href="javascript:newGenre();"><fmt:message key="newBook.label.newAuthor"/></a>
                                      </div>

                                </div>
                            </div>


                            <div id = "newGenreData" style="display:none; padding-right: 3em;">
                                <div class="formTwoRowRM0">
                                    <label for="genreModal_en" class="form-label"><fmt:message key="bookManager.label.genreModal_en"/></label>
                                    <input type = "text" name ="genreEn"
                                           id="genreModal_en"  class="form-control"
                                           value="${not empty param.genreEn? param.genreEn: ''}"
                                           />
                                    <label for="genreModal_ua" class="form-label"><fmt:message key="bookManager.label.genreModal_ua"/></label>
                                    <input type = "text" name ="genreUa"
                                           id="genreModal_ua"  class="form-control"
                                           value="${not empty param.genreUa? param.genreUa: ''}"
                                           />
                                </div>
                            </div>
                            <div class="formTwoRow">
                                <label for="quantityModal" class="form-label"><fmt:message key="bookManager.label.image"/></label>
                                <input type = "file"
                                       name ="image"
                                       id="imageModal"
                                       class="form-control"
                                       accept=".jpeg,.gif,.png,.svg, .jpg"
                                       required value="${not empty param.image? param.image: ''}"/>
                                <label for="quantityModal" class="form-label"><fmt:message key="bookManager.label.quantityModal"/></label>
                                <input type = "number"
                                        name ="quantity"
                                        id="quantityModal"
                                        class="form-control"
                                        min="1"
                                        max="100"
                                        required
                                        value="${not empty param.quantity? param.quantity: ''}"/>

                            </div>
                            <div class = "footerCenterMT1">
                              <a class="btn btn-primary m-auto" id="generalForward" onClick="validateAndJump('general', 'author-tab')">
                              <fmt:message key="bookManager.label.nextButton"/></a>
                            </div>
                        </div>


                        <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                            <div class="input-group">
                                  <div class="input-group-text">
                                      <a class = "btn btn-primary btn-sm"
                                         id ="findAuthorButton"
                                         href="javascript:findAuthor();">
                                         <fmt:message key="newBook.label.search"/>
                                      </a>
                                  </div>
                                  <input type="text"
                                  id = "findAuthorStr"
                                  class="form-control" placeholder=<fmt:message key="newBook.label.author"/>>
                                  <div class="input-group-text">
                                    <a class = "btn btn-warning btn-sm"
                                        id = "newAuthorButton"
                                        href="javascript:newAuthor();"><fmt:message key="newBook.label.newAuthor"/></a>
                                  </div>

                            </div>

                            <div>
                                 <select name="authorId"
                                         id="authorSelectModal"
                                         class = "form-control mt-3"
                                         required
                                         value="${not empty param.authorId? param.authorId: ''}">
                                     <c:forEach items="${authors}" var="author">
                                        <option value="${author.id}">
                                        ${author.firstName[language]} ${author.secondName[language]} ${author.birthday}</option>
                                     </c:forEach>
                                 </select>


                            </div>

                            <div id = "newContactData" style="display:none">
                            <div class="formTwoRowRM0">

                                <label for="firstNameModal_en" class="form-label"><fmt:message key="bookManager.label.firstNameModalEn"/></label>
                                <input type = "text" name ="firstNameEn"
                                       id="firstNameModal_en"  class="form-control"
                                       
                                       value="${not empty param.firstNameEn? param.firstNameEn: ''}"
                                       />

                                <label for="firstNameModal_ua" class="form-label"><fmt:message key="bookManager.label.firstNameModalUa"/></label>
                                <input type = "text" name ="firstNameUa"
                                       id="firstNameModal_ua"  class="form-control"
                                       value="${not empty param.firstNameUa? param.firstNameUa: ''}"
                                       />

                                <label for="secondNameModal_en" class="form-label"><fmt:message key="bookManager.label.secondNameModal_en"/></label>
                                <input type = "text" name ="secondNameEn"
                                       id="secondNameModal_en"  class="form-control"
                                       value="${not empty param.secondNameEn? param.secondNameEn: ''}"
                                       />

                                <label for="secondNameModal_ua" class="form-label"><fmt:message key="bookManager.label.secondNameModal_ua"/></label>
                                <input type = "text" name ="secondNameUa"
                                       id="secondNameModal_ua"  class="form-control"
                                       value="${not empty param.secondNameUa? param.secondNameUa: ''}"
                                       />

                                <label for="birthdayModal_ua" class="form-label"><fmt:message key="bookManager.label.birthday"/></label>
                                <input type = "date" name ="authorBirthday"
                                       id="birthdayModal_ua"  class="form-control"
                                       value="${not empty param.birthday? param.birthday: null}"
                                       <input type="date" max="3000-01-01" onfocus="this.max=new Date().toISOString().split('T')[0]" />


                                <label for="authorCountry" class="form-label"><fmt:message key="bookManager.label.country"/></label>
                                <input type = "text" name ="authorCountryEn"
                                       id="authorCountry"  class="form-control"
                                       value="${not empty param.authorCountry? param.authorCountry: ""}"
                                       />

                                <label for="authorCountryUa" class="form-label"><fmt:message key="bookManager.label.countryUa"/></label>
                                <input type = "text" name ="authorCountryUa"
                                       id="authorCountryUa"  class="form-control"
                                       value="${not empty param.authorCountryUa? param.authorCountryUa: ""}"
                                       />
                            </div>
                            </div>
                            <div class = "footerCenterMT1">
                                <a class="btn btn-primary m-auto" id="saveModalButton" onClick="validateAndJump('author','general-tab')">
                                    <fmt:message key="bookManager.label.prevButton"/>
                                </a>
                                <a class="btn btn-primary m-auto" id="saveModalButton" onClick="validateAndJump('author', 'publisher-tab')">
                                <fmt:message key="bookManager.label.nextButton"/></a>
                            </div>
                        </div>


                        <div class="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">

                            <div class="input-group">
                                  <div class="input-group-text">
                                      <a class = "btn btn-primary btn-sm"
                                         id ="findPublisherButton"
                                         href="javascript:findPublisher();">
                                         <fmt:message key="newBook.label.search"/>
                                      </a>
                                  </div>
                                  <input type="text"
                                  id = "findPublisherStr"
                                  class="form-control" placeholder=<fmt:message key="newBook.label.publisher"/>>
                                  <div class="input-group-text">
                                    <a class = "btn btn-warning btn-sm"
                                        id = "newPublisherButton"
                                        href="javascript:newPublisher();"><fmt:message key="newBook.label.newAuthor"/></a>
                                  </div>

                            </div>

                            <div>
                                 <select name="publisherId"
                                         id="publisherSelectModal"
                                         class = "form-control mt-3"
                                         required
                                         value="${not empty param.publisherId? param.publisherId: ''}">
                                     <c:forEach items="${publishers}" var="publisher">
                                        <option value="${publisher.id}">
                                        ${publisher.publisher[language]} ${publisher.country[language]}</option>
                                     </c:forEach>
                                 </select>
                            </div>

                            <div id = "newPublisherData" style="display:none">
                            <div class="formTwoRowRM0">
                                <label for="publisherModal_en" class="form-label"><fmt:message key="bookManager.label.publisherModal_en"/></label>
                                <input type = "text" name ="publisherEn"
                                       id="publisherModal_en"  class="form-control"
                                       value="${not empty param.publisherEn? param.publisherEn: ''}"
                                       />
                                <label for="publisherModal_ua" class="form-label"><fmt:message key="bookManager.label.publisherModal_ua"/></label>
                                <input type = "text" name ="publisherUa"
                                       id="publisherModal_ua"  class="form-control"
                                       value="${not empty param.publisherUa? param.publisherUa: ''}"
                                       />

                                <label for="countryModal_en" class="form-label"><fmt:message key="bookManager.label.countryModal_en"/></label>
                                <input type = "text" name ="countryEn"
                                       id="countryModal_en"  class="form-control"
                                       value="${not empty param.countryEn? param.countryEn: ''}"
                                       />

                                <label for="countryModal_ua" class="form-label"><fmt:message key="bookManager.label.countryModal_ua"/></label>
                                <input type = "text" name ="countryUa"
                                       id="countryModal_ua"  class="form-control"
                                       value="${not empty param.countryUa? param.countryUa: ''}"
                                       />


                            </div>
                            </div>
                            <div class="formTwoRowRM0">
                            <label for="dateModal" class="form-label"><fmt:message key="bookManager.label.date"/></label>
                                <input type = "date" name ="date"
                                       id="dateModal"  class="form-control"
                                       required
                                       value="${not empty param.date? param.date: ''}"
                                />
                            </div>

                            <div class = "footerCenterMT1">
                                <a class="btn btn-primary m-auto" id="backPublisher" onClick="validateAndJump('publisher', 'author-tab')">
                                <fmt:message key="bookManager.label.prevButton"/></a>
                                <a class="btn btn-primary m-auto" id="frontPublisher" onClick="validateAndJump('publisher', 'store-tab')">
                                <fmt:message key="bookManager.label.nextButton"/></a>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="store" role="tabpanel" aria-labelledby="store-tab">
                            <div class="formThreeRow">
                                <div>
                                    <label for="caseSelectModal" class="form-label"><fmt:message key="bookManager.label.caseModal"/></label>
                                </div>
                                <div>
                                    <label for="shelfSelectModal" class="form-label"><fmt:message key="bookManager.label.shelfModal"/></label>
                                </div>
                                <div>
                                    <label for="cellSelectModal" class="form-label"><fmt:message key="bookManager.label.cellModal"/></label>
                                </div>
                                <div>
                                    <select name="case"
                                    id="caseSelectModal"
                                    class="form-control"
                                    required
                                    value="${not empty param.caseNum? param.caseNum: ''}">
                                         <c:forEach begin="1" end="${bookStorage['case']}" varStatus="loop">
                                            <option value=${loop.count}>${loop.count}</option>
                                         </c:forEach>
                                    </select>
                                </div>
                                <div>
                                    <select name="shelf"
                                            id="shelfSelectModal"
                                            class = "form-control"
                                            required
                                            value="${not empty param.shelf? param.shelf: ''}">
                                         <c:forEach begin="1" end="${bookStorage['shelf']}" varStatus="loop">
                                            <option value=${loop.count}>${loop.count}</option>
                                         </c:forEach>
                                    </select>
                                </div>
                                <div>
                                    <select name="cell"
                                            id="cellSelectModal"
                                            class="form-control"
                                            required
                                            value="${not empty param.cell? param.cell: ''}">
                                         <c:forEach begin="1" end="${bookStorage['cell']}" varStatus="loop">
                                            <option value=${loop.count}>${loop.count}</option>
                                         </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class = "footerCenterMT1">

                                <a class="btn btn-primary m-auto" id="backStore" onClick="validateAndJump('store', 'publisher-tab')">
                                                            <fmt:message key="bookManager.label.prevButton"/></a>
                                <a class="btn btn-primary m-auto" id="saveModalButton"
                                onClick="submitForms();"
                                ><fmt:message key="bookManager.label.saveButton"/></a>
                            </div>

                        </div>
                    </div>
            </form>


    <div class="col-2">
     </div>
     <c:if test="${not empty errors}">
         <ul>
         <c:forEach items="${errors.keySet()}" var="error">
             <li>
             <div class="error">
             ${errors[error][language]}
             </div>
             </li>
         </c:forEach>
         </ul>
     </c:if>
</div>

<script type="text/javascript">

    function findAuthor(){
       const urlParams = new URLSearchParams(window.location.search);
       var searchStr = document.getElementById('findAuthorStr').value;
       urlParams.set('author', searchStr);
       window.location.search = urlParams;
       sessionStorage.setItem("tab", "profile-tab");
    }

    function findPublisher(){
       const urlParams = new URLSearchParams(window.location.search);
       var searchStr = document.getElementById('findPublisherStr').value;
       urlParams.set('publisher', searchStr);
       window.location.search = urlParams;
       sessionStorage.setItem("tab", "contact-tab");

    }



    function newAuthor(){
        if (document.getElementById("newContactData").style.display=="none")
            document.getElementById("newContactData").style.display="block";
        else
            document.getElementById("newContactData").style.display="none";

        document.getElementById("authorSelectModal").disabled =
            !document.getElementById("authorSelectModal").disabled;

        document.getElementById("authorSelectModal").hidden = !document.getElementById("authorSelectModal").hidden;

        console.log(document.getElementById("newAuthorButton").innerHTML);
        if(document.getElementById("newAuthorButton").innerHTML =='<fmt:message key="newBook.label.newAuthor"/>')
            document.getElementById("newAuthorButton").innerHTML ='<fmt:message key="newBook.label.chooseExisted"/>';
        else
            document.getElementById("newAuthorButton").innerHTML = '<fmt:message key="newBook.label.newAuthor"/>';

        document.getElementById("findAuthorStr").disabled=!document.getElementById("findAuthorStr").disabled;
        if (document.getElementById("findAuthorButton").classList.contains('disabled'))
            document.getElementById("findAuthorButton").classList.remove('disabled');
        else
            document.getElementById("findAuthorButton").classList.add('disabled');

    }


    function newPublisher(){
            if (document.getElementById("newPublisherData").style.display=="none")
                document.getElementById("newPublisherData").style.display="block";
            else
                document.getElementById("newPublisherData").style.display="none";

            document.getElementById("publisherSelectModal").disabled =
                !document.getElementById("publisherSelectModal").disabled;

            document.getElementById("publisherSelectModal").hidden = !document.getElementById("publisherSelectModal").hidden;

            if(document.getElementById("newPublisherButton").innerHTML =='<fmt:message key="newBook.label.newAuthor"/>')
                document.getElementById("newPublisherButton").innerHTML ='<fmt:message key="newBook.label.chooseExisted"/>';
            else
                document.getElementById("newPublisherButton").innerHTML = '<fmt:message key="newBook.label.newAuthor"/>';

            document.getElementById("findPublisherStr").disabled=!document.getElementById("findPublisherStr").disabled;
            if (document.getElementById("findPublisherButton").classList.contains('disabled'))
                document.getElementById("findPublisherButton").classList.remove('disabled');
            else
                document.getElementById("findPublisherButton").classList.add('disabled');
        }

        function newGenre(){
                    if (document.getElementById("newGenreData").style.display=="none")
                        document.getElementById("newGenreData").style.display="block";
                    else
                        document.getElementById("newGenreData").style.display="none";

                    document.getElementById("genreSelectModal").disabled =
                        !document.getElementById("genreSelectModal").disabled;

                    if(document.getElementById("newGenreButton").innerHTML =='<fmt:message key="newBook.label.newAuthor"/>')
                        document.getElementById("newGenreButton").innerHTML ='<fmt:message key="newBook.label.chooseExisted"/>';
                    else
                        document.getElementById("newGenreButton").innerHTML = '<fmt:message key="newBook.label.newAuthor"/>';
                }

        function validateAndJump(name, idToJump){
               document.getElementById(idToJump).click();
        }

        function submitForms(){
            var form = document.forms['general'];
            if (form.checkValidity() === false){
                try{
                   form.reportValidity();
                }catch(Error){ }

                }
                else{
                    form.submit();
                }


        }






</script>
