<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.til.service.common.dao.hibernate.entity.User" buffer="50kb"%>
<%@page import="com.til.service.common.menu.MenuHelper"%>
<%@page import="java.util.Collection"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><fmt:message key="application.title"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<META http-equiv="Page-Enter" content="blendTrans(Duration=0.2)">
<META http-equiv="Page-Exit" content="blendTrans(Duration=0.2)"> 

<script type="text/javascript" src="resources/js/jquery.js"></script>
<script type="text/javascript" src="resources/js/stickytooltip.js"></script>
<script type="text/javascript" src="resources/js/thickbox.js"></script>
<script type="text/javascript" src="resources/js/lazyLoad.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="resources/css/project_v2.css"/>
<link rel="stylesheet" type="text/css" media="all" href="resources/css/menu.css"/>
<link rel="stylesheet" type="text/css" media="all" href="resources/css/thickbox.css"/>
<script type="text/javascript" src="resources/js/project.js"></script>
<link rel="shortcut icon" href="resources/images/infavicon.ico">

<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.GrantedAuthority" %>
<%@ page import="org.springframework.security.core.userdetails.UserDetails" %>
<%@ page import="com.til.security.acegi.CustomUser" %>
<%@ page import="com.til.service.common.menu.Menu" %>
<%@ page import="com.til.service.common.dao.hibernate.entity.Website" %>

<script type="text/javascript">
	var oldText="";
	function replaceText(elem, newText) 
	{
		oldText=elem.firstChild.data;
		elem.firstChild.data=newText;
	}
	function restoreText(elem) 
	{
		elem.firstChild.data=oldText;
	}
	$(document).ready(function()
	{
		$('pre.loadme').lazyLoad();
		$('.hoverImage').hover(function() {
	      	var id = $(this).attr('id');
			var html = $('#showHoverImage' + id).html();
			html = $.trim(html.replace(/<!--/, ''));
			html = $.trim(html.replace(/-->/, ''));
			$('#showHoverImage' + id).html(html);
	    });
	});
</script>

<% 
  			String username = "";
  			String fullname = "";
  			String role = "";
  			String email = "";
  			String secondaryEmailAddress = "";
  			int rightlevel = 0;
  			int uid = 0;
  			short rid = 0;
  			User user = null;
  			Website website = null;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
			if (auth != null) {
				Collection<GrantedAuthority> authorities = auth.getAuthorities();
				Object obj = auth.getPrincipal();
				
				if (obj instanceof UserDetails) {
					user = (User)((CustomUser) obj).getUser();
					username = user.getEmailAddress();
					fullname = user.getFullName();
					email = user.getEmailAddress();
					secondaryEmailAddress = user.getSecondaryEmailAddress();
					rightlevel = user.getRole().getSortOrder();
					website = user.getWebsite();
					uid = user.getId();
					rid = user.getRole().getId();
				} else {
					username = obj.toString();
					fullname = obj.toString();
				}
				for (GrantedAuthority grantedAuthority : authorities) {
					if(!"ROLE_ANONYMOUS".equals(grantedAuthority.getAuthority()))
					{
						role = grantedAuthority.getAuthority().toString();
					}
				}
			}
			
%>	
<c:set var="loggedinuser" value="<%= user %>"/>
</head>
<body>

