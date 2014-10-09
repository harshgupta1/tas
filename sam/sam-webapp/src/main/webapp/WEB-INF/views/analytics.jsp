<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Year');
        data.addColumn('number', 'Likes');
        data.addColumn('number', 'UnLikes');

        data.addRows(${likeList.size()});
       
        <c:forEach items="${likeList}" var="list" varStatus="status">
	        data.setValue(${status.index}, 0, '<fmt:formatDate pattern="dd.MMM" value="${list[1]}" />');
	        data.setValue(${status.index}, 1, ${list[0]});
    	</c:forEach>
    	<c:forEach items="${dislikeList}" var="list" varStatus="status">
        	data.setValue(${status.index}, 2, ${list[0]});
		</c:forEach>
		
        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
        chart.draw(data, {width: 1024, height: 300, title: '${pageName}  Likes/Unlikes Trend'});
      }
    </script>
  
   <div id="chart_div"></div>

<%@ include file="/WEB-INF/views/footer.jsp" %>