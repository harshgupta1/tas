<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<H2 style="padding:10px 10px 0px 10px">Role:</H2>
<div class="space10LR">
<TABLE border="0" class="stattable" cellpadding="0" cellspacing="0">
<tr>
   <th class="stattable" width="10%">
        <spring:message code="roleManagement.roleName" />
   </th>
   <th class="stattable">
        <spring:message code="roleManagement.roleDescription" />
   </th>
   <th class="stattable" width="10%">
        <spring:message code="roleManagement.actions" />
   </th>
</tr>
    
	<%-- ========================================================= --%>
	<%-- Loop through comments --%>
	<%-- ========================================================= --%>
   
	<c:forEach items="${list}" var="role">
		<tr>
		    <td>
                <span > ${role.roleName} </span>
            </td>
		    
		    <td>
		        <span > ${role.roleDescription} </span>
		    </td>
			
			<td>&nbsp;
		        <span>
		        	<authz:authorize ifAllGranted="ROLE_SUPERADMIN"> 
		        		<a href='<c:url value="/addEditRole?id=${role.id}"/>&m=edit'> <spring:message code="websiteManagement.actions.edit" /></a>
		        		<a href='<c:url value="/deleteRole?id=${role.id}"/>&m=delete'> <spring:message code="websiteManagement.actions.delete" /></a>
		        	</authz:authorize> 
		        </span>
		    </td>
		</tr>
	</c:forEach>
    <tr>
        <td colspan="5">
            <authz:authorize ifAllGranted="ROLE_SUPERADMIN">
            	<a class="linkEdit" href='<c:url value="/addEditRole"/>'> <spring:message code="roleManagement.createNewRole" /></a>
            </authz:authorize>
        </td>
    </tr>
</TABLE>
</div>
<%@ include file="/WEB-INF/views/footer.jsp" %>