<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Clinton Vs Trump</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
<title>Insert title here</title>
</head>
<body>
<%@ page import="com.crawler.cnn.CNNPost"
		 import ="java.util.ArrayList"
		 import = "java.util.Map"
		 import ="com.topicmodel.TopicModel"
 %>
 
 <%
    	int choice = 0;
    	int id = 0;
    	//out.println(id +" " + choice);
    	ArrayList<CNNPost> post = null;

    	//out.println("Hello " + request.getAttribute("trumpPostId"));

    	if (request.getAttribute("clintonPostId") != null) {
    		choice = 1;
    		post = (ArrayList<CNNPost>) request.getAttribute("clintonPosts");
    		id = Integer.parseInt((String) request.getAttribute("clintonPostId"));
    	} else if (request.getAttribute("trumpPostId") != null) {
    		choice = 2;
    		post = (ArrayList<CNNPost>) request.getAttribute("trumpPosts");
    		id = Integer.parseInt((String) request.getAttribute("trumpPostId"));
    	}

    	//out.println(id +" " + choice);
    %>
 
<div class="container-fluid">
  <div class="text-left" style="font-size:24px"> <p class="text-primary"><%out.print(post.get(id).getTitle());%></p> </div>
   <div class="text-left" style="font-family: Monospace ; font-size:14px"> <p class="text-muted"><%out.print("Posted by <b>"+post.get(id).getAuthorName()+"</b> on " + post.get(id).getDate() ); %> </p> </div>
    <div class="row">
    <div class="col-sm-8" style="background-color:lavenderblush;">
    <% for (String s : post.get(id).getBody()){ %>
                 <p class="text-justify"><% out.println(s);%></p>
    <%}%>
    
    
    </div>
    <div class="col-sm-4" style="background-color:lavender;">.col-sm-8</div>
    <div class = "list-group"> 
    	<% 
    		int pid = 0;
    		if(choice == 1){
    		Map<Integer,Integer> clintonToPost =(Map<Integer,Integer>)request.getAttribute("clintonToPost");
    		pid = clintonToPost.get(id);
    		}else if (choice == 2){
    			Map<Integer,Integer> trumpToPost =(Map<Integer,Integer>)request.getAttribute("trumpToPost");
        		pid = trumpToPost.get(id);
    		}
    		TopicModel tmodel = (TopicModel)request.getAttribute("topicModel");
    		ArrayList<CNNPost> posts = (ArrayList<CNNPost>)request.getAttribute("posts");
    		for(int t = 0 ; t < 5 ; t ++){
    	%>
    		<div>
    			<p class = "bg-primary">Topic<%out.print(t);%> : </p>
    			
    		 </div>
    	<%}%>
    	 
    	<div> Hello2 </div>
    </div>
  </div>
</div>
	 
</body>
</html>