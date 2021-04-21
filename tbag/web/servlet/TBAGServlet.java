package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import game.Game;

public class TBAGServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean firstRun = true;
	private String pastInputs = "";
	private int moves = 0;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {			
		if (firstRun) {
			firstRun = !firstRun;
			
			req.setAttribute("story", "Start game?");
			req.setAttribute("moves", "Moves: 0");
			req.setAttribute("score", "Score: 0");
			req.setAttribute("timeText", "Time Left: ");
			req.setAttribute("duration", 900);
		}
		
		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		Game game = new Game();
		
		if (req.getParameter("submit").equals("Clear Game")) {
			req.setAttribute("story", game.getPlayer().getRoom().getDescription());
			moves = 0;
		} else {
			String text = req.getParameter("userInput");
			String story = req.getParameter("story");
			
			if (text.length() != 0) {
				pastInputs += text + "\n";
				story += " > " + text + "\n";
				
				game.runCommand(text);
				
				story += game.getOutput();
				
			}
			
			req.setAttribute("story", story + "\n");
			req.setAttribute("pastInputs", pastInputs + "\n");
			req.setAttribute("duration", req.getParameter("duration"));
		}
		
		req.setAttribute("moves", "Moves: " + ++moves);
		req.setAttribute("score", "Score: 0");
		req.setAttribute("timeText", "Time Left: ");

		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
		
	}
}
