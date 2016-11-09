<%@page import="com.crawler.cnn.CNNPost"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>Clinton Vs Trump</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="js/bootstrap.min.js"></script>

</head>
<body>
<script>
$(document).ready(function(){
    $('[data-toggle="popover"]').popover({
    	container: 'body',
    	placement: 'bottom'
    });   
});
</script>
<%@ page import="com.crawler.cnn.CNNPost"
		 import ="java.util.ArrayList"
 %>
<div class="container-fluid">
  <h1>Hello World!</h1>
  <p>Resize the browser window to see the effect.</p>
  <div class="row" style="background-color:lavender;">
    <div class="col-sm-5 col-md-6" style="background-color:lavenderblush;">Hilary Clinton
    	<div class = "list-group">
    		<% 
    			ArrayList<CNNPost> clintonPost = (ArrayList<CNNPost>)request.getAttribute("clintonPosts");
    			int size = clintonPost.size() > 25 ? 25 : clintonPost.size();
    			for(int i = 0 ; i < size ; i ++ ){
    				
    		%>
    		  <form name=form method=post action="${pageContext.request.contextPath}/Welcome" TARGET="_blank">
    		<a href= "javascript:{openNewWindow();};" onclick="parentNode.submit();" class=list-group-item list-group-item-action" data-toggle="popover" data-trigger="hover" data-content=
    		"<% out.print(clintonPost.get(i).getBody().get(0)); %>"
    		>
    		<% out.println(clintonPost.get(i).getTitle()); %> 
    		</a>
    		<input type=hidden value=<%out.print(i);%>  name="clintonPostId"></form>
    		<% }%>
    	
    	</div>
    
    
    </div>
    <div class="col-sm-5 col-sm-offset-2 col-md-6 col-md-offset-0" style="background-color:lightcyan;">Donald Trump    
    <div class = "list-group">
    		<div class = "list-group">
    		<% 
    			ArrayList<CNNPost> trumpPost = (ArrayList<CNNPost>)request.getAttribute("trumpPosts");
    			int tsize = trumpPost.size() > 25 ? 25 : trumpPost.size();
    			for(int i = 0 ; i < tsize ; i ++ ){
    		%>
    		<form name=form method=post action="${pageContext.request.contextPath}/Welcome"  TARGET="_blank">
    		<a href="javascript:;" onclick="parentNode.submit();"  class="list-group-item list-group-item-action" data-toggle="popover" data-trigger="hover" data-content=
    		"<% out.print(trumpPost.get(i).getBody().get(0)); %>">
    		<% out.println(trumpPost.get(i).getTitle()); %> 
    		</a>
    		<input type=hidden value=<%out.print(i);%>  name="trumpPostId"></form>
    		<% }%>
    	
    	</div>
    	
    	</div>
    
    </div>
  </div>
</div>
    
</body>
</html>
