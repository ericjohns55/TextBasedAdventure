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
	
//	private boolean loggedIn = false;
	private int gameID = -1;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String success = (String) req.getSession().getAttribute("success");	// grab game ID from login page
		
		if (success != null) {
			gameID = Integer.parseInt(success); 	// grab game ID		
		} else {
			req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);	// push them back to login because it is not there
			return;
		}
		
		Game game = new Game(gameID);	// create a new game class from game ID
		Player player = game.getPlayer();
		
		if (firstRun) {
			firstRun = !firstRun;
			player.setLastOutput(player.getLastOutput() + "\n");	// on first run set the game output to what was stored in the player's user
		}
		
		req.setAttribute("story", player.getLastOutput());
		req.setAttribute("moves", "Moves: " + player.getMoves());	// update the moves
		req.setAttribute("duration", 900);
		
		// Need to change this when adding more rooms
		String roomProgress = String.format("Room: %d/18 (%.2f%%)", player.getRoomID(), (player.getRoomID() - 1) / 18.0 * 100);
		
		if (player.getRoomID() == 19) {
			//roomProgress = "YOU ESCAPED!";
			resp.sendRedirect(req.getContextPath() + "/gameOver");
      return;
		}
		
		req.setAttribute("room", roomProgress);
		
		req.getRequestDispatcher("/_view/game.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		Game game = new Game(gameID);	// create new game
		Player player = game.getPlayer();
		
		String text = req.getParameter("userInput");
		String story = req.getParameter("story");
		
		if (text.length() != 0) {	// print out command as it is inputted
			pastInputs += text + "\n";
			story += "\n > " + text + "\n";
			
			game.runCommand(text);
			
			story += game.getOutput() + "\n";	// append new output
			
		}
		
		if (story.length() >= 8000) {
			story = story.substring(story.length() - 8000, story.length());	// crop output once it exceeds 8000 (db max)
		}
		
		player.setLastOutput(story);
		player.setMoves(player.getMoves() + 1);	
		
		game.updateGameState(story, player.getMoves());	// update players table with moves and output
		
		req.setAttribute("story", story);	// make sure all fields carry through post method
		req.setAttribute("pastInputs", pastInputs + "\n");
		req.setAttribute("duration", req.getParameter("duration"));
		req.setAttribute("moves", "Moves: " + player.getMoves());
		
		// fun room progress screen
		String roomProgress = String.format("Room: %d/18 (%.2f%%)", player.getRoomID(), (player.getRoomID() - 1) / 18.0 * 100);
		
		if (player.getRoomID() == 19) {
			//roomProgress = "YOU ESCAPED!";
			resp.sendRedirect(req.getContextPath() + "/gameOver");
		}
		
		req.setAttribute("room", roomProgress);	// set room progress to be visible

		req.getRequestDispatcher("/_view/game.jsp").forward(req, resp);
	}
}
