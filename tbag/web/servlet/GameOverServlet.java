package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GameOverServlet extends HttpServlet  {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/_view/gameOver.jsp").forward(req, resp);
	}
		
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// If home button is pressed, the go back to login screen
		if (req.getParameter("home") != null)  {	
			resp.sendRedirect(req.getContextPath() + "/login");
		} else {	
			req.getRequestDispatcher("/_view/gameOver.jsp").forward(req, resp);
		}
	}
}	