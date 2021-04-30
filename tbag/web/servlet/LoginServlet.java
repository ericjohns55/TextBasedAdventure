package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LoginController controller = null;
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
		String username = req.getParameter("username");
		String password = req.getParameter("username");
		String errorMessage = null;
		
		boolean validLogin = false;
		
		if (username == null || password == null || username.equals("") || password.equals("")) {
			errorMessage = "Please input both a username and password.";
		} else {
			controller = new LoginController();
			
			if (req.getParameter("submit") != null) {
				validLogin = controller.attemptLogin(username, password);
				
				if (!validLogin) {
					errorMessage = "You entered an invalid username or password!";
				}
			} else if (req.getParameter("create") != null) {
				controller.createAccount(username, password);
				validLogin = true;
			}
		}

		req.setAttribute("username", req.getParameter("username"));
		req.setAttribute("password", req.getParameter("password"));
		req.setAttribute("errorMessage", errorMessage);
		
		if (validLogin) {
			req.getSession().setAttribute("success", username);
			resp.sendRedirect(req.getContextPath() + "/game");
		} else {
			req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
		}		
	}
}
