<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
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
	function togglecheck(id) {
		$('#'+id).slideToggle();
	}
	function validate(formNamePrefix, id) {
		var frmName = formNamePrefix+id;

		if(!document[frmName].url.value.match(/^http/)) {
			$('#errMsg'+id).html("Please enter a valid url to schedule a message.");
			$('#errMsg'+id).show();
			//document[frmName].errMsg.display="block";
			return false;
		}
		if(document[frmName].autoSchedule.checked) {
			var now = new Date();
			var dt = new Date(document[frmName].scheduleDate.value);
			if(dt == "Invalid Date"){
				$('#errMsg'+id).html("Please enter a valid date to schedule a message.");
				$('#errMsg'+id).show();
				return false;
			}
			var hour = document[frmName].scheduleHour.value;
			var minute = document[frmName].scheduleMinute.value;
			if(hour < 0 || hour > 12 || minute < 0 || minute > 59) {
				$('#errMsg'+id).html("Please enter a valid time to schedule a message.");
				$('#errMsg'+id).show();
				return false;
			}
			
			if(document[frmName].afterNoon.value=='true') {
				hour+=(hour<12)?12:0;
			} else {
				hour-=(hour<12)?0:12;
			}
			dt.setHours(hour);
			dt.setMinutes(minute);
			if(dt<now) {
				$('#errMsg'+id).html("Please enter a valid future date/time to schedule a message.");
				$('#errMsg'+id).show();
				return false;
			}
			
			if(document[frmName].emailMe.checked && document[frmName].email.value.trim()=="") {
				$('#errMsg'+id).html("You must enter an email to get notification.");
				$('#errMsg'+id).show();
				return false;
			}
		}
		$('#errMsg'+id).hide();
		document[frmName].submit();
	}
