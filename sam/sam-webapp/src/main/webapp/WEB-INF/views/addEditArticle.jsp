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

<P>
<H2 style="padding:10px 10px 0px 10px"> 
<% if(isnew) { %> New <% }else{ %> Edit <%}%> Article :</H2>
<c:if test="${not empty sucessMsg}">
	<div class="messages" id="messages">
		<ul><li><span class="actionMessage">${sucessMsg}</span></li></ul>
	</div>
</c:if>
<form:form method="post" modelAttribute="article" name="article">
<form:hidden path="id"/>
  <table width="98%" border="0" cellspacing="0" cellpadding="5" style="border:1px solid #52829C; background:#f8f8ff" class="edittable" align="center">
  	<tr>
      <td align="right" class="boldFont">Website Name:</td>
        <td>
          <form:select path="website.id" id="articlewebsiteid" disabled="<%=String.valueOf(!isnew) %>">
          	<form:options items="${websiteCol}" itemValue="id" itemLabel="name"/>
          </form:select>
        </td>
        <td>&nbsp;</td>
    </tr>
  	<tr>
	    <td align="right" width="20%" class="boldFont">Social Application:</td>
	    <td width="35%">
	      	<form:select path="website.socialAppName" disabled="<%=String.valueOf(!isnew) %>">
	       		<form:option value="FACEBOOK">FACEBOOK</form:option>
	       		<form:option value="TWITTER">TWITTER</form:option>
			</form:select>
	    </td>
	    <td width="45%">&nbsp;</td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Topic Name</td>
        <td>
          <form:input path="topicPage.entityName" disabled="<%=String.valueOf(!isnew) %>"/>
        </td>
        <td>
          <form:errors path="topicPage.entityName" cssClass="error" />
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Message:</td>
        <td>
          <form:input path="message" size="40"/>
        </td>
        <td>
          <form:errors path="message" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Link/Site Url:</td>
        <td>
          <form:input path="url" size="80"/>
        </td>
        <td>
          <form:errors path="url" cssClass="error" />
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Processed:</td>
        <td>
          <form:checkbox path="processed" disabled="true"/>
        </td>
        <td>
          <form:errors path="processed" cssClass="error"/>
        </td>
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

<%@ include file="/WEB-INF/views/footer.jsp" %>
