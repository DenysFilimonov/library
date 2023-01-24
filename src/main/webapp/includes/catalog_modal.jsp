
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
                <label class="mr-sm-2 margintop" for="inlineFormCustomSelect">Books on page</label>
                      <select class="custom-select mr-sm-2" id="inlineFormCustomSelect" name ="linesOnPage">
                        <option selected value="1">1</option>
                        <option selected value="5">5</option>
                        <option value="10">10</option>
                      </select>
                </div>
                <div class="col">
                  <input type="submit" class="btn btn-primary" value=<fmt:message key="login.label.button"/>
                </div>
              </div>
            </form>

            <div class="${class_}">
                      <div class = "tableHeader"><fmt:message key="catalog.label.title"/></div>
                      <div class = "tableHeader"><fmt:message key="catalog.label.firstName"/></div>
                      <div class = "tableHeader"><fmt:message key="catalog.label.secondName"/></div>
                      <div class = "tableHeader"><fmt:message key="catalog.label.publisher"/></div>
                      <div class = "tableHeader"><fmt:message key="catalog.label.date"/></div>
                      <c:if test="${user!= null}">
                      <div class = "tableHeader"><fmt:message key="catalog.label.order"/></div>
                      </c:if>

                <c:forEach items="${books}" var="book">
                    <div class="tableCell">"${book.title}"</div>
                    <div class="tableCell">"${book.author.firstName}"</div>
                    <div class="tableCell">"${book.author.secondName}"</div>
                    <div class="tableCell">"${book.publisher.publisher}"</div>
                    <div class="tableCell">"${book.date}"</div>
                    <c:if test="${(user!= null)}">
                        <div class="tableCell">
                    </c:if>
                    <c:if test="${user!= null and book.availableQuantity>0 and orders[book.id]==null}">
                            <a target="_self" href="/Library/controller?command=orderBook&book=${book.id}" class="btn btn-primary">
                                 <fmt:message key="catalog.label.order"/>
                            </a>

                    </c:if>
                    <c:if test="${(user!= null)}">
                        </div>
                    </c:if>
                </c:forEach>
            </div>
            <div>
                <fmt:message key="catalog.label.page"/>
                <c:if test="${pagination!= null}">
                    <c:forEach var = "i" begin = "1" end = "${pagination.totalPages}">
                        <a href="/Library/controller?command=catalog&page=${i}"
                        class =  <c:if test="${pagination.currentPage==i}">"currentPage"</c:if>
                        >"${i}" </a>
                    </c:forEach>
                </c:if>
            </div>
</div>



