<div class="col rounded bgColor white mt-2">
   <div>
        <label class="mr-sm-2 margintop" for="inlineFormCustomSelect"> <fmt:message key="users.label.pagination"/></label>
        <select class="custom-select mr-sm-2" id="linesOnPage"
        name ="linesOnPage" onChange="setLinesOnPage();"
        value="${param.linesOnPage}">
            <option value="5"
            <c:if test ="${param.linesOnPage == 5}">
                selected="selected"
            </c:if>
            >5</option>
            <option value="10"
            <c:if test ="${param.linesOnPage == 10}">
                selected="selected"
            </c:if>

            >10</option>
        </select>
    </div>

    <div>
        <fmt:message key="catalog.label.page"/>
        <c:if test="${pagination!= null}">
            <c:forEach var = "i" begin = "1" end = "${pagination.totalPages}">
                <a href="javascript:setPage(${i})"
                   class =  <c:if test="${pagination.currentPage==i}">"currentPage"</c:if>
        >${i}&nbsp</a>
        </c:forEach>
        </c:if>
    </div>
    <c:if test = "${param.command=='catalog'}">
    <div>
        <fmt:message key="catalog.label.availableCount"/>: <tf:libraryStat/>
    </div>
    </c:if>
</div>


<script type="text/javascript">


 function setPage(pageNum){
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set('page', pageNum);
    window.location.search = urlParams;
    }

 function setLinesOnPage(){
     const urlParams = new URLSearchParams(window.location.search);
     var lines = document.getElementById('linesOnPage').value;
     urlParams.set('linesOnPage', lines);
     window.location.search = urlParams;
 }

 </script>