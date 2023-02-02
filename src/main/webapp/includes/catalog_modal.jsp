<!-- Modal -->
<div class="modal fade" id="orderBook" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <form method=post>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header baseColor">
        <h5 class="modal-title" id="staticBackdropLabel"><fmt:message key="catalog.label.confirmation"/></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
         <div>
            <h4 id ="titleModal"></h4>
            <h5 id ="firstNameModal"></div>
        <div class="modalBody">
            <div>
                <img id="coverImage" class="cover"/>
            </div>
            <div>
                <span id = "publisherModal"></span>
            </div>
        </div>



                <input type="text" id ="book" name = "book" hidden/>



                </div>
              <div class="modal-footer" >
              <div class="text-left">
                            <input class="form-check-input" type="radio" name="issueType" value="subscription"  id="subscriptionCB">
                            <label class="form-check-label" for="flexRadioDefault1">
                              <fmt:message key="catalog.label.subscription"/>
                            </label>
                          </div>
                      <div class="text-left">
                            <input class="form-check-input" type="radio" name="issueType" value="reading room" id="subscriptionCB1" checked>
                            <label class="form-check-label" for="flexRadioDefault2">
                              <fmt:message key="catalog.label.readingRoom"/>
                            </label>
                      </div>
                <button type="submit" class="btn btn-primary m-auto"><fmt:message key="catalog.label.order"/></button>
              </div>
      </div>

    </div>
  </div>
  </form>
</div>