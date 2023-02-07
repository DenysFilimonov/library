    <div class="container col-8 bgColor rounded p-3">
    <form method="POST">
    <input type="number" value="${userBook.id}" name = "userBookId" hidden readonly='readonly'>
    <input type="number" value="${userBook.userId}" name = "userId" hidden readonly='readonly'>
    <input type="number" value="${userBook.bookId}" name = "bookId" hidden readonly='readonly'>

    <div class="container col-12 bgColor rounded ">
          <div class="h4 text-center white">
            <fmt:message key="IssueBook.label.header" />
          </div>
    </div>
    <div class="container col-12">
            <div class="h4">
                   <fmt:message key="issueBook.label.issueWarning"/> ${reader.firstName} ${reader.secondName}, ID ${reader.id}
            </div>
            <div class="h4">
                   <fmt:message key="issueBook.label.bookStore"/>: ${book.bookStore.caseNum} / ${book.bookStore.shelfNum} /${book.bookStore.cellNum}
            </div>

      </div>


    <div class="input-group mt-2">
          <div class="input-group-text w15">
                 <fmt:message key="issueBook.label.title"/>
          </div>
          <input type="text"
          name = "title"
          id="title"
          class="form-control"
          value = "${book.title[language]}"
          readonly='readonly'>
    </div>

    <div class="input-group mt-2">
         <div class="input-group-text w15">
                <fmt:message key="issueBook.label.author"/>
         </div>
         <input type="text"
         name = "author"
         id="author"
         class="form-control"
         value = "${book.author.firstName[language]} ${book.author.secondName[language]}"
         readonly='readonly'>
   </div>

    <div class="input-group mt-2">
         <div class="input-group-text w15">
                <fmt:message key="issueBook.label.publisher"/>
         </div>
         <input type="text"
         name = "publisher"
         id="publisher"
         class="form-control"
         value = "${book.publisher.publisher[language]} ${book.publisher.country[language]}"
         readonly='readonly'>
   </div>

  <div class="input-group mt-2">
            <div class="input-group-text w15">
                   <fmt:message key="issueBook.label.issueType"/>
            </div>
            <input type="text"
            name = "issueType"
            id="issueType"
            class="form-control"
            value  = "${userBook.issueType.issueType[language]}"
            readonly="readonly"
            >
    </div>
  <div class="input-group mt-2">
          <div class="input-group-text w15">
                 <fmt:message key="issueBook.label.issueDate"/>
          </div>
          <input type="date"
          name = "issueDate"
          id="issueDate"
          value = <ex:DateFormat date="${userBook.issueDate}"/>
          class="form-control"
          >
  </div>

  <div class="input-group mt-2">
          <div class="input-group-text w15">
                 <fmt:message key="issueBook.label.targetDate"/>
          </div>
          <input type="date"
          name = "targetDate"
          id="targetDate"
          class="form-control"
          value  = <ex:DateFormat date="${userBook.targetDate}"/>

          <c:if test="${userBook.issueType.id}==2">
              readonly='readonly'
          </c:if>
          >
  </div>

  <div class="input-group mt-2 error">
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
            placeholder=<fmt:message key="issueBook.label.placeholderIsbn"/>
            >
    </div>


  <div class="container col-3 pt-3">
                    <a href="javascript: cancelIssue(${userBook.id})" class="btn btn-danger">
                        <fmt:message key="issueBook.label.cancelIssue"/>
                    </a>

                <input type ="submit" class="btn btn-primary" value=<fmt:message key="issueBook.label.issue"/>>
  </div>

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

  </div>
  </form>



<script type="text/javascript">

document.getElementById('issueDate').value = '<ex:DateFormat date="${userBook.issueDate}"/>'
document.getElementById('targetDate').value = '<ex:DateFormat date="${userBook.targetDate}"/>'

function cancelIssue(id){
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set('cancelIssue', id);
    urlParams.delete('issueOrderId');
    window.location.search = urlParams;
}

function convertDate(inputFormat) {
  function pad(s) { return (s < 10) ? '0' + s : s; }
  var d = new Date(inputFormat)
  return [pad(d.getDate()), pad(d.getMonth()+1), d.getFullYear()].join('/')
}


</script>
