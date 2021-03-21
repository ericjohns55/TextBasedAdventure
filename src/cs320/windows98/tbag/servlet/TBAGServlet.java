package cs320.windows98.tbag.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs320.windows98.tbag.game.Game;



public class TBAGServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 Game game = new Game();
	 boolean firstRun = true;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		if (firstRun) {
			firstRun = !firstRun;
			
			req.setAttribute("story", game.getPlayer().getRoom().getDescription());
		}
		
		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		if (req.getParameter("submit").equals("Clear Game")) {
			req.setAttribute("story", "");
		} else {
			String text = req.getParameter("userInput");
			
			String story = req.getParameter("story");
			
			story += game.runCommand(text);
			
			req.setAttribute("story", story + "\n");
		}

		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
		
	}
}
