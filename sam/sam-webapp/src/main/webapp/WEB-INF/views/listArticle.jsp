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
	       	<H2 style="padding:10px 0px 0px 10px">Displaying ${articleListBean.navInfo.displayCount} Of ${articleListBean.navInfo.rowCount} Article(s) </H2>
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
		<form:form id="listArticle" name="listArticle" action="" method="post" modelAttribute="articleListBean" onsubmit="this.page.value='1';">
			<input type="hidden" name="status" value="PROCESSED" />
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
			   		<%-- 
			   		<td width="250" valign="top">
			   			<div class="spaceTB">
				           <div class="leftPart"><a style="text-decoration:none">Sort By</a></div>
				           <div class="rightPart">
					           	<form:select path="sortBy" id="sortBy" onchange="if(this.selectedIndex > 0){docuement.getElementById('topicName').disabled=true;}else{docuement.getElementById('topicName').disabled=false;}">
					           		<form:option value="">--Select--</form:option>
				        			<form:option value="entityName">TopicName</form:option>
				        			<form:option value="likes">Likes</form:option>
				        			<form:option value="shares">Shares</form:option>
				        			<form:option value="active">Active</form:option>
				        			<form:option value="createdate">CreateDate</form:option>
				        			<form:option value="updatedate">UpdateDate</form:option>
								</form:select>
								<form:select path="order" id="order">
				        			<form:option value="desc">NEWEST</form:option>
				        			<form:option value="asc">OLDEST</form:option>
								</form:select>
							</div>
				       </div>
			   		</td>
			   		--%>
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
<div class="parentCHILD">
    <table id="firstDIV" class="wrapWithHeight" cellpadding="0" cellspacing="0">
	    <tbody>
	      <tr>
	        <td class="tpHeading" align="center" valign="top" width="5%"><spring:message code="websiteManagement.code" /></td>
	        <td class="tpHeading" valign="top" width="7%"><spring:message code="websiteManagement.name" /></td>
	        <%-- <td class="tpHeading" valign="top" width="20%"><spring:message code="websiteManagement.siteurl" /></td>--%>
	        <td class="tpHeading" valign="top" width="9%"><spring:message code="topicManagement.topicname" /></td>
	        <%-- <td class="tpHeading" valign="top" width="13%"><spring:message code="articleManagement.message" /></td>--%>
	        <td class="tpHeading" valign="top" width="17%"><spring:message code="articleManagement.link" /></td>
	        <%-- <td class="tpHeading" valign="top" width="5%"><spring:message code="articleManagement.processed" /></td>--%>
	        <td class="tpHeading" valign="top" width="6%"><spring:message code="articleManagement.viewcount" /></td>
	        <td colspan="2" width="9%" class="topRow tpHeading">
	        	<table cellpadding="0" cellspacing="0" width="100%">
		        	<tr>
				        <td class="tpHeading bottomRow" valign="top" width="9%" colspan="2"><spring:message code="topicManagement.impressions" /></td>
				    </tr>
				    <tr>
				        <td class="tpHeading rightRow font11px" valign="top" width="51%"><spring:message code="topicManagement.unique" /></td>
				        <td class="tpHeading font11px" valign="top" width="49%"><spring:message code="topicManagement.total" /></td>
				    </tr>
		        </table>
	        </td>
	        <td colspan="2" width="9%" class="topRow tpHeading">
	        	<table cellpadding="0" cellspacing="0" width="100%">
		        	<tr>
				        <td class="tpHeading bottomRow" valign="top" width="9%" colspan="2" align="center"><spring:message code="topicManagement.clicks" /></td>
				    </tr>
				    <tr>
				        <td class="tpHeading rightRow font11px" valign="top" width="51%"><spring:message code="topicManagement.unique" /></td>
				        <td class="tpHeading font11px" valign="top" width="49%"><spring:message code="topicManagement.total" /></td>
				    </tr>
		        </table>
	        </td>
	        <td class="tpHeading" valign="top" width="13%"><spring:message code="articleManagement.createdate" /></td>
	        <td class="tpHeading" valign="top" width="12%"><spring:message code="articleManagement.updatedate" /></td>
	        <td class="tpHeading" valign="top" width="13%"><spring:message code="articleManagement.feeddate" /></td>
	        <!-- 
	        <td class="tpHeading" valign="top" width="9%"><spring:message code="websiteManagement.actions" /></td>
	         -->
	     </tr>
    
	<%-- ========================================================= --%>
	<%-- Loop through WebsiteList --%>
	<%-- ========================================================= --%>
	<c:forEach items="${articleListBean.articleList}" var="list">
			<c:choose>
				<c:when test="${ list[0].scheduled }">
	       			<c:set var="bgcolor"><spring:message code='articleManagement.scheduledRowColor' /></c:set>
	       		</c:when>
	       		<c:otherwise>
	       			<c:set var="bgcolor"><spring:message code='articleManagement.rowColor' /></c:set>
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
	         <%-- <td valign="top">${list[0].message}</td>--%>
	         <td valign="top">${fn:substring(list[0].message,0,20)}...<br/><a href="${list[0].url}" target="_blank" title="${list[0].message}">${list[0].url}</a></td>
	         <%-- <td valign="top">${list[0].processed}</td>--%>
	         <td valign="top">${list[0].viewcount}</td>
	         <td valign="top">${list[3].impressionsUnique}</td>
	         <td valign="top">${list[3].impressionsTotal}</td>
	         <td valign="top">${list[3].clicksUnique}</td>
	         <td valign="top">${list[3].clicksTotal}</td>
	         <td valign="top">
	         	<fmt:formatDate value="${list[0].createdate}" type="both" timeStyle="long" dateStyle="long" />&nbsp;
	         </td>
	         <td valign="top">
	         	<fmt:formatDate value="${list[0].updatedate}" type="both" timeStyle="long" dateStyle="long" />&nbsp;
	         </td>
	         <td valign="top">
	         	<fmt:formatDate value="${list[0].feedtimestamp}" type="both" timeStyle="long" dateStyle="long" />&nbsp;
	         </td>
	         <!-- 
	         <td valign="top">
	         	<authz:authorize ifAllGranted="ROLE_ADMIN">
		         	<a  class="linkEdit" href='<c:url value="/addEditArticle?id=${list[0].id}&m=edit"/>'> 
		         		<spring:message code="websiteManagement.actions.edit" />
		         	</a>
		         	<a  class="linkDelete" href='<c:url value="/deleteArticle?aid=${list[0].id}&app=${list[2].socialAppName}&pid=${list[3].postId}&m=delete&wid=${list[2].id}&tpaid=${list[3].id}&tid=${list[1].id}"/>'> 
		         		<spring:message code="websiteManagement.actions.delete" />
		         	</a>
		         </authz:authorize>
		         <authz:authorize ifAllGranted="ROLE_SUPERADMIN">
		         	<a  class="linkEdit" href='<c:url value="/addEditArticle?id=${list[0].id}&m=edit"/>'> 
		         		<spring:message code="websiteManagement.actions.edit" />
		         	</a>
		         	<a  class="linkDelete" href='<c:url value="/deleteArticle?aid=${list[0].id}&app=${list[2].socialAppName}&pid=${list[3].postId}&m=delete&wid=${list[2].id}&tpaid=${list[3].id}&tid=${list[1].id}"/>'> 
		         		<spring:message code="websiteManagement.actions.delete" />
		         	</a>
		         </authz:authorize> 
	         </td>
	          -->
	      </tr>
	</c:forEach>
	</tbody>
	</table>				
</div>
<div class="creatNewBu">
<authz:authorize ifAllGranted="ROLE_ADMIN">
	<a class="linkEdit" href='<c:url value="/addEditArticle"/>'> <spring:message code="articleManagement.createNewArticle" /></a>
</authz:authorize>
<authz:authorize ifAllGranted="ROLE_SUPERADMIN">
	<a class="linkEdit" href='<c:url value="/addEditArticle"/>'> <spring:message code="articleManagement.createNewArticle" /></a>
</authz:authorize>
</div>
<%@ include file="/WEB-INF/views/footer.jsp" %>