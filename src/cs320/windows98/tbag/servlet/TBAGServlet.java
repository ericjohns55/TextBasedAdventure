package cs320.windows98.tbag.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs320.windows98.tbag.game.Game;



public class TBAGServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Game game = new Game();
	private boolean firstRun = true;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		if (firstRun) {
			firstRun = !firstRun;
			
			req.setAttribute("story", game.getPlayer().getRoom().getDescription());
			req.setAttribute("moves", "Moves: 0");
			req.setAttribute("score", "Score: 0");
			req.setAttribute("time", "Time Left: 15:00");
		}
		
		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		if (req.getParameter("submit").equals("Clear Game")) {
			req.setAttribute("story", game.getPlayer().getRoom().getDescription());
			
			game.resetMoves();
		} else {
			String text = req.getParameter("userInput");
			
			if (text.length() != 0) {
				String story = req.getParameter("story");
				
				story += game.runCommand(text);
				
				req.setAttribute("story", story + "\n");
			}
		}
		
		req.setAttribute("moves", "Moves: " + game.getMoves());
		req.setAttribute("score", "Score: 0");
		req.setAttribute("time", "Time Left: 11:52");

		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
		
	}
}
