<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ include file="/WEB-INF/views/header.jsp" %>


<H2 style="padding:10px 10px 0px 10px">Users:</H2>
<TABLE border="0" id="firstDIV" class="wrapWithHeight" cellpadding="0" cellspacing="0">
<tr>
   <td class="tpHeading" valign="top" width="9%">
        <spring:message code="userManagement.fullname" />
   </td>
   <td class="tpHeading" valign="top" width="13%">
        <spring:message code="userManagement.emailaddress" />
   </td>
   <td class="tpHeading" valign="top" width="13%">
        <spring:message code="userManagement.secondaryemailaddress" />
   </td>
   <td class="tpHeading" valign="top" width="8%">
        <spring:message code="userManagement.mobilenumber" />
   </td>
   <td class="tpHeading" valign="top" width="3%">
        <spring:message code="userManagement.enabled" />
   </td>
   <td class="tpHeading" valign="top" width="9%">
        <spring:message code="userManagement.validfrom" />
   </td>
   <td class="tpHeading" valign="top" width="9%">
        <spring:message code="userManagement.validtill" />
   </td>
   <td class="tpHeading" valign="top" width="9%">
        <spring:message code="userManagement.rolename" />
   </td>
   <td class="tpHeading" valign="top" width="9%">
        <spring:message code="userManagement.websitename" />
   </td>
   <td class="tpHeading" valign="top" width="9%">
        <spring:message code="userManagement.createdate" />
   </td>
   <td class="tpHeading" valign="top" width="9%">
        <spring:message code="websiteManagement.actions" />
   </td>
</tr>
    
	<%-- ========================================================= --%>
	<%-- Loop through comments --%>
	<%-- ========================================================= --%>
   
	<c:forEach items="${list}" var="user">
		<tr>
			<td width="9%">${user.fullName}&nbsp;</td>
		    <td width="13%">${user.emailAddress}</td>
		    <td width="13%">${user.secondaryEmailAddress}&nbsp;</td>
		    <td width="8%">${user.mobile}&nbsp;</td>
		    <td width="3%">${user.enabled}</td>
		    <td  width="9%">${user.validFrom}</td>
		    <td  width="9%">${user.validTill}</td>
		    <td  width="9%">${user.roleName}</td>
		    <td  width="9%">${user.websiteName}</td>
		    <td  width="9%">${user.createdate}</td>
			<td width="9%">
				<authz:authorize ifAllGranted="ROLE_ADMIN">
 					<c:if test="${username eq user.emailAddress or rightlevel lt user.rightLevel}"> 
			        	<span > <a href='<c:url value="/addEditUser?id=${user.id}&r=${user.roleId}&w=${user.websiteId}"/>&m=edit'> <spring:message code="websiteManagement.actions.edit"/></a> </span>
			        </c:if>
		        </authz:authorize>
		        <authz:authorize ifAllGranted="ROLE_SUPERADMIN">
		        	<c:if test="${username eq user.emailAddress or rightlevel lt user.rightLevel}"> 
			        	<span > <a href='<c:url value="/addEditUser?id=${user.id}"/>&r=${user.roleId}&w=${user.websiteId}&m=edit'> <spring:message code="websiteManagement.actions.edit"/></a> </span>
			        </c:if>
		        </authz:authorize>
		        
		        <c:if test="${username eq user.emailAddress}"> 
		        	<c:if test="${role eq 'ROLE_AUTHOR' or role eq 'ROLE_EDITOR' }">
						<span > <a href='<c:url value="/editUser?id=${user.id}"/>&r=${user.roleId}&w=${user.websiteId}&m=edit'> <spring:message code="websiteManagement.actions.edit"/></a> </span>
					</c:if>
				</c:if>
				&nbsp;
		    </td>
		</tr>
	</c:forEach>
    <tr>
		<authz:authorize ifAllGranted="ROLE_ADMIN">
        <td colspan="11">
            <a class="linkEdit" href='<c:url value="/addEditUser"/>'> <spring:message code="userManagement.createNewUser" /></a>
        </td>
        </authz:authorize>
        <authz:authorize ifAllGranted="ROLE_SUPERADMIN">
        <td colspan="11">
            <a class="linkEdit" href='<c:url value="/addEditUser"/>'> <spring:message code="userManagement.createNewUser" /></a>
        </td>
        </authz:authorize>
    </tr>
</TABLE>

<%@ include file="/WEB-INF/views/footer.jsp" %>