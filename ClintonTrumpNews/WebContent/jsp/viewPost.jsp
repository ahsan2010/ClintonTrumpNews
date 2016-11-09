<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ page import="com.crawler.cnn.CNNPost"
		 import ="java.util.ArrayList"
 %>
	<div class="container">
            <div class="row" style="background-color:cornsilk;">
                <div class="col-xs-12" style="background-color:beige;"> <% 
                int choice = 0;
                int id = 0;
                //out.println(id +" " + choice);
                ArrayList<CNNPost> post = null;
                
                //out.println("Hello " + request.getAttribute("trumpPostId"));
                
                if(request.getAttribute("clintonPostId") != null ){
                	choice = 1;
                	post = (ArrayList<CNNPost>)request.getAttribute("clintonPosts"); 
                	id = Integer.parseInt((String)request.getAttribute("clintonPostId"));
                }else if (request.getAttribute("trumpPostId") != null){
                	choice = 2;
                	post = (ArrayList<CNNPost>)request.getAttribute("trumpPosts"); 
                	id = Integer.parseInt((String)request.getAttribute("trumpPostId"));
                }
                
                //out.println(id +" " + choice);
                
                %>
                 <p class="text-center"><% out.println(post.get(id).getTitle());%></p>
                </div>
                 
            </div>
           
</div>  
</body>
</html>