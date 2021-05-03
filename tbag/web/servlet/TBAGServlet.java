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
	
	private boolean loggedIn = false;
	private int gameID = -1;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = (String) req.getSession().getAttribute("success");
		
		if (username != null) {
			loggedIn = true;
			gameID = 0; // get from username
			// generate new data with game ID
		} else if (!loggedIn) {
			req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
			return;
		}
		
		Game game = new Game();
		Player player = game.getPlayer();
		
		if (firstRun) {
			firstRun = !firstRun;
			player.setLastOutput(player.getLastOutput() + "\n");
		}
		
		req.setAttribute("story", player.getLastOutput());
		req.setAttribute("moves", "Moves: " + player.getMoves());
		req.setAttribute("room", String.format("Room %d/16 (%.2f%%)", player.getRoomID(), player.getRoomID() / 16.0 * 100));
		req.setAttribute("duration", 900);
		
		req.getRequestDispatcher("/_view/game.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		Game game = new Game();
		Player player = game.getPlayer();
		
		String text = req.getParameter("userInput");
		String story = req.getParameter("story");
		
		if (text.length() != 0) {
			pastInputs += text + "\n";
			story += " > " + text + "\n";
			
			game.runCommand(text);
			
			story += game.getOutput() + "\n";
			
		}
		
		if (story.length() >= 8000) {
			story = story.substring(story.length() - 8000, story.length());
		}
		
		player.setLastOutput(story);
		player.setMoves(player.getMoves() + 1);
		
		game.updateGameState(story, player.getMoves());
		
		req.setAttribute("story", story);
		req.setAttribute("pastInputs", pastInputs + "\n");
		req.setAttribute("duration", req.getParameter("duration"));
		req.setAttribute("moves", "Moves: " + player.getMoves());
		req.setAttribute("room", String.format("Room %d/16 (%.2f%%)", player.getRoomID(), player.getRoomID() / 16.0 * 100));

		req.getRequestDispatcher("/_view/game.jsp").forward(req, resp);
	}
}
