<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<script type="text/javascript">
$(document).ready(function()
{
	$(".configure_link").css("display","none");
	$(document).keypress(function(e)
	{
		switch(e.which)
		{
			// user presses "c"
			case 99:	showViaKeypress(".configure_link");
						break;	
		}
	});
});
//shows a given element and hides all others
function showViaKeypress(element_id)
{
	$(element_id).slideDown("slow");
}
</script>
<%
String msg = request.getParameter("msg");
String app = request.getParameter("app");
String expires = request.getParameter("expires");
if(msg != null && !"".equals(msg))
{
	if("success".equalsIgnoreCase(msg))
	{
		if(app != null && !"".equals(app))
		{
			msg = app + " authorization successful";
			if(expires != null)
			{
				int expiry = Integer.parseInt(expires);
				expiry = expiry/(60*60*24);
				msg = msg + " and will expire in " + expiry + " days.";
			}
		}
		else
		{
			msg = "Authorization successful.";
		}
	}
	else
	{
		if(app != null && !"".equals(app))
		{
			msg = "Error authorizing " + app + "," + msg + " .Please report to developer team.";
		}
		else
		{
			msg = "ERROR authorizing: " + msg + " .Please report to developer team.";
		}
	}
	pageContext.setAttribute("msg",msg);
}
%>
<P>
<h2 class="leftRightSpace">Websites:</h2>
<c:if test="${not empty msg}">
	<div class="messages" id="messages">
		<ul><li><span class="actionMessage">${msg}</span></li></ul>
	</div>
</c:if>
<div class="parentCHILD">
    <table id="firstDIV" class="wrapWithHeight" cellpadding="0" cellspacing="0">
	    <tbody>
	      <tr>
	        <td class="tpHeading" valign="top" width="10%"><spring:message code="websiteManagement.code" /></td>
	        <td class="tpHeading" valign="top" width="10%"><spring:message code="websiteManagement.sitekey" /></td>
	        <td class="tpHeading" valign="top" width="10%"><spring:message code="websiteManagement.name" /></td>
	        <td class="tpHeading" valign="top" width="10%"><spring:message code="websiteManagement.description" /></td>
	        <td class="tpHeading" valign="top" width="25%"><spring:message code="websiteManagement.siteurl" /></td>
	        <td class="tpHeading" valign="top" width="10%"><spring:message code="websiteManagement.accesstoken" /></td>
	        <td class="tpHeading" valign="top" width="5%"><spring:message code="websiteManagement.source" /></td>
	        <td class="tpHeading" valign="top" width="5%"><spring:message code="websiteManagement.active" /></td>
	        <td class="tpHeading" valign="top" width="5%"><spring:message code="websiteManagement.postasuser" /></td>
	        <td class="tpHeading" valign="top" width="10%"><spring:message code="websiteManagement.actions" /></td>
	     </tr>
    
	<%-- ========================================================= --%>
	<%-- Loop through WebsiteList --%>
	<%-- ========================================================= --%>
	<c:forEach items="${list}" var="website">
	      <tr>
	        <td valign="top">
	        	<span class="${fn:toLowerCase(website.socialAppName)}ExtraLargeIcon">&nbsp;</span>
	        </td>
      		<td valign="top">${website.socialAppId}</td>
	         <td valign="top">${website.name}</td>
	         <td valign="top">${website.description}&nbsp;</td>
	         <td valign="top"><a href="${website.siteUrl}" target="_blank">${website.siteUrl}</a>&nbsp;</td>
	         <td valign="top">${fn:substring(website.accessToken,15,60)}&nbsp;</td>
	         <td valign="top">${website.source}&nbsp;</td>
	         <td valign="top">${website.active}</td>
	         <td valign="top">${website.postasuser}</td>
	         <td valign="top">
	         	<%--
	         	<authz:authorize ifAllGranted="ROLE_ADMIN">
		         	<a  class="linkEdit" href='<c:url value="/addEditWebsite?websiteId=${website.id}&m=edit"/>'> 
		         		<spring:message code="websiteManagement.actions.edit" />
		         	</a>
		         	<a  class="linkDelete" href='<c:url value="/deleteWebsite?id=${website.id}&m=delete"/>'> 
		         		<spring:message code="websiteManagement.actions.delete" />
		         	</a>
		         </authz:authorize>
		          --%>
		         <authz:authorize ifAllGranted="ROLE_SUPERADMIN">
		         	<a  class="linkEdit" href='<c:url value="/addEditWebsite?websiteId=${website.id}&m=edit"/>'> 
		         		<spring:message code="websiteManagement.actions.edit" />
		         	</a>
		         	<a  class="linkDelete" href='<c:url value="/deleteWebsite?id=${website.id}&m=delete"/>'> 
		         		<spring:message code="websiteManagement.actions.delete" />
		         	</a>
		         </authz:authorize> 
		         <c:if test="${website.socialAppId ne '' and website.socialApiSecret ne ''}">
			     	<a class="linkEdit configure_link" href='<c:url value="/authorize/${fn:toLowerCase(website.socialAppName)}?id=${website.id}"/>'>Configure&nbsp;/</a>
			     </c:if>
			     <c:if test="${website.socialAppName eq 'FACEBOOK'}">
					<a class="linkEdit configure_link" href='<c:url value="/regenerateTopicAccessToken?id=${website.id}"/>'>Regenerate Topic Access Token&nbsp;/</a>
				</c:if>
				<c:if test="${not empty website.accessToken}">
			     	 <a class="thickbox linkEdit"href='<c:url value="/listPages?id=${website.id}&limit=200&offset=0&keepThis=true&amp;TB_iframe=true&amp;height=450&amp;width=500"/>'>Select Pages</a>&nbsp;
			     	 <authz:authorize ifAllGranted="ROLE_SUPERADMIN">/
			         	<a  class="linkEdit" href='<c:url value="/regeneratepages?id=${website.id}"/>'>Save all pages</a>
			         </authz:authorize>
		         </c:if>
	         </td>
	      </tr>
	</c:forEach>
	</tbody>
	</table>				
</div>
<div class="creatNewBu">
<%--
<authz:authorize ifAllGranted="ROLE_ADMIN">
	<a class="linkEdit" href='<c:url value="/addEditWebsite"/>'> <spring:message code="websiteManagement.createNewWebsite" /></a>
</authz:authorize>
 --%>
<authz:authorize ifAllGranted="ROLE_SUPERADMIN">
	<a class="linkEdit" href='<c:url value="/addEditWebsite"/>'> <spring:message code="websiteManagement.createNewWebsite" /></a>
</authz:authorize>
</div>
<%@ include file="/WEB-INF/views/footer.jsp" %>