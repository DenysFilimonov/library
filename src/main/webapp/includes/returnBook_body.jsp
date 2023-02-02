    <div class="container col-6 bgColor rounded p-3">
    <form method="POST">
    <input type="number" value="${userBook.id}" name = "userBookId" hidden readonly='readonly'>
    <input type="number" value="${userBook.userId}" name = "userId" hidden readonly='readonly'>
    <input type="number" value="${userBook.bookId}" name = "bookId" hidden readonly='readonly'>


    <div class="container col-6">
            <div class="h4 text-center white"">
                   <fmt:message key="return.label.returnWarning"/> ${user.firstName} ${user.secondName}
            </div>
            <div class="h4 text-center white"">
                   <fmt:message key="return.label.bookStore"/> ${book.bookStore.caseNum} / ${book.bookStore.shelfNum} /${book.bookStore.cellNum}
            </div>
      </div>


    <div class="input-group pt-1">
          <div class="input-group-text w15">
                 <fmt:message key="issueBook.label.title"/>
          </div>
          <input type="text"
          name = "title"
          id="title"
          class="form-control"
          value = "${book.title[language]}"
          readonly='readonly'/>
    </div>

    <div class="input-group pt-1">
         <div class="input-group-text w15">
                <fmt:message key="issueBook.label.author"/>
         </div>
         <input type="text"
         name = "author"
         id="author"
         class="form-control"
         value = "${book.author.firstName[language]} ${book.author.secondName[language]}"
         readonly='readonly'/>
   </div>


  <div class="input-group pt-1">
            <div class="input-group-text w15">
                   <fmt:message key="issueBook.label.issueType"/>
            </div>
            <input type="text"
            name = "issueType"
            id="issueType"
            class="form-control"
            value  = "${userBook.issueType.issueType[language]}"
            readonly="readonly"
            />
    </div>
  <div class="input-group pt-1">
          <div class="input-group-text w15">
                 <fmt:message key="issueBook.label.issueDate"/>
          </div>
          <input type="date"
          name = "issueDate"
          id="issueDate"
          readonly
          value = <ex:DateFormat date="${userBook.issueDate}"/>
          class="form-control"
          />
  </div>

  <div class="input-group pt-1">
          <div class="input-group-text w15">
                 <fmt:message key="issueBook.label.targetDate"/>
          </div>
          <input type="date"
          name = "targetDate"
          id="targetDate"
          class="form-control"
          value  = <ex:DateFormat date="${userBook.targetDate}"/>
          readonly
          />

  </div>

    <div class="input-group pt-1">
            <div class="input-group-text w15">
                   <fmt:message key="returnBook.label.returnDate"/>
            </div>
            <input type="date"
            name = "returnDate"
            id="returnDate"
            class="form-control"
            value  = <ex:DateFormat date="${userBook.returnDate}"/>
            readonly
            />
    </div>

    <div class="input-group pt-1">
                <div class="input-group-text w15">
                       <fmt:message key="returnBook.label.fine"/>
                </div>
                <input type="num"
                name = "fine"
                id="fine"
                class="form-control"
                value  = ${userBook.getFineDays()}
                readonly
                />
                <c:if test="${userBook.getFineDays()>payment}">
                    <a href="javascript: getFine(${userBook.id}, ${userBook.getFineDays()})" class="btn btn-primary pl-2 ms-2">
                        <fmt:message key="returnBook.label.getFine"/>
                    </a>
                </c:if>
                <c:if test="${userBook.getFineDays()<=payment}">
                        <div class="input-group-text w15">
                               <fmt:message key="returnBook.label.paid"/>
                        </div>
                </c:if>

        </div>


  <div class="input-group pt-1">
            <br>
            <div class="input-group-text w15">
                   <fmt:message key="issueBook.label.isbn"/>
            </div>
            <input type="text"
            name = "isbn"
            id="isbn"
            class="form-control"
            required
            autofocus
            placeholder='<fmt:message key="returnBook.label.placeholderIsbn"/>'
            />
    </div>


  <div class="container col-3 pt-3">
                    <a href="javascript: cancelBook(${userBook.id})" class="btn btn-danger">
                        <fmt:message key="issueBook.label.cancelIssue"/>
                    </a>

                <input type ="submit" class="btn btn-primary"
                value=<fmt:message key="returnBook.label.return"/>
                <c:if test="${userBook.getFineDays()>payment}">
                    disabled
                </c:if>
                />
  </div>


  </div>
  </form>

    <div class="container col-2">
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

    document.getElementById('issueDate').value = '<ex:DateFormat date="${userBook.issueDate}"/>'
    document.getElementById('targetDate').value = '<ex:DateFormat date="${userBook.targetDate}"/>'

    function cancelBook(id){
        const urlParams = new URLSearchParams(window.location.search);
        urlParams.set('cancelBook', id);
        urlParams.delete('returnBook');
        window.location.search = urlParams;
    }

    function getFine(id, amount){
        const urlParams = new URLSearchParams(window.location.search);
        urlParams.set('userBookId', id);
        urlParams.set('amount', amount);
        window.location.search = urlParams;
    }

</script>
