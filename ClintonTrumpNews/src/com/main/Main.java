package com.main;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crawler.cnn.CNNPost;
import com.crawler.cnn.Controller;
import com.topicmodel.TopicModel;

/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
	
	public ArrayList<CNNPost> clintonPosts = new ArrayList<CNNPost>();
	public ArrayList<CNNPost> trumpPosts = new ArrayList<CNNPost>();
	
	TopicModel tmodel;
	Controller cl;
	// Initializes the important resources
	

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		System.out.println("GOT");
		//String path = config.getServletContext().getRealPath("js");
		//path = path.substring(0, path.indexOf("js")-1);
		//Properties.setRoot(path);
		//Properties.updatePath();
		try {
			
			
			cl = new Controller();
			cl.startCrawler();
			cl.selectPosts();
			clintonPosts = cl.getClintonPosts();
			trumpPosts = cl.getTrumpPosts();
			tmodel = new TopicModel(cl.posts);
			tmodel.generateTopicModel(false);

			System.out.println("Successfully FInish");

		} catch (Exception e) {
			e.printStackTrace();
		}

		//super.init(config);
	}

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Main() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setAttribute("clintonPosts", clintonPosts);
		request.setAttribute("trumpPosts",trumpPosts);
		String yourJSP = "/jsp/index.jsp";

		System.out.println(clintonPosts.size() +" " + trumpPosts.size());
		
        RequestDispatcher rd = getServletContext().getRequestDispatcher(yourJSP);
        rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("clintonPostId") != null){
			System.out.println("Clinton " + request.getParameter("clintonPostId"));
			request.setAttribute("clintonPostId", request.getParameter("clintonPostId"));
		}
		if(request.getParameter("trumpPostId") != null){
			System.out.println("Trump " + request.getParameter("trumpPostId"));
			request.setAttribute("trumpPostId", request.getParameter("trumpPostId"));
		}
		String yourJSP = "/jsp/viewPost.jsp";
		request.setAttribute("clintonPosts", clintonPosts);
		request.setAttribute("trumpPosts",trumpPosts);
		request.setAttribute("posts",cl.posts);
		request.setAttribute("trumpToPost", cl.trumpToPost);
		request.setAttribute("clintonToPost", cl.clintonToPost);
		request.setAttribute("topicModel", tmodel);
        RequestDispatcher rd = getServletContext().getRequestDispatcher(yourJSP);
        rd.forward(request, response);
	}

	

}
