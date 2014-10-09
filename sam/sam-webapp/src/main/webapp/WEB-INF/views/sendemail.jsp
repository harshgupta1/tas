<%@ include file="/WEB-INF/views/includes.jsp" %>
<c:choose>
    <c:when test="${empty requestScope.errors}">
    </c:when>
    <c:otherwise>
        <script>window.parent.result('failure:<c:out value="${requestScope.errors}" />')</script>
    </c:otherwise>
</c:choose>
