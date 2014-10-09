
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'memory.jsp' starting page</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
  </head>
  
  <body>
    <%
    // Get current size of heap in bytes 
    long heapSize = Runtime.getRuntime().totalMemory(); 
    out.println("heapSize= " + heapSize);
    // Get maximum size of heap in bytes. The heap cannot grow beyond this size. 
    // Any attempt will result in an OutOfMemoryException. 
    long heapMaxSize = Runtime.getRuntime().maxMemory(); 
    out.println("heapMaxSize= " + heapMaxSize);
    // Get amount of free memory within the heap in bytes. This size will increase 
    // after garbage collection and decrease as new objects are created.
     long heapFreeSize = Runtime.getRuntime().freeMemory(); 
     out.println("heapFreeSize= " + heapFreeSize);
    %>
    
  </body>
</html>
