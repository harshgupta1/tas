<%@ page import="org.springframework.dao.DataAccessException"%>
<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<%
Exception ex = (Exception) request.getAttribute("exception");
%>

<H2>Data access failure: <%= ex.getMessage() %></H2>
<P>


<%
ex.printStackTrace(new java.io.PrintWriter(out));
%>

<P>
<br/>
<A href="<c:url value="/welcome.htm"/>">Home</A>

<%@ include file="/WEB-INF/views/footer.jsp" %>
