<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<script type="text/javascript">
function submitform(page,formname)
{
	document[formname].page.value=page;
	document[formname].formname.value=formname;
	document[formname].submit(); 
	return false;
}
</script>
<div class="space10LR">
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
	    <td>
	       	<H2 style="padding:10px 0px 0px 10px">History:
	       	<c:if test="${articleListBean.navInfo.displayCount > 0}" > 
	       	Displaying ${articleListBean.navInfo.displayCount} Of ${articleListBean.navInfo.rowCount} Article(s)
	       	</c:if> 
	       	</H2>
	   	</td>
	</tr>
	<tr>
	  <td colspan="4">
		  <spring:hasBindErrors name="articleListBean">
			  <c:forEach items="${errors.globalErrors}" var="error">
			 		<div class="error">
			   		${error.defaultMessage}
			   	</div>
			  </c:forEach>
			  <c:forEach items="${errors.fieldErrors}" var="error">
			  	<div class="error">
			    	${error.defaultMessage}
			    </div>
			  </c:forEach>
		  </spring:hasBindErrors>
		</td>
	</tr>
	<tr>
		<form:form id="articleHistory" name="articleHistory" action="" method="post" modelAttribute="articleListBean" onsubmit="this.page.value='1';">
		    <form:hidden path="page"/>
		    <form:hidden path="formname"/>
		    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="paddingRL">
		      <tr>
		      	<td colspan="4" class="errorGen paddBot"><i></i></td>
		       </tr>
			   <tr>
			   		<td width="230">
			   			<div class="spaceTB">
				           <div class="leftPart"><a style="text-decoration:none">Topic Name</a></div>
				           <div class="rightPart"><form:input path="topicName" size="18"/></div>
				           <div class="clear"></div>
				       </div>
				       <div><a style="text-decoration:none">(Use % as wildcard char...)</a></div>
			   		</td>
			        <td rowspan="2" valign="top"><input id="query" class="btn" name="query" value="Search" type="submit"></td>
			        <td>
			        	<div class="tablenav">
					       <div style="float: left;"></div>
					         <div align="right">
					             <c:if test="${!articleListBean.navInfo.firstPage and articleListBean.navInfo.rowCount gt 0}">
					                 <a href="#" onclick="submitform('1','${articleListBean.formname}')" >First</a>&nbsp;
					             </c:if>
					             <c:forEach var="i" items="${articleListBean.navInfo.indexList}">
					                 <c:choose>
					                     <c:when test="${i != articleListBean.navInfo.currentPage}">
					                         <a href="#" onclick="submitform('${i}','${articleListBean.formname}');">${i}</a>&nbsp;
					                     </c:when>
					                     <c:otherwise>
					                         <b>${i}</b>&nbsp;
					                     </c:otherwise>
					                 </c:choose>
					             </c:forEach>
					             <c:if test="${!articleListBean.navInfo.lastPage and articleListBean.navInfo.rowCount gt 0}">
					                 <a href="#" onclick="submitform('${articleListBean.navInfo.pageCount}','${articleListBean.formname}');">Last</a>
					             </c:if>
					           </div>   
					         </div>
	        			 </div>
			        </td>
			   </tr>
		    </table>         
		</form:form> 
	</tr>
</table>
</div>
<c:if test="${fn:length(articleListBean.articleList) > 0}">
	<div class="parentCHILD">
	    <table id="firstDIV" class="wrapWithHeight" cellpadding="0" cellspacing="0">
		    <tbody>
		      <tr>
		        <td class="tpHeading" align="center" valign="top" width="5%"><spring:message code="websiteManagement.code" /></td>
		        <td class="tpHeading" valign="top" width="7%"><spring:message code="websiteManagement.name" /></td>
		        <td class="tpHeading" valign="top" width="9%"><spring:message code="topicManagement.topicname" /></td>
		        <td class="tpHeading" valign="top" width="17%"><spring:message code="articleManagement.link" /></td>
		        <td class="tpHeading" valign="top" width="12%"><spring:message code="articleManagement.createdate" /></td>
		        <td class="tpHeading" valign="top" width="12%"><spring:message code="articleManagement.updatedate" /></td>
		        <td class="tpHeading" valign="top" width="12%"><spring:message code="articleManagement.feeddate" /></td>
		        <td class="tpHeading" valign="top" width="8%"><spring:message code="articleManagement.status" /></td>
		        <td class="tpHeading" valign="top" width="18%"><spring:message code="articleManagement.remarks" /></td>
		     </tr>
	    
		<%-- ========================================================= --%>
		<%-- Loop through WebsiteList --%>
		<%-- ========================================================= --%>
		<c:forEach items="${articleListBean.articleList}" var="list">
			<c:choose>
				<c:when test="${ list[0].scheduled }">
	       			<c:set var="bgcolor"><spring:message code='articleManagement.scheduledRowColor' /></c:set>
	       		</c:when>
				<c:when test="${ not empty list[3].status && list[3].status eq 'PROCESSED' }">
	       			<c:set var="bgcolor"><spring:message code='articleManagement.rowColor' /></c:set>
	       		</c:when>
	       		<c:otherwise>
	       			<c:set var="bgcolor"><spring:message code='articleManagement.errorRowColor' /></c:set>
	       		</c:otherwise>
			</c:choose>
		      <tr bgcolor="${bgcolor}">
		        <td valign="top" align="center">
		        	<span class="${fn:toLowerCase(list[2].socialAppName)}LargeIcon">&nbsp;</span>
		        </td>
	      		<td valign="top">${list[2].name}</td>
		         <%-- <td valign="top">${list[2].siteUrl}</td> --%>
		         <td valign="top">
		         	<c:if test="${list[2].socialAppName eq 'FACEBOOK'}">
		         		<c:if test="${not empty list[1].username}">
		         			<a href="http://www.facebook.com/${list[1].username}?sk=wall" target="_blank">${list[1].entityName}</a>
		         		</c:if>
		         	</c:if>
		         	<c:if test="${list[2].socialAppName eq 'TWITTER'}">
		         		<a href="http://www.twitter.com/${list[1].username}" target="_blank">${list[1].entityName}</a>
		         	</c:if>
		         	
		         </td>
		         <td valign="top">${fn:substring(list[0].message,0,20)}...<br/><a href="${list[0].url}" target="_blank" title="${list[0].message}">${list[0].url}</a></td>
		         <td valign="top">
		         	<fmt:formatDate value="${list[0].createdate}" type="both" timeStyle="long" dateStyle="long" />&nbsp;
		         </td>
		         <td valign="top">
		         	<fmt:formatDate value="${list[0].updatedate}" type="both" timeStyle="long" dateStyle="long" />&nbsp;
		         </td>
		         <td valign="top">
		         	<fmt:formatDate value="${list[0].feedtimestamp}" type="both" timeStyle="long" dateStyle="long" />&nbsp;
		         </td>
		         <td valign="top">${list[3].status}</td>
		         <td valign="top">${list[3].remarks}</td>
		      </tr>
		</c:forEach>
		</tbody>
		</table>				
	</div>
</c:if>
<%@ include file="/WEB-INF/views/footer.jsp" %>