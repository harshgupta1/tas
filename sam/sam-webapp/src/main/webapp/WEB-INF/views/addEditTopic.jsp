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
%>
<script type="text/javascript">
$(document).ready(function() {
    $('#entityName').live('change', function() {
        $.ajax({
            url: "https://api.twitter.com/1/statuses/user_timeline.json?screen_name=" + jQuery.trim($("#entityName").val()) + "&count=1&callback=?",
            dataType: 'json',
            success: function(data)
            {
            	if(data && data[0] && data[0].user.id!="" && data[0].user.id!="0") {
		   			$('#pageId').val(data[0].user.id);
            	} else {
                	$.get("getid", { username: $("#entityName").val(), wid: $("#website").val()}, function(msg) {
            			if(msg.id!="" && msg.id!="0") {
            				$('#pageId').val(msg.id);
            			}
            		});
            	}
            },
            error: function(xhr, ajaxOptions, thrownError)
            {
            	$.get("getid", {
            		username: $("#entityName").val(), 
            		wid: $("#website").val()
           		}, function(msg) {
        			if(msg.id!="" && msg.id!="0") {
        				$('#pageId').val(msg.id);
        			}
        		});
            }
          });
    });
});
</script>

<P>
<H2 style="padding:10px 10px 0px 10px"> 
<% if(isnew) { %> New <% }else{ %> Edit <%}%> TopicPage :</H2>
<c:if test="${not empty sucessMsg}">
	<div class="messages" id="messages">
		<ul><li><span class="actionMessage">${sucessMsg}</span></li></ul>
	</div>
</c:if>
<form:form method="post" modelAttribute="topicPage" name="topicPage">

  <table width="98%" border="0" cellspacing="0" cellpadding="5" style="border:1px solid #52829C; background:#f8f8ff" class="edittable" align="center">
  	<tr>
      <td align="right" class="boldFont" width="21%">Website Name:</td>
      <td width="50%">
        <c:if test="${not empty mode}">
        	<form:select path="website.id" id="website" disabled="true">
        		<c:forEach items="${websiteCol}" var="site">
        			<form:option value="${site.id}">${site.name}</form:option>
        		</c:forEach>
			</form:select>
        </c:if>
        <c:if test="${empty mode}">
        	<form:select path="website.id" id="website">
        		<c:forEach items="${websiteCol}" var="site">
        			<form:option value="${site.id}">${site.name}</form:option>
        		</c:forEach>
			</form:select>
        </c:if>  
        <input type="hidden" value="<%= request.getParameter("m")%>">
        </td>
        <td>
          
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Topic Name</td>
        <td>
          <form:input path="entityName" size="30"/> (topic name should not contain spaces & cannot be updated e.g Mark-Zuckerberg, IBM)
        </td>
        <td>
          <form:errors path="entityName" cssClass="error" />
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Topic Url:</td>
        <td>
          <form:input path="url" size="40"/>
        </td>
        <td>
          <form:errors path="url" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Email Address:</td>
        <td>
          <form:input path="email" size="40"/>
        </td>
        <td>
          <form:errors path="email" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">${topicPage.website.socialAppName} PageId:</td>
        <td>
          <form:input path="pageId"/>
        </td>
        <td>
          <form:errors path="pageId" cssClass="error" />
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">PageName:</td>
        <td>
          <form:input path="pageName" size="30"/>
        </td>
        <td>
          <form:errors path="pageName" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Accesstoken:</td>
        <td>
        	<authz:authorize ifAllGranted="ROLE_SUPERADMIN">
          		<form:input path="accessToken" size="60"/>
          	</authz:authorize>
          	<authz:authorize ifAllGranted="ROLE_ADMIN,ROLE_AUTHOR,ROLE_EDITOR">
          		<form:input path="accessToken" readonly="true" size="60"/>
          	</authz:authorize>
        </td>
        <td>
          <form:errors path="accessToken" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Picture URL:</td>
        <td>
          <form:input path="picture" size="60"/>
        </td>
        <td>
          <form:errors path="picture" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Page Link:</td>
        <td>
          <form:input path="link" size="60"/>
        </td>
        <td>
          <form:errors path="link" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">FeedUrl:</td>
        <td>
          <form:input path="feedUrl" size="50"/> (Daylife Feed Url e.g http://timesofindia.indiatimes.com/topic/{0}/latestnews)
        </td>
        <td>
          <form:errors path="feedUrl" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">utm_source:</td>
        <td>
          <form:input path="utmSource" size="50"/> (Default is facebook.com for FB and twitter.com for twitter)
        </td>
        <td>
          <form:errors path="utmSource" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">utm_medium:</td>
        <td>
          <form:input path="utmMedium" size="50"/> (Default is referral)
        </td>
        <td>
          <form:errors path="utmMedium" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">utm_campaign:</td>
        <td>
          <form:input path="utmCampaign" size="50"/>
        </td>
        <td>
          <form:errors path="utmCampaign" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Headline:</td>
        <td>
          <form:input path="headline" size="50"/>
        </td>
        <td>
          <form:errors path="headline" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Push time:</td>
        <td>
          <form:input path="pushTime" size="5"/> (Push start time in hours, i.e. 6.5 = 6:30AM, Used in twitter, to post after specific time for certain accounts)
        </td>
        <td>
          <form:errors path="pushTime" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">HashTags (Delimited by space):</td>
        <td>
          <form:input path="hashtags" size="50"/> (Used in twitter E.g #Olympics #Olympics2012)
        </td>
        <td>
          <form:errors path="hashtags" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Condition HashTags:</td>
        <td>
          <form:input path="hashtagsCondition" size="50"/> (This condition in url will also trigger pushing of the above hashTag)
        </td>
        <td>
          <form:errors path="hashtagsCondition" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Max feed sent/day:</td>
        <td>
          <form:input path="poststhreshold" size="10"/> (This is the threshold limit for each page)
        </td>
        <td>
          <form:errors path="poststhreshold" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Feed pick-time interval (Hours)</td>
        <td>
          <form:input path="feedpicktime" size="10"/> (Default is -12 i.e. Represents news feed, generated after 12AM)
        </td>
        <td>
          <form:errors path="feedpicktime" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Publish all articles:</td>
        <td>
          <form:checkbox path="push_all_article"/>
        </td>
        <td>
          <form:errors path="push_all_article" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Active:</td>
        <td>
          <form:checkbox path="active"/>
        </td>
        <td>
          <form:errors path="active" cssClass="error"/>
        </td>
    </tr>
    <tr>
      <td align="right" class="boldFont">Remarks:</td>
        <td>
          <form:textarea path="remarks" id="remarks" rows="5" cols="30"/>
        </td>
        <td>
          <form:errors path="remarks" cssClass="error"/>
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
