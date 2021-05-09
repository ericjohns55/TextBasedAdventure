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
		String username = req.getParameter("username");	// grab username and password field
		String password = req.getParameter("password");
		String errorMessage = null;
		
		boolean validLogin = false;
		
		int gameID = 0;
		
		if (username == null || password == null || username.equals("") || password.equals("")) {
			errorMessage = "Please input both a username and password.";	// yell at invalid data
		} else {
			controller = new LoginController();
			
			if (req.getParameter("login") != null) {
				validLogin = controller.attemptLogin(username, password);	// check that login exists
				
				if (!validLogin) {	// they messed up: call them stupid
					errorMessage = "You entered an invalid username or password!";
				} else {
					gameID = controller.getGameID(username, password);	// grab game ID
				}
			} else if (req.getParameter("create") != null) {
				if (controller.attemptLogin(username, password)) {	// make sure account does not already exist
					errorMessage = "This account already exists.";
				} else {
					gameID = controller.createAccount(username, password);	// create new account and grab gameID
									
					validLogin = true;
				}
			}
		}

		req.setAttribute("username", req.getParameter("username"));
		req.setAttribute("password", req.getParameter("password"));
		req.setAttribute("errorMessage", errorMessage);
		
		if (validLogin) {	// check valid login
			req.getSession().setAttribute("success", "" + gameID);	// store game ID in page, to be grabbed in the game servlet
			resp.sendRedirect(req.getContextPath() + "/game");
		} else {
			req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);	// they were not valid, refresh page
		}		
	}
}
