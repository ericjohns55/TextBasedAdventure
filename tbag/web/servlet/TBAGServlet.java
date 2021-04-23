package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import actor.Player;
import game.Game;

public class TBAGServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean firstRun = true;
	private String pastInputs = "";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		Game game = new Game();
		Player player = game.getPlayer();
		
		if (firstRun) {
			firstRun = !firstRun;
			player.setLastOutput(player.getLastOutput() + "\n");
		}
		
		req.setAttribute("story", player.getLastOutput());
		req.setAttribute("moves", "Moves: " + player.getMoves());
		req.setAttribute("score", "Score: 0");
		req.setAttribute("timeText", "Time Left: ");
		req.setAttribute("duration", 900);
		
		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		Game game = new Game();
		Player player = game.getPlayer();
		
		if (req.getParameter("submit").equals("Clear Game")) {
			req.setAttribute("story", player.getLastOutput());
		} else {
			String text = req.getParameter("userInput");
			String story = req.getParameter("story");
			
			if (text.length() != 0) {
				pastInputs += text + "\n";
				story += " > " + text + "\n";
				
				game.runCommand(text);
				
				story += game.getOutput() + "\n";
				
			}
			
			player.setLastOutput(story);
			player.setMoves(player.getMoves() + 1);
			
			game.updateGameState(story, player.getMoves());
			
			req.setAttribute("story", story);
			req.setAttribute("pastInputs", pastInputs + "\n");
			req.setAttribute("duration", req.getParameter("duration"));
			
		}
		
		req.setAttribute("moves", "Moves: " + player.getMoves());
		req.setAttribute("score", "Score: 0");
		req.setAttribute("timeText", "Time Left: ");

		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
	}
}
