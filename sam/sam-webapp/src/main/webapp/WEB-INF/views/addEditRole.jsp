<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<%
String mode = request.getParameter("m") ;
boolean isnew = true ;
if(mode != null) {
    if(mode.equalsIgnoreCase("edit")) {
        isnew = false ;
    }
}
%>
<c:set var="mode" value="<%= mode %>"/>
<H2 style="padding:10px 10px 0px 10px"> 
<% if(isnew) { %> New <% }else{ %> Edit <%}%>Role:</H2>
<c:if test="${not empty sucessMsg}">
	<div class="messages" id="messages">
		<ul><li><span class="actionMessage">${sucessMsg}</span></li></ul>
	</div>
</c:if>
<form:form method="post" modelAttribute="role">
  <table width="98%" border="0" cellspacing="0" cellpadding="5" style="border:1px solid #52829C; background:#f8f8ff" class="edittable" align="center">
    <tr>
      <td align="right" width="25%" class="boldFont">Role Name :</td>
        <td width="25%">
        	<c:if test="${mode eq null}">
          		<form:input path="roleName"/>
          	</c:if>
          	<c:if test="${mode eq 'edit'}">
          		<form:input path="roleName" disabled="true"/>
          	</c:if>
        </td>
        <td width="50%">
          <form:errors path="roleName" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" width="25%" class="boldFont">Role Description :</td>
        <td width="25%">
          <form:textarea path="roleDescription" rows="5" cols="30"/>
        </td>
        <td width="50%">
          <form:errors path="roleDescription" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" width="25%" class="boldFont">Role Sort Order:</td>
        <td width="25%">
          <form:input path="sortOrder"/>
        </td>
        <td width="50%">
          <form:errors path="sortOrder" cssClass="error"/>
        </td>
    </tr>
    <tr><td colspan="5"><input type="submit" align="center" value="Save" class="btn" onmouseover="this.className='btnhov'" onmouseout="this.className='btn'"></td></tr>
  </table>
  <br/>
  
</form:form>


<%@ include file="/WEB-INF/views/footer.jsp" %>