</script>
<div class="space10LR">
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
	    <td>
	       	<H2 style="padding:10px 0px 0px 10px">Displaying ${topicListBean.navInfo.displayCount} Of ${topicListBean.navInfo.rowCount} Topic(s) </H2>
	   	</td>
	</tr>
	<tr>
	  <td colspan="4">
		  <spring:hasBindErrors name="topicListBean">
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
		  <c:if test="${not empty msg}">
			<div class="messages" id="messages">
				<ul><li><span class="actionMessage">${msg}</span></li></ul>
			</div>
		 </c:if>
		</td>
	</tr>
	<tr>
		<form:form id="listTopic" name="listTopic" action="listTopic" method="post" modelAttribute="topicListBean" onsubmit="this.page.value='1';">
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
					             <c:if test="${!topicListBean.navInfo.firstPage and topicListBean.navInfo.rowCount gt 0}">
					                 <a href="#" onclick="submitform('1','${topicListBean.formname}')" >First</a>&nbsp;
					             </c:if>
					             <c:forEach var="i" items="${topicListBean.navInfo.indexList}">
					                 <c:choose>
					                     <c:when test="${i != topicListBean.navInfo.currentPage}">
					                         <a href="#" onclick="submitform('${i}','${topicListBean.formname}');">${i}</a>&nbsp;
					                     </c:when>
					                     <c:otherwise>
					                         <b>${i}</b>&nbsp;
					                     </c:otherwise>
					                 </c:choose>
					             </c:forEach>
					             <c:if test="${!topicListBean.navInfo.lastPage and topicListBean.navInfo.rowCount gt 0}">
					                 <a href="#" onclick="submitform('${topicListBean.navInfo.pageCount}','${topicListBean.formname}');">Last</a>
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
	      	<td class="tpHeading" valign="top" width="5%">&nbsp;</td>
	        <td class="tpHeading" valign="top" width="7%"><spring:message code="websiteManagement.code" /></td>
	        <td class="tpHeading" valign="top" width="10%"><spring:message code="websiteManagement.name" /></td>
	        <td class="tpHeading" valign="top" width="16%"><spring:message code="topicManagement.topicname" /></td>
	        <td class="tpHeading" valign="top" width="12%"><spring:message code="topicManagement.pageid" /></td>
	        <td class="tpHeading" valign="top" width="5%"><spring:message code="topicManagement.likes" /></td>
	        <td class="tpHeading" valign="top" width="5%"><spring:message code="topicManagement.friends" /></td>
	        <td class="tpHeading" valign="top" width="5%"><spring:message code="topicManagement.followers" /></td>
	        <%-- <td class="tpHeading" valign="top" width="6%"><spring:message code="topicManagement.shares" /></td>--%>
	        <td class="tpHeading" valign="top" width="9%"><spring:message code="topicManagement.maxPosts" /></td>
	        <td class="tpHeading" valign="top" width="4%"><spring:message code="topicManagement.active" /></td>
	        <td class="tpHeading" valign="top" width="12%"><spring:message code="articleManagement.createdate" /></td>
	        <td class="tpHeading" valign="top" width="10%"><spring:message code="websiteManagement.actions" /></td>
	     </tr>
    
	<%-- ========================================================= --%>
	<%-- Loop through TopicPage List --%>
	<%-- ========================================================= --%>

	<c:forEach items="${topicListBean.topicPageList}" var="list" varStatus="status">
	      <tr>
	      	 <td valign="top">
	      		<c:if test="${not empty list[0].picture}"><pre class="loadme" ><!--<img src="${list[0].picture}" width="35" height="35"/>--></pre></c:if>
			    <c:if test="${empty list[0].picture}"><img height="35" width="35" src="resources/images/zero.gif" /></c:if>
			 </td>
	         <td valign="top" align="center">
	        	<span class="${fn:toLowerCase(list[1].socialAppName)}LargeIcon">&nbsp;</span>
	         </td>
      		 <td valign="top">${list[1].name}&nbsp;</td>
	         <td valign="top">${list[0].entityName}&nbsp;</td>
	         <td valign="top">
	         	<c:if test="${list[1].socialAppName eq 'FACEBOOK'}">
	         		<c:if test="${not empty list[0].username}">
	         			<a href="http://www.facebook.com/${list[0].username}?sk=wall" target="_blank">${list[0].pageId}</a>
	         		</c:if>
	         		<c:if test="${empty list[0].username}">
         				<%-- TODO: fn:replace not working here --%>
	         			<a href='http://www.facebook.com/pages/${tilutils:cleanArticleTitle(list[0].pageName)}/${list[0].pageId}?sk=wall' target="_blank">${list[0].pageId}</a>
	         		</c:if>
	         	</c:if>
	         	<c:if test="${list[1].socialAppName eq 'TWITTER'}">
	         		<a href="http://www.twitter.com/${list[0].username}" target="_blank">${list[0].pageId}</a>
	         	</c:if>
	         	<c:if test="${list[1].socialAppName eq 'GOOGLEPLUS'}">
	         		<a href="https://plus.google.com/u/0/${list[0].pageId}" target="_blank">${list[0].pageId}</a>
	         	</c:if>
	         		<c:if test="${list[1].socialAppName eq 'LINKEDIN'}">
	         		<a href="http://www.linkedin.com/${list[0].username}" target="_blank">${list[0].pageId}</a>
	         	</c:if>
	         	
	         </td>
	         <td valign="top">${list[0].likes}&nbsp;</td>
	         <td valign="top">${list[0].friends}&nbsp;</td>
	         <td valign="top">${list[0].followers}&nbsp;</td>
	         <%-- <td valign="top">${list[0].shares}&nbsp;</td>--%>
	         <td valign="top">${list[0].poststhreshold}&nbsp;</td>
	         <td valign="top">${list[0].active}&nbsp;</td>
	         <td valign="top"><fmt:formatDate value="${list[0].createdate}" type="both" timeStyle="long" dateStyle="long" />&nbsp;</td>
	         <td valign="top">
	         	<authz:authorize ifAllGranted="ROLE_ADMIN">
		         	<a  class="linkEdit" href='<c:url value="/addEditTopic?id=${list[0].id}&m=edit"/>'> 
		         		<spring:message code="websiteManagement.actions.edit" />
		         	</a>&nbsp;
		         	<a  class="linkDelete" href='<c:url value="/deleteTopic?id=${list[0].id}&m=delete"/>'> 
		         		<spring:message code="websiteManagement.actions.delete" />
		         	</a>&nbsp;
		         	<a class="linkEdit hoverImage" href="#" id="detail${status.index}" data-tooltip="stickytooltip${status.index}">Details</a>
		         	<a class="linkEdit hoverImage" href="#" id="msgShare${status.index}" data-tooltip="messageShare${status.index}">Schedule Message</a>
		         </authz:authorize>
		         <authz:authorize ifAllGranted="ROLE_SUPERADMIN">
		         	<a  class="linkEdit" href='<c:url value="/addEditTopic?id=${list[0].id}&m=edit"/>'> 
		         		<spring:message code="websiteManagement.actions.edit" />
		         	</a>&nbsp;
		         	<a  class="linkDelete" href='<c:url value="/deleteTopic?id=${list[0].id}&m=delete"/>'> 
		         		<spring:message code="websiteManagement.actions.delete" />
		         	</a>&nbsp;
		         	<a class="linkEdit hoverImage" href="#" id="detail${status.index}" data-tooltip="stickytooltip${status.index}">Details</a>
		         	<a class="linkEdit hoverImage" href="#" id="msgShare${status.index}" data-tooltip="messageShare${status.index}">Schedule Message</a>
		         	<a class="linkEdit" href="<c:url value="/sendArticle?id=${list[0].id}"/>">Test Push</a>
		         </authz:authorize>
		         <a class="linkEdit" href="<c:url value="/analytics?pageid=${list[0].id}&pagename=${list[0].entityName}"/>">Likes Trend</a>
		         &nbsp;
	         </td>
	      </tr>
	</c:forEach>
		<c:set var="param1" value="{0}"/>
		<div id="mystickytooltip" class="stickytooltip">
			<div style="padding:5px">	
				<c:forEach items="${topicListBean.topicPageList}" var="list" varStatus="status">
				 	<div  id="stickytooltip${status.index}" class="atip" style="width:600px">
				 	  <div class="topicName">${list[0].entityName}</div>
				 	  <div>
				      <div class="leftSide">
				      		<c:if test="${not empty list[0].picture}">
				      			<div id="showHoverImagedetail${status.index}">
				      				<!-- <img src="${list[0].picture}" width="40" height="40"/>  -->
				      			</div>
				      		</c:if>
				      		<c:if test="${empty list[0].picture}"><img height="40" width="40" src="resources/images/zero.gif" /></c:if>
				      </div>
				      <div class="rightSide">
				      		<div>${list[0].pageName}</div>
				      		<div class="descriptionTooltip">${list[0].description}</div>
				      		<div>
				      			<div class="leSide"><b>Topic URL:</b></div>
				      			<div class="reSide"><a href="${list[0].url}" target="_blank">${list[0].url}</a></div>
				      			<div class="clear"></div>
				      		</div>
				      		<div>
				      			<div class="leSide"><b>Feed URL:</b></div>
				      			<div class="reSide"><a href='${fn:replace(list[0].feedUrl,param1,list[0].entityName)}' target="_blank">${fn:replace(list[0].feedUrl,"{0}",list[0].entityName)}</a></div>
				      			<div class="clear"></div>
				      		</div>
				      		<div>
				      			<div class="leSide"><b>Likes:</b></div>
				      			<div class="reSide">${list[0].likes}</div>
				      			<div class="clear"></div>
				      		</div>
				      		<div>
				      			<div class="leSide"><b>Shares:</b></div>
				      			<div class="reSide">${list[0].shares}</div>
				      			<div class="clear"></div>
				      		</div>
				      		<div>
				      			<div class="leSide"><b>Error:</b></div>
				      			<div class="reSide">${list[0].error}</div>
				      			<div class="clear"></div>
				      		</div>
				      </div>
				      <div class="clear"></div>
				      </div>
				    </div>
				    <div class="clear"></div>
				    
   				<div id="messageShare${status.index}" class="messageShare atip" style="width:600px">
	   				<div class="topicName">Schedule a message for ${list[0].entityName}</div>
					<form:form id="scheduleMessage${status.index}" name="scheduleMessage${status.index}" action="scheduleMessage" method="post" modelAttribute="topicListBean">
						<form:hidden path="page"/>
						<form:hidden path="topicName"/>
			    		<form:hidden path="formname"/>
						<input type="hidden" name="topicPage.id" value="${list[0].id }" ></input>
						
						<div class="textAreaDiv">
							<textarea rows="5" name="message" maxlength="100"></textarea>
						</div>
					    <div class="urlText">
					    	<input type="text" value="Add a link" name="url" onblur="validate('scheduleMessage${status.index}');if(value=='') value = 'Add a link'" onfocus="if(value=='Add a link') value = ''"/>
					    </div>
					    <div class="autoCheck">
					    	<label for="checkForAuto">Auto Schedule</label>
							<input type="checkbox" name="autoSchedule" value="true" id="checkForAuto${status.index}" onclick="togglecheck('hiddenCheckContent${status.index}')" />
					        <div class="clear"></div>
					    </div>
					    <!-- hidden content start-->
					    <div id="hiddenCheckContent${status.index}" class="hiddenCheckContent" style="display:none;">
					    	<div class="date">
					        		<span class="txt">Date</span>
					        		<%
					                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					                %>
					                <input type="date" name="scheduleDate" value="<%=sdf.format(new Date())%>" onblur="if(value=='') value = 'yyyy-MM-dd'" onfocus="if(value=='yyyy-MM-dd') value = ''" min="<%=sdf.format(new Date())%>" /> 
					                <div class="clear"></div>
					        </div>
					        <div class="time">
					        		<span class="txt">Time</span>
					                <input type="number" name="scheduleHour" value="0" maxlength="2" min="0" max="12" />
					                <input type="number" name="scheduleMinute" value="0" maxlength="2" min="0" max="59" />
					                <select name="afterNoon">
					                  <option value="false">AM</option>
					                  <option value="true">PM</option>
					                </select>
					                <div class="clear"></div>
					        </div>
					        <div class="emailMe">
				        		<input type="checkbox" name="emailMe" value="true" id="checkForEmail" onclick="togglecheck('emailMe${status.index}')" />
				        		<label for="checkForEmail">Email me when message is sent</label>
					            <div class="clear"></div>
					       	</div>
					        <div class="emailMe" id="emailMe${status.index}" style="display:none">
				        		
				        		<input type="text" name="email" value="" id="email" size="50"/>
				        		<label for="email">(comma seperated emails)</label>
					            <div class="clear"></div>
					       	</div>
					    </div>
					    <!-- hidden content end-->
					    <div class="submitSchedule">
					    	<div id="errMsg${status.index }" class="error" style="display: none"></div>
					    	<input type="button" class="submitSchButton" value="Schedule" onclick="return validate('scheduleMessage','${status.index}')" />
					    </div>
					</form:form>
				</div>
				</c:forEach>
			</div>
			<div class="stickystatus"></div>
		</div> 	
	</tbody>
	</table>				
</div>
<div class="creatNewBu">
 
<authz:authorize ifAllGranted="ROLE_ADMIN">
	<a class="linkEdit" href='<c:url value="/addEditTopic"/>'> <spring:message code="topicManagement.createNewTopic" /></a>
</authz:authorize>
<authz:authorize ifAllGranted="ROLE_SUPERADMIN">
	<a class="linkEdit" href='<c:url value="/addEditTopic"/>'> <spring:message code="topicManagement.createNewTopic" /></a>
</authz:authorize>

</div>
<%@ include file="/WEB-INF/views/footer.jsp" %>