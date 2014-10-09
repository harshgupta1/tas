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
String sucessMsg = request.getParameter("sucessMsg");
if(sucessMsg != null && !"".equals(sucessMsg))
{
	pageContext.setAttribute("sucessMsg",sucessMsg);
}
%>
<%--
<c:if test="${param.m == \"new\"}">New </c:if>
--%>


<H2 style="padding: 10px 10px 0px;"> 
<% if(isnew) { %> New <% }else{ %> Edit <%}%>User:</H2>
<c:if test="${not empty sucessMsg}">
	<div class="messages" id="messages">
		<ul><li><span class="actionMessage">${sucessMsg}</span></li></ul>
	</div>
</c:if>
<form:form method="post" modelAttribute="user" name="user">
  <table width="98%" border="0" cellspacing="0" cellpadding="5" style="border:1px solid #52829C; background:#f8f8ff" class="edittable" align="center">
	<tr>
      <td align="right" width="25%" class="boldFont"><span style="color:red">*</span>UserId/Email Address :</td>
        <td width="25%">
	      <c:if test="${username ne user.emailAddress}">
	          <form:input path="emailAddress"/>
          </c:if>
          <c:if test="${username eq user.emailAddress}">
	          <form:input path="emailAddress" readonly="true"/>
	      </c:if>
        </td>
        <td width="50%">
          <form:errors path="emailAddress" cssClass="error"/>
        </td>
    </tr>    <tr>
      <td align="right" width="25%" class="boldFont"><span style="color:red">*</span>Password :</td>
        <td width="25%">
          <form:password path="passphrase"  showPassword="true"/>
        </td>
        <td width="50%">
          <form:errors path="passphrase" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" width="25%" class="boldFont"><span style="color:red">*</span>Full Name :</td>
        <td width="25%">
          <form:input path="fullName"/>
        </td>
        <td width="50%">
          <form:errors path="fullName" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" width="25%" class="boldFont">Mobile Number :</td>
        <td width="25%">
          <form:input path="mobileNumber"/>
        </td>
        <td width="50%">
          <form:errors path="mobileNumber" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" width="25%" class="boldFont">Secondary Email Address :</td>
        <td width="25%">
          <form:input path="secondaryEmailAddress"/>
        </td>
        <td width="50%">
          <form:errors path="secondaryEmailAddress" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" width="25%" class="boldFont"><span style="color:red">*</span>Website :</td>
        <td width="25%">
          <c:if test="${username eq user.emailAddress}">
			<form:select path="websiteids" disabled="true" multiple="true">
				<form:options items="${websiteCol}" itemLabel="name" itemValue="id" />
			</form:select>
          </c:if>
          <c:if test="${username ne user.emailAddress}">
			<form:select path="websiteids" multiple="true">
				<form:options items="${websiteCol}" itemLabel="name" itemValue="id" />
			</form:select>
          </c:if>
        </td>
        <td width="50%">
          
        </td>
    </tr>
    
	<tr>
      <td align="right" width="25%" class="boldFont"><span style="color:red">*</span>Role :</td>
        <td width="25%">
          <c:if test="${username eq user.emailAddress}">
			<form:select path="role.id" disabled="true">
				<form:options items="${roleCol}" itemLabel="roleName" itemValue="id" />
			</form:select>
          </c:if>
          <c:if test="${username ne user.emailAddress}">
          	<form:select path="role.id">
				<form:options items="${allowedRoleCol}" itemLabel="roleName" itemValue="id" />
			</form:select>
          </c:if>
        </td>
        <td width="50%">
          
        </td>
    </tr>
    <tr>
      <td align="right" width="25%" class="boldFont"><span style="color:red">*</span>Valid From :</td>
        <td width="25%">
            <c:if test="${username eq user.emailAddress}">
          		<form:input path="validFrom" readonly="true"/>
          	</c:if>
          	<c:if test="${username ne user.emailAddress}">
          		<form:input path="validFrom" cssClass="date-pick dp-applied"/>
          	</c:if>
          	
        </td>
        <td width="50%">
          <form:errors path="validFrom" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" width="25%" class="boldFont"><span style="color:red">*</span>Valid Till :</td>
        <td width="25%">
            <c:if test="${username eq user.emailAddress}">
	          	<form:input path="validTill" readonly="true"/>
	        </c:if>
          	<c:if test="${username ne user.emailAddress}">
          		<form:input path="validTill" cssClass="date-pick dp-applied"/>
          	</c:if>
          	<form:hidden path="decrypted"/>
        </td>
        <td width="50%">
          <form:errors path="validTill" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" width="25%" class="boldFont">Enabled :</td>
        <td width="25%">
        	<c:if test="${username eq user.emailAddress}">
	          	<form:checkbox path="enabled" disabled="true"/>
	        </c:if>
          	<c:if test="${username ne user.emailAddress}">
	          	<form:checkbox path="enabled"/>
          	</c:if>
        </td>
        <td width="50%">
          <form:errors path="enabled" cssClass="error"/>
        </td>
    </tr>
    <tr><td colspan="10"><input type="submit" align="center" value="Save" class="btn" onmouseover="this.className='btnhov'" onmouseout="this.className='btn'"></td></tr>
  </table>
  
</form:form>
<%@ include file="/WEB-INF/views/footer.jsp" %>
