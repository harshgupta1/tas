<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ page import="org.springframework.security.web.authentication.AbstractProcessingFilter" %>
<%@page import="org.springframework.security.core.AuthenticationException"%>
<link rel="stylesheet" type="text/css" media="all" href="resources/css/project_v2.css" />
<title>Social Audience Manager</title>
<style type="text/css">
* {margin: 0;}
html, body {height: 100%;}
.wrapper {min-height: 100%;height: auto !important;height: 100%;margin: 0 auto -4em;background:#FFFFFF none repeat scroll 0 0;border:1px solid #CCCCCC;width:1003px;}
body {background:#eee;}

/*new login form for Audience manager start*/
.headerAudience{background:#fff; border-bottom:1px solid #E5E5E5; }
.headerAudience span.leftHA{float:left; padding:35px 0px 20px 20px;}
.headerAudience span.rightHA{float:right; padding:0px 20px 0px 0px}
.afterHeader {font-family:Arial, Helvetica, sans-serif; padding-top:30px;}
.afterHeader .leftAfterheader{float:left; width:540px; padding:25px}
.afterHeader .leftAfterheader h1{color:#DD4B39; font-size:25px; font-weight:normal; line-height:24px;}
.afterHeader .leftAfterheader p{margin:20px 0px 30px 0px; padding:0px;}
.afterHeader .leftAfterheader ul.mainField{margin:0px; padding:0px}
.afterHeader .leftAfterheader ul.mainField li{list-style:none; padding:0px 0px 20px 0px}
.afterHeader .leftAfterheader ul.mainField li span{float:left}
.afterHeader .leftAfterheader ul.mainField li p{font-size:12px; margin:0px; padding:0px}
.afterHeader .leftAfterheader ul.mainField li p.title{font-size:16px; margin:0px 0px 3px 0px; padding:0px}
.afterHeader .leftAfterheader ul.mainField li .txtLogin{margin:5px 0px 0px 20px}
.afterHeader .leftAfterheader ul.mainField li .facebookHeaderIcon{background:url(resources/images/network_images.png) -1px -14px no-repeat; height:60px; width:62px; display:block;}
.afterHeader .leftAfterheader ul.mainField li .twitterHeaderIcon{background:url(resources/images/network_images.png) -175px -14px no-repeat; height:60px; width:62px; display:block;}
.afterHeader .leftAfterheader ul.mainField li .buzzHeaderIcon{background:url(resources/images/network_images.png) -349px -14px no-repeat; height:60px; width:63px; display:block;}

.afterHeader .rightAfterheader{float:left; padding:100px 0px 50px 30px; width:342px;}
.rightAfterheader h2{font-size: 16px;height: 16px;line-height: 16px; color:#222222;margin: 5px 0px 5px 5px;
font-weight: normal;
font-family: arial,helvetica,sans-serif;}
/*new login form for Audience manager end*/
</style>
</head>
<body>
<div class="wrapper">
	<!--header start-->
    <div class="headerAudience">
    		<span class="leftHA"><a href='<c:url value="/welcome"/>'/><img src="resources/images/indiatimesLogo.gif" /></a></span>
            <span class="rightHA"><img src="resources/images/audienceManager.jpg" height="100" /></span>
            <div class="clear"></div>
    </div>
    <!--header end-->
	<!--after header DIV start-->
    		<div class="afterHeader">
				   <div class="leftAfterheader">
                   		<h1>Social Audience Manager</h1>
                        <p>
                        	Social Audience Manager is a dashboard for social media management, tracking and reporting, It has support for: 
                        </p>
                        <ul class="mainField">
                        		<li>
                                		<span class="facebookHeaderIcon"></span>
                                        <span class="txtLogin">
                                        		<p class="title">Publish/Engage/Monitor</p>
                                                <p>Publish on any number of facebook Pages.</p>
                                        </span>
                                        <div class="clear"></div>
                                </li>
                                <li>
                                		<span class="twitterHeaderIcon"></span>
                                        <span class="txtLogin">
                                        		<p class="title">Publish Tweet/Engage/Monitor</p>
                                                <p>Publish on any number of twitter accounts.</p>
                                        </span>
                                        <div class="clear"></div>
                                </li>
                                <li>
                                		<span class="buzzHeaderIcon"></span>
                                        <span class="txtLogin">
                                        		<p class="title"></p>
                                                <p>Coming soon...</p>
                                        </span>
                                        <div class="clear"></div>
                                </li>
                        </ul>
                   </div>
                   <div class="rightAfterheader">
                   		<!--login form start-->
                        <form action="<c:url value='j_spring_security_check'/>" method="post">
			    <div id="acloginpod">
                          <table border="0" cellpadding="0" cellspacing="0">
                            <tbody>
                            <tr>
                            		<td colspan="2"><h2>Sign in</h2></td>
                            </tr>
                              <tr>
                                <td width="100%"><table border="0" cellpadding="2" cellspacing="1" width="100%">
                                    <tbody>
                                      <tr>
                                      		<td align="left" width="100"><b><font face="Verdana" size="1">&nbsp;Username :</font></b></td>
                                      </tr>
                                      <tr>
                                        <td>
                                          <input class="textinput" type='text' name='j_username' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'>
                                        </td>
                                      </tr>
                                      <tr>
                                      		<td align="left" width="100"><b><font face="Verdana" size="1">&nbsp;Password :</font></b></td>
                                      </tr>
                                      <tr>
                                        <td>
                                          <input class="textinput" type='password' name='j_password'>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td colspan="2" align="left" border="0">
                                            <input name="submit" class="acloginbttn" type="submit" value="">
                                            &nbsp;
                                            <!--<input class="btn accessLoginReset" onmouseover="this.className='btnhov'" onmouseout="this.className='btn'" name="reset" type="reset" value="Reset">-->
                                        </td>
                                      </tr>
                                    </tbody>
                                  </table></td>
                              </tr>
                            </tbody>
                          </table>
			   </div>
                        </form>
                        <!--login form end-->
                        <c:if test="${not empty param.login_error}">
                            <div style="color:red;font-weight:bold;" class="loginTable">
                                    Your login attempt was not successful, try again.
                                    Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
                                   </c:if>
                            </div>
                            <script type="text/javascript">
                                document.forms[0].j_username.focus();
                            </script>
                   </div>
                   <div class="clear"></div>        
            </div>
            <!--after header DIV end-->
		<br />
		
	</div>


<div id="footerWelcome" class="footer">
<hr/>
<div class="foMargin"><a linkindex="396" href="http://www.indiatimes.com/aboutus.cms" pg="becpPos1">About us</a> | <a linkindex="397" href="http://advertise.indiatimes.com/" pg="becpPos2">Advertise with us</a> | <a linkindex="398" href="http://www.indiatimes.com/careers.cms" pg="becpPos3">Careers @ TIL</a> | <a linkindex="399" href="http://www.indiatimes.com/policyterms/3441503.cms" pg="becpPos4">Terms of use</a> | <a linkindex="400" href="http://www.indiatimes.com/policyterms/1554651.cms" pg="becpPos5">Privacy policy</a> | <a linkindex="401" href="http://www.indiatimes.com/feedback.cms" pg="becpPos6">Feedback</a> | <a linkindex="402" href="http://www.indiatimes.com/sitemap.cms" pg="becpPos7">Sitemap</a></div>
</div>
</body>
</html>