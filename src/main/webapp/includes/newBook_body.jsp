 <button type="button"
    id = "modalOpenButton"
    class="btn btn-primary"
    data-bs-toggle="modal"
    data-bs-target="#orderBook"
    hidden
 >
 </button>

<!-- Modal -->
<div class="modal fade" id="orderBook" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
    <form method="POST" action="/Library/controller?command=editBook">
      <div class="modal-header baseColor">
        <h5 class="modal-title" id="staticBackdropLabel"><fmt:message key="bookManager.label.modalTitle"/></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" id="closeModal"></button>
      </div>
        <div class="modalBody">

            <div class="listAndEditModal">
                <div class="col">
                    <img id="coverImage" class="cover"/>
                </div>
                <div class="col">

                <ul class="nav nav-tabs pt-4" id="myTab" role="tablist">
                  <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="home-tab"
                    data-bs-toggle="tab" data-bs-target="#home" type="button"
                    role="tab" aria-controls="home" aria-selected="true">
                    <fmt:message key="bookManager.label.generalBookData"/></button>
                  </li>
                  <li class="nav-item" role="presentation">
                    <button class="nav-link" id="profile-tab"
                    data-bs-toggle="tab" data-bs-target="#profile" type="button"
                    role="tab" aria-controls="profile" aria-selected="false">
                    <fmt:message key="bookManager.label.AuthorBookData"/></button>
                  </li>
                  <li class="nav-item" role="presentation">
                    <button class="nav-link" id="contact-tab"
                    data-bs-toggle="tab" data-bs-target="#contact" type="button"
                    role="tab" aria-controls="contact" aria-selected="false">
                    <fmt:message key="bookManager.label.PublisherBookData"/></button>
                  </li>
                  <li class="nav-item" role="presentation">
                    <button class="nav-link" id="contact-tab"
                    data-bs-toggle="tab" data-bs-target="#store" type="button"
                    role="tab" aria-controls="store" aria-selected="false">
                    <fmt:message key="bookManager.label.bookStore"/></button>
                  </li>
                </ul>
                <div class="tab-content pt-3" id="myTabContent">
                    <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                        <div class="formTwoRow">
                            <label for="idModal" class="form-label"><fmt:message key="bookManager.label.id"/></label>
                            <input type = "number" name ="id" id="idModal"  class="form-control" readonly="readonly"/>
                            <label for="isbnModal" class="form-label"><fmt:message key="bookManager.label.isbn"/></label>
                            <input type = "text" name ="isbn" id="isbnModal"  class="form-control"/>
                            <label for="titleModal_en" class="form-label"><fmt:message key="bookManager.label.titleEN"/></label>
                            <input type = "text" name ="titleEn" id="titleModal_en"  class="form-control"/>
                            <label for="titleModal_ua" class="form-label"><fmt:message key="bookManager.label.titleUA"/></label>
                            <input type = "text" name ="titleUa" id="titleModal_ua"  class="form-control"/>
                            <input type = "text" name ="cover" id="coverModal"  class="form-control" hidden/>
                            <label for="genreSelectModal" class="form-label"><fmt:message key="bookManager.label.genreModal"/></label>
                            <select name="genreId" id="genreSelectModal">
                                <option value="">--Please choose an option--</option>
                                 <c:forEach items="${genres.keySet()}" var="genre">
                                    <option value="${genres[genre].id}">${genres[genre].genre[language]}</option>
                                 </c:forEach>

                            </select>

                            <label for="quantityModal" class="form-label"><fmt:message key="bookManager.label.quantityModal"/></label>
                            <input type = "number" name ="quantity" id="quantityModal"  class="form-control"/>
                        </div>
                    </div>
                    <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                        <div class="formTwoRow">
                            <input type = "text" name ="authorId" id="authorIdModal" hidden/>

                            <label for="firstNameModal_en" class="form-label"><fmt:message key="bookManager.label.firstNameModalEn"/></label>
                            <input type = "text" name ="firstNameEn" id="firstNameModal_en"  class="form-control" readonly="readonly"/>

                            <label for="firstNameModal_ua" class="form-label"><fmt:message key="bookManager.label.firstNameModalUa"/></label>
                            <input type = "text" name ="firstNameUa" id="firstNameModal_ua"  class="form-control" readonly="readonly"/>

                            <label for="secondNameModal_en" class="form-label"><fmt:message key="bookManager.label.secondNameModal_en"/></label>
                            <input type = "text" name ="secondNameEn" id="secondNameModal_en"  class="form-control" readonly="readonly"/>

                            <label for="secondNameModal_ua" class="form-label"><fmt:message key="bookManager.label.secondNameModal_ua"/></label>
                            <input type = "text" name ="secondNameUa" id="secondNameModal_ua"  class="form-control" readonly="readonly"/>
                        </div>
                    </div>
                    <div class="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">
                        <div class="formTwoRow">
                            <input type = "text" name ="publisherId" id="publisherIdModal" hidden />
                            <label for="publisherModal_en" class="form-label"><fmt:message key="bookManager.label.publisherModal_en"/></label>
                            <input type = "text" name ="publisherEn" id="publisherModal_en"  class="form-control" readonly="readonly"/>
                            <label for="publisherModal_ua" class="form-label"><fmt:message key="bookManager.label.publisherModal_ua"/></label>
                            <input type = "text" name ="publisherUa" id="publisherModal_ua"  class="form-control" readonly="readonly"/>

                            <label for="countryModal_en" class="form-label"><fmt:message key="bookManager.label.countryModal_en"/></label>
                            <input type = "text" name ="countryEn" id="countryModal_en"  class="form-control" readonly="readonly"/>

                            <label for="countryModal_ua" class="form-label"><fmt:message key="bookManager.label.countryModal_ua"/></label>
                            <input type = "text" name ="countryUa" id="countryModal_ua"  class="form-control" readonly="readonly"/>

                            <label for="dateModal" class="form-label"><fmt:message key="bookManager.label.date"/></label>
                            <input type = "date" name ="date" id="dateModal"  class="form-control"/>
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
                                <select name="case" id="caseSelectModal">
                                     <c:forEach begin="1" end="${bookStorage['case']}" varStatus="loop">
                                        <option value=${loop.count}>${loop.count}</option>
                                     </c:forEach>
                                </select>
                            </div>
                            <div>
                                <select name="shelf" id="shelfSelectModal">
                                     <c:forEach begin="1" end="${bookStorage['shelf']}" varStatus="loop">
                                        <option value=${loop.count}>${loop.count}</option>
                                     </c:forEach>
                                </select>
                            </div>
                            <div>
                                <select name="cell" id="cellSelectModal">
                                     <c:forEach begin="1" end="${bookStorage['cell']}" varStatus="loop">
                                        <option value=${loop.count}>${loop.count}</option>
                                     </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
      <div class="modal-footer" >
        <div class = "footerThreeButton">
            <button type="cancel"
                    class="btn btn-primary m-auto"
                    id="cancelModalButton"
                    onClick="document.getElementById('closeModal').click();"
                    ><fmt:message key="bookManager.label.cancelButton" /></button>
            <button class="btn btn-danger m-auto"
                    id="deleteModalButton"
                    onClick="deleteButtonClick();"
                    >

                    <fmt:message key="bookManager.label.deleteButton"/></button>
            <button type="submit" class="btn btn-primary m-auto" id="saveModalButton"><fmt:message key="bookManager.label.saveButton"/></button>

        </div>
      </div>
    </form>
    </div>
  </div>
</div>

<script type="text/javascript">
    function deleteButtonClick(){
       const urlParams = new URLSearchParams(window.location.search);
       var bookId = document.getElementById('idModal').value;
       urlParams.set('delete', bookId);
       window.location.search = urlParams;
    }
</script>