<c:set var="username" value="<%= username %>"/>
<c:set var="email" value="<%= email %>"/>
<c:set var="uid" value="<%= uid %>"/>
<c:set var="rid" value="<%= rid %>"/>
<c:set var="secondaryEmailAddress" value="<%=secondaryEmailAddress%>"/>
<c:set var="role" value="<%= role %>"/>
<c:set var="rightlevel" value="<%= rightlevel %>"/>
<c:set var="fullname" value="<%= fullname %>"/>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td colspan="2" height="5"></td></tr>
  <tr>
  	<td height="10" colspan="3" align="right">
		<table cellpadding="0" cellspacing="0" border="0" width="100%" align="right">
			<tr>
				<td width="65%">&nbsp;</td>
				<td> 
					<%-- <div>&nbsp;<a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Signout</a>&nbsp;&nbsp;</div> --%>
					<div class="absoluteShare">
						<div onclick="shareIcon(this)" class="shareIconDiv share111" id="111">
				        	<span><a href="#"><c:out value="${fullname}"/></a></span>
				        </div>
						<div class="signOutWindow showHiddenIcon" style="display:none" id="showHiddenIcon111">
							<div class="signOutWindowInner">
				                <div class="imageLeftPart">
				                        <img src="resources/images/blankUserImage.jpg" height="97" width="97" />
				                </div>
				                <div class="contentRightPart">
				                        <div class="name"><c:out value="${fullname}"/></div>
				                        <div class="mail"><c:out value="${secondaryEmailAddress}"/></div>
				                        <ul class="links">
				                                <li><a href="<c:url value="/addEditUser?id=${uid}&r=${rid}&w=${website.id}&m=edit"/>">Profile</a></li>
				                                <%-- <li><a href="#">Stream</a></li>
				                                <li><a href="#">Account settings</a></li>--%>
				                                <li><a href="http://www.indiatimes.com/policyterms/1554651.cms">Privacy</a></li>
				                        </ul>
				                </div>
				                <div class="clear"></div>
				            </div>
				            <div class="signOut"><a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Sign out</a></div>
				        </div>
				   </div>
				</td>
			</tr>
		</table>
	</td>
  </tr>
  <tr><td colspan="2" height="5"></td></tr>
  <tr>
    <td width="300" align="left" valign="top" style="padding:10px 0px 0px 20px;"><div class="headerBoldSecond"><a href='<c:url value="/welcome"/>'>Social Audience Manager</a></div></td>
    <td align="right" style="position:relative;display:block" colspan="2" height="77"><img src="resources/images/audienceManager.jpg" alt="" height="100" style="position:absolute; top:0px; right:20px;" /></td>
  </tr>
  
  <% 
  	String handler = (String)request.getAttribute("org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping"); 
  	handler = handler.replace("/","").replace(".htm","");
  	Menu menu = MenuHelper.getMenu("editor",handler,user,website); 
  	
  %>
  <c:set var="handlerName" value='<%= handler %>'/>
  <c:set var="tabMenu" value="<%=menu%>"/>
	<c:if test="${handlerName ne 'welcome'}">
		<c:if test="${not empty tabMenu}">
			<table class="menuTabTable" cellspacing="0" style="font-size:12px">
				<tr>
					<c:forEach var="tab" items="${tabMenu.tabs}" >
						<td>
						    <c:if test="${tab.selected}">
						        <c:set var="selectedTab" value="${tab}" />
						        <td class="menuTabSelected">
						    </c:if>
						    <c:if test="${tab.selected == false}">
						        <td class="menuTabUnselected">
						    </c:if>
						    <div class="menu-tr">
						        <div class="menu-tl">
						            &nbsp;&nbsp;<a href="<c:url value="${tab.action}"/>"><spring:message code="${tab.key}" /></a>&nbsp;&nbsp;
						        </div>
						    </div>
					    </td>
					    <td class="menuTabSeparator"></td>
					</c:forEach>
				</tr>
			</table>
			<table class="menuItemTable" cellspacing="0" style="font-size:12px">
			    <tr>
			        <td class="padleft">
			           <c:forEach var="tabItem" items="${selectedTab.items}" varStatus="stat">
		                	<c:if test="${stat.first == false}">|</c:if>
			                <c:if test="${tabItem.selected}">
			                    <a class="menuItemSelected" href="<c:url value="${tabItem.action}"/>"><spring:message code="${tabItem.key}" /></a>
			                </c:if>
			                <c:if test="${tabItem.selected eq false}">
			                	<a class="menuItemUnselected" href="<c:url value="${tabItem.action}"/>"><spring:message code="${tabItem.key}" /></a>
			                </c:if>
			           </c:forEach>
			        </td>
			    </tr>
			</table>
		</c:if>
	</c:if>		          
</table>
<%-- Include these js/css for dates --%>
<%-- Reference documentation http://www.kelvinluck.com/assets/jquery/datePicker/v2/demo/ --%>
<!-- datePicker required styles -->
<link rel="stylesheet" type="text/css" media="screen" href="resources/css/datePicker.css">
<script type="text/javascript" src="resources/js/date.js"></script>
<script type="text/javascript" src="resources/js/jquery.datePicker.js"></script>

<script type="text/javascript">
    <!--
    Date.format = 'dd-mm-yyyy';
    $(function()
	{
		$('.date-pick').datePicker({startDate:'01-01-2007'});
	});
    // -->
</script>
<%--  Include these js/css for dates --%>