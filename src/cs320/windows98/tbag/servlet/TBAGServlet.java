package cs320.windows98.tbag.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TBAGServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("TBAG Servlet: doGet");
		
		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("TBAG Servlet: doPost");
		
		String text = req.getParameter("userInput");
		req.setAttribute("userInput", text);
		req.setAttribute("output", "You inputted: " + text);
		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
		
	}
}
