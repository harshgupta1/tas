<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<div id="centercontent_wrap">
    <div id="centercontent">
      <h1>Main Menu</h1>
      <p class="subtitle">Select a website to manage.</p>
      <%-- 
      <c:if test="${fn:length(loggedinuser.websiterole) > 1}">
      <c:forEach var="websiterole" items="${loggedinuser.websiterole}">
      <c:set var="website" value="${websiterole.website}"/>
      <c:set var="role" value="${websiterole.role}"/>
      <c:set var="key" value="${website.key}"/>
      <div class="yourWeblogBox"> <span class="mm_weblog_name"><img src="images/folder.png">&nbsp;${website.sitecode}</span>
        <table class="mm_table" cellpadding="0" cellspacing="0" width="100%">
          <tbody>
            <tr>
              <td valign="top"><table cellpadding="0" cellspacing="0">
                  <tbody>
                    <tr>
                      <td class="mm_subtable_label">Permission</td>
                      <td> ${role.roleName}</td>
                    </tr>
                    <tr>
                      <td class="mm_subtable_label">Description</td>
                      <td>${website.description}</td>
                    </tr>
                  </tbody>
              </table></td>
              <td class="mm_table_actions" align="left" width="20%">
                <img src="images/table_multiple.png" align="absmiddle"> <br>
                <img src="images/page_white_edit.png" align="absmiddle"> <br>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      </c:forEach>
      </c:if>
      --%>
    </div>
  </div>

<%-- <c:if test="${fn:length(loggedinuser.websiterole) eq 1}">
	<c:redirect url="listWebsite"></c:redirect>
</c:if>
--%>
<c:redirect url="listWebsite"></c:redirect>
<%@ include file="/WEB-INF/views/footer.jsp" %>
