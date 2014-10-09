<%@ include file="/WEB-INF/views/includes.jsp" %>
<script type="text/javascript">
function validateform(form)
{
	var elem = form.elements;
	var elementschecked = false;
    for(var i = 0; i < elem.length; i++)
    {
    	// elem[i].type // elem[i].name elem[i].value
        if(elem[i].type == 'checkbox')
       	{
        	if(elem[i].checked)
       		{
        		elementschecked = true;
        		form.configure.disabled=true;
        		break;
       		}
       	}
    } 
    if(!elementschecked)
   	{
   		alert("Please select <c:out value="${appName}"/> pages to configure.");
   		return false;
   	}
    else
   	{
		return true;   	
   	}
}
</script>
<style type="text/css">
/*css for big popup*/
.facebookPopBody{padding:10px; font-family:"Arial",Ariel;}
h2.facebookPopHead{font-size:20px;border-bottom:1px dotted #666666;color:green; margin:0px; padding:0px}
.facebookPopBody .facebookPopHeadSmall{font-size:16px; margin:10px 0px; padding:0px}
.facebooktable{padding:10px 5px 10px 5px; border:1px solid #CCCCCC;font-family: "Arial",Ariel;}
.facebooktable td.radio{padding-left:5px; padding-right:5px;}
.facebooktable a{color:#35556B; font-size:12px; text-decoration:none}
.facebooktable a:hover{color:#35556B; font-size:12px; text-decoration:underline}
.clear{clear:both}
.floatLeft{float:left}
.floatRight{float:right;margin:10px 10px 10px 0px; padding:0px}
.floatRight a{color:#35556B; font-size:12px; text-decoration:none}
.floatRight a:hover{color:#35556B; font-size:12px; text-decoration:underline}
.messages{background-color: #CFC;border: 1px solid green; padding:5px; font-size:12px; margin-bottom:10px;}
.messages ul{margin:0px 0px 0px 25px;padding:0px}
.facebookIcon{background: url("resources/images/audienceSprite.png") no-repeat -16px -1px; height:16px; width:16px; display:inline-block; margin:-3px 0px 0px 0px}
.facebookFlag{background: url("resources/images/audienceSprite.png") no-repeat -19px -18px; height:15px; width:13px; display:inline-block; margin:0px 0px 0px 0px}
.linkedinSmallIcon{background:url("resources/images/network_images.png") -262px -152px no-repeat; height:16px; width:16px; display:block; margin:-3px 0px 0px 0px}
.googleplusSmallIcon{background:url("resources/images/network_images.png") -349px -153px no-repeat; height:14px; width:16px; display:block; margin:-3px 0px 0px 0px}
.twitterSmallIcon{background:url("resources/images/network_images.png") -175px -153px no-repeat; height:16px; width:16px; display:block; margin:-3px 0px 0px 0px}
</style>

<%
String msg = request.getParameter("msg");
if(msg != null && !"".equals(msg))
{
	if("success".equalsIgnoreCase(msg))
	{
		msg = "Pages configured successfully.";
	}
	pageContext.setAttribute("msg",msg);
}
%>
<div class="facebookPopBody"><h2 class="facebookPopHead">Please select <c:out value="${appName}"/> pages to configure</h2>
<form:form method="post" modelAttribute="pageListBean" name="page" onsubmit="return validateform(this);">
<c:if test="${not empty msg}">
	<div class="messages" id="messages">
		<ul><li><span class="actionMessage">${msg}</span></li></ul>
	</div>
</c:if>        			 
	<c:set var="profile" value="${pageListBean.accounts.profileData }"></c:set>
	<c:set var="error" value="${pageListBean.accounts.error}"></c:set>
	<c:if test="${ not empty profile }">
		<div>
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td width="20">
						<c:if test="${fn:containsIgnoreCase('facebook',appName)}">
			        		<span class="facebookIcon"></span>	
			        	</c:if>
						<c:if test="${fn:containsIgnoreCase('twitter',appName)}">
			        		<span class="twitterSmallIcon"></span>	
			        	</c:if>
			        	<c:if test="${fn:containsIgnoreCase('LinkedIN',appName)}">
			        		<span class="linkedinSmallIcon"></span>	
			        	</c:if>
			        	<c:if test="${fn:containsIgnoreCase('GooglePlus',appName)}">
			        		<span class="googleplusSmallIcon"></span>	
			        	</c:if>
					</td>
					
					<td><h3 class="facebookPopHeadSmall"><c:out value="${appName}"/> Profile </h3></td>
				</tr>
			</table>
		</div>
		<div class="parentCHILD">
		    <table id="firstDIV" class="facebooktable wrapWithHeight" cellpadding="0" cellspacing="0" width="100%">
			    <tbody>
				<tr>
		        <td valign="top" class="radio" width="20">
		        	<c:if test="${profile.id ne null}">
			        	<c:if test="${profile.exists}">
			        		<form:checkbox path="pageid" value="${profile.id}" disabled="true"/>
			        	</c:if>
			        	<c:if test="${profile.exists eq false}">
			        		<form:checkbox path="pageid" value="${profile.id}"/>
			        	</c:if>
		        	</c:if>
		        </td>
		        <c:set var="profileUrl" value="" />
		        <c:if test="${fn:containsIgnoreCase('GooglePlus',appName)}">
		        	<c:set var="profileUrl" value="https://plus.google.com/u/0/${profile.id}" />
		        </c:if>
		        <c:if test="${fn:containsIgnoreCase('LinkedIn',appName)}">
		        	<c:set var="profileUrl" value="http://www.linkedin.com/${profile.id}" />
		        </c:if>		
		        <c:if test="${fn:containsIgnoreCase('facebook',appName)}">
		        	<c:set var="profileUrl" value="http://www.facebook.com/${profile.id}" />
		        </c:if>
		        <td valign="top">
		        	<c:if test="${profile.id ne null}">
		        		<a href="${profileUrl}" target="_blank"><c:out value="${profile.name}"/></a>
		        	</c:if>
		        	<c:if test="${profile.id eq null}">
		   				<c:out value="${error.message}"/>
		        	</c:if>
		        </td>
		      </tr>
		      </tbody>
	      </table>
      </div>
	</c:if>	
<div>
<c:if test="${fn:length(pageListBean.accounts.data) gt 0}">
<div class="floatLeft">
	<table cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="20"><span class="facebookFlag"></span></td>
			<td><h3 class="facebookPopHeadSmall"><c:out value="${appName}"/> Pages &nbsp;(${fn:length(pageListBean.accounts.data)})</h3></td>
		</tr>
	</table>
</div>
<div class=clear></div>
</div>	

<div class="parentCHILD">
    <table id="firstDIV" class="facebooktable wrapWithHeight" cellpadding="0" cellspacing="0" width="100%">
	    <tbody>
			<%-- ========================================================= --%>
			<%-- Loop through SelectedPages --%>
			<%-- ========================================================= --%>
			
				<c:forEach items="${pageListBean.accounts.data}" var="pagedetail">
			      <tr>
			        <td valign="top" class="radio" width="20">
			        	<c:if test="${pagedetail.exists}">
			        		<c:if test="${pagedetail.id ne null}">
			        		<form:checkbox path="pageid" value="${pagedetail.id}" disabled="true"/>
			        		</c:if>
			        	</c:if>
			        	<c:if test="${pagedetail.exists eq false}">
			        	
			        		<c:if test="${pagedetail.id ne null}">
			        		<form:checkbox path="pageid" value="${pagedetail.id}"/>
			        		</c:if>
			        	</c:if>
			       	
			       
			        </td>
			         <td valign="top">
		        	<c:if test="${pagedetail.id eq null}">
		        		<c:out value="${error.message}"/>
		        	</c:if>
		        	
		        </td>
			       <td valign="top">
			       
			        	<c:set var="link" value="${pagedetail.link}"></c:set>	
			        	  <c:if test="${fn:containsIgnoreCase('facebook',appName)}">
			        		<c:choose>
				        		<c:when test="${ not empty pagedetail.username }">
				        			<c:set var="link" value="http://www.facebook.com/${pagedetail.username}"></c:set>
				        		</c:when>
				        	<c:otherwise>
				        			<c:set var="link" value="http://www.facebook.com/pages/${pagedetail.name}/${pagedetail.id}"></c:set>
				        	</c:otherwise>
			        		</c:choose>
			        	</c:if>
			        	<a href="${link}" target="_blank">${pagedetail.name}</a>
			        </td>
			      </tr>
				</c:forEach>
				
		</tbody>
	</table>
	
	<div class="floatRight">
	     <c:if test="${not empty pageListBean.accounts.paging.previous}">
	     	 <c:set var="limit" value=""/>
	     	 <c:set var="offset" value=""/>
	     	 <c:forEach var="str" items="${fn:split(pageListBean.accounts.paging.previous, '?&')}">
	               <c:if test="${fn:startsWith(str,'limit')}"><c:set var="limit" value="${str}"/></c:if> 
	               <c:if test="${fn:startsWith(str,'offset')}"><c:set var="offset" value="${str}"/></c:if>
	            </c:forEach>
	         <a href='<c:url value="/listPages?id=2&${limit}&${offset}"/>'>Previous</a>&nbsp;
	     </c:if>
	     <c:if test="${not empty pageListBean.accounts.paging.next}">
	     	 <c:set var="limit" value=""/>
	     	 <c:set var="offset" value=""/>
	     	 <c:forEach var="str" items="${fn:split(pageListBean.accounts.paging.next, '?&')}">
	               <c:if test="${fn:startsWith(str,'limit')}"><c:set var="limit" value="${str}"/></c:if> 
	               <c:if test="${fn:startsWith(str,'offset')}"><c:set var="offset" value="${str}"/></c:if>
	            </c:forEach>
	         <c:if test="${ (not empty limit && fn:length(pageListBean.accounts.data) >= fn:split(limit,'=')[1] ) }">
	         	<a href='<c:url value="/listPages?id=2&${limit}&${offset}"/>'>Next</a>
	         </c:if>
	     </c:if>
       </div>
</div>
</c:if>
    <div class="clear"></div>
	<div align="center">
		<c:if test="${error.message eq null}">
			<input type="submit" id="configure" name="configure" value="Configure"/>
		</c:if>
    </div>
	<div></div>		
</form:form>
</div>