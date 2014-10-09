<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<%
String mode = request.getParameter("m") ;
boolean isnew = true ;
if(mode != null) {
    if(mode.equalsIgnoreCase("edit")) {
        isnew = false ;
        pageContext.setAttribute("mode",mode);
    }
}

String sucessMsg = request.getParameter("sucessMsg");
if(sucessMsg != null && !"".equals(sucessMsg))
{
	pageContext.setAttribute("sucessMsg",sucessMsg);
}
Website web = (Website)request.getAttribute("website");
//System.out.println(web.isPostasuser());
%>

<P>
<H2 style="padding:10px 10px 0px 10px"> 
<% if(isnew) { %> New <% }else{ %> Edit <%}%> Website :</H2>
<c:if test="${not empty sucessMsg}">
	<div class="messages" id="messages">
		<ul><li><span class="actionMessage">${sucessMsg}</span></li></ul>
	</div>
</c:if>
<form:form method="post" modelAttribute="website" name="webSite">

  <table width="98%" border="0" cellspacing="0" cellpadding="5" style="border:1px solid #52829C; background:#f8f8ff" class="edittable" align="center">
  	<tr>
	    <td align="right" width="20%" class="boldFont">Social Application:</td>
	    <td width="35%">
	      	<form:select path="socialAppName">
	       		<form:option value="FACEBOOK">FACEBOOK</form:option>
	       		<form:option value="TWITTER">TWITTER</form:option>
	       		<form:option value="LINKEDIN">LINKEDIN</form:option>
	       		<form:option value="GOOGLEPLUS">GOOGLEPLUS</form:option>
			</form:select>
			<input type="hidden" value="<%= request.getParameter("m")%>">
			<form:hidden path="id"/>
	    </td>
	    <td width="45%">&nbsp;</td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Name:</td>
        <td>
          <form:input path="name" id="name"/>
        </td>
        <td>
          <form:errors path="name" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Description:</td>
        <td>
          <form:textarea path="description" id="description" rows="5" cols="30"/>
        </td>
        <td>
          <form:errors path="description" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Social App/API Id:</td>
        <td>
          <form:input path="socialAppId"/>
        </td>
        <td>
          <form:errors path="socialAppId" cssClass="error" />
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Social API Secret Key:</td>
        <td>
          <form:input path="socialApiSecret" size="40"/>
        </td>
        <td>
          <form:errors path="socialApiSecret" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Website Url:</td>
        <td>
          <form:input path="siteUrl" size="40"/>
        </td>
        <td>
          <form:errors path="siteUrl" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Contact Email Address:</td>
        <td>
          <form:input path="contactEmail" size="40"/>
        </td>
        <td>
          <form:errors path="contactEmail" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">From Email Address:</td>
        <td>
          <form:input path="emailFromAddress" size="40"/>
        </td>
        <td>
          <form:errors path="emailFromAddress" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">AccessToken:</td>
        <td>
          <form:input path="accessToken" size="80"/>
        </td>
        <td>
         	<form:errors path="accessToken" cssClass="error" />
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">PageViews api:</td>
        <td>
          <form:input path="pageviewsapi" size="60"/>
        </td>
        <td>
         	<form:errors path="pageviewsapi" cssClass="error" />
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Shortener api:</td>
        <td>
          <form:input path="shortenerapi" size="60"/>
        </td>
        <td>
         	<form:errors path="shortenerapi" cssClass="error" />
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Source:</td>
        <td>
         	<form:select path="source">
	       		<form:option value=""></form:option>
	       		<form:option value="Times of India">Times of India</form:option>
	       		<form:option value="Economic Times">Economic Times</form:option>
	       		<form:option value="Technoholik">Technoholik</form:option>
	       		<form:option value="Government Fleet">Government Fleet</form:option>
	       		<form:option value="iDiva.com">iDiva.com</form:option>
			</form:select>
        </td>
        <td>&nbsp;</td>
    </tr>
    <tr>
      <td align="right" class="boldFont">SiteCode:</td>
        <td>
         	<form:input path="sitecode" size="20"/>
        </td>
        <td><form:errors path="sitecode" cssClass="error" /></td>
    </tr>
     <tr>
      <td align="right" class="boldFont">SocialCode:</td>
        <td>
         	<form:input path="socialcode" size="20"/>
        </td>
        <td><form:errors path="socialcode" cssClass="error" /></td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Active:</td>
        <td>
          <form:checkbox path="active"/>
        </td>
        <td>&nbsp;</td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Post As User:</td>
        <td>
          <form:checkbox path="postasuser" onclick="document.getElementById('userid').disabled = !this.checked;"/>
        </td>
        <td>&nbsp;</td>
    </tr>
    <tr>
      <td align="right" class="boldFont">User ID:</td>
        <td>
        	<form:input path="userid" size="40" disabled="${postasuser}"/>
        </td>
        <td>This userid is automatically generated using long-lived access token while configuring app and is used for "select pages/regenerate pages/regenerate topic AccessToken".</td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Regenerate Token:</td>
        <td>
        	<form:checkbox path="regeneratetoken"/>
        </td>
        <td>This property is used in case of Facebook Profiles where token is expired after 60 days</td>
    </tr>
    <tr>
	  	<td colspan="3">
		  	<span>
	  		<input type="submit" align="center" value="Save" class="btn" onmouseover="this.className='btnhov'" onmouseout="this.className='btn'">
	  		</span>
	  	</td>
  	</tr>  
  </table>
  <br/>
</form:form>
<script>
document.getElementById('userid').disabled=!document.getElementById("postasuser1").checked;
</script>
<%@ include file="/WEB-INF/views/footer.jsp" %>
