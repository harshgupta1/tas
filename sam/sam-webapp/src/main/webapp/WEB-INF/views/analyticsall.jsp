<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ include file="/WEB-INF/views/header.jsp" %>


<script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);


      function drawChart() {
    	  <c:if test="${facebook}">
    	  	drawChartFaceBook();
    	  </c:if>
    	  <c:if test="${twitter}">
    	  	drawChartTwitter();
    	  </c:if>
      }

      function drawChartFaceBook() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Year');
        data.addColumn('number', 'Likes');
        data.addColumn('number', 'UnLikes');

        data.addRows(${faceBookLikes.size()});
       
        <c:forEach items="${faceBookLikes}" var="list" varStatus="status">
	        data.setValue(${status.index}, 0, '<fmt:formatDate pattern="dd.MMM" value="${list[1]}" />');
	        data.setValue(${status.index}, 1, ${list[0]});
    	</c:forEach>
    	<c:forEach items="${faceBookDislikes}" var="list" varStatus="status">
        	data.setValue(${status.index}, 2, ${list[0]});
		</c:forEach>
	
        var chart = new google.visualization.LineChart(document.getElementById('chart_div_facebook'));
        chart.draw(data, {width: 1024, height: 300, title: 'Facebook Topic Page Likes/Unlikes Trend'});
      }

      function drawChartTwitter() {
          var data = new google.visualization.DataTable();
          data.addColumn('string', 'Year');
          data.addColumn('number', 'Likes');
          data.addColumn('number', 'UnLikes');

          data.addRows(${twitterLikes.size()});
         
          <c:forEach items="${twitterLikes}" var="list" varStatus="status">
  	        data.setValue(${status.index}, 0, '<fmt:formatDate pattern="dd.MMM" value="${list[1]}" />');
  	        data.setValue(${status.index}, 1, ${list[0]});
      	</c:forEach>
      	<c:forEach items="${twitterDislikes}" var="list" varStatus="status">
          	data.setValue(${status.index}, 2, ${list[0]});
  		</c:forEach>
  	
          var chart = new google.visualization.LineChart(document.getElementById('chart_div_twitter'));
          chart.draw(data, {width: 1024, height: 300, title: 'Twitter Topic Page Likes/Unlikes Trend'});
        }
        
    </script>
  
   <div id="chart_div_facebook"></div>
   <div id="chart_div_twitter"></div>

<%@ include file="/WEB-INF/views/footer.jsp" %>