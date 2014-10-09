<%@ include file="/WEB-INF/views/includes.jsp" %>
<link rel="stylesheet" type="text/css" media="all" href="resources/css/project_v2.css">
<title>Social Audience Manager</title>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td colspan="2" height="5"></td></tr>
  <tr><td colspan="2" height="5"></td></tr>
  <tr>
    <td width="250" align="center" style="padding:10px"><a href='<c:url value="/welcome"/>'/><img src="images/5239805.cms.gif" width="228" height="37" /></a></td>
    <td align="center" style="padding:10px" colspan="2"><div class="headerBoldSecond">Social Audience Manager</div></td>
  </tr>
  <tr><td colspan="2" height="10"></td></tr>
  <tr><td colspan="3"><hr/></td></tr>
</table>

<h2>Sorry, access is denied</h2>

<p>
<%--= request.getAttribute(AccessDeniedHandlerImpl.ACEGI_SECURITY_ACCESS_DENIED_EXCEPTION_KEY)--%>

<p>

<%--		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) { %>
			Authentication object as a String: <%= auth.toString() %><BR><BR>
<%      } --%>
<%@ include file="/WEB-INF/views/footer.jsp" %>