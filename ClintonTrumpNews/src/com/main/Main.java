package com.main;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crawler.cnn.CNNPost;
import com.crawler.cnn.Controller;

/**
 * Servlet implementation class Main
 */
@WebServlet("/Main")
public class Main extends HttpServlet {
	
	public ArrayList<CNNPost> clintonPosts = new ArrayList<CNNPost>();
	public ArrayList<CNNPost> trumpPosts = new ArrayList<CNNPost>();
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		//super.init();
		try{
			Controller cl = new Controller();
			cl.startCrawler();
			cl.selectPosts();
			clintonPosts = cl.getClintonPosts();
			trumpPosts = cl.getTrumpPosts();			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
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
		// TODO Auto-generated method stub
		/*response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print("<html><body>");
		out.print("<h3>Hello Servlet</h3>");
		out.print("</body></html>");
		*/
		request.setAttribute("clintonPosts", clintonPosts);
		request.setAttribute("trumpPosts",trumpPosts);
		String yourJSP = "/jsp/index.jsp";

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
	
        RequestDispatcher rd = getServletContext().getRequestDispatcher(yourJSP);
        rd.forward(request, response);
	}

	

}
