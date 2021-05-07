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
		String success = (String) req.getSession().getAttribute("success");
		
		if (success != null) {
//			loggedIn = true;
			gameID = Integer.parseInt(success); 			
			System.out.println("--- GRABBED LOG IN INFO --- GAME ID: " + success);
		} else {
			req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
			return;
		}
		
		Game game = new Game(gameID);
		Player player = game.getPlayer();
		
		if (firstRun) {
			firstRun = !firstRun;
			player.setLastOutput(player.getLastOutput() + "\n");
		}
		
		req.setAttribute("story", player.getLastOutput());
		req.setAttribute("moves", "Moves: " + player.getMoves());
		req.setAttribute("duration", 900);
		
		// Need to change this when adding more rooms
		String roomProgress = String.format("Room: %d/17 (%.2f%%)", player.getRoomID(), (player.getRoomID() - 1) / 17.0 * 100);
		
		if (player.getRoomID() == 18) {
			roomProgress = "YOU ESCAPED!";
			
		}
		
		req.setAttribute("room", roomProgress);
		
		req.getRequestDispatcher("/_view/game.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		Game game = new Game(gameID);
		Player player = game.getPlayer();
		
		String text = req.getParameter("userInput");
		String story = req.getParameter("story");
		
		System.out.println("========== REQ STATUS: " + (String) req.getSession().getAttribute("success"));
		
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
		
		// Need to change this when adding more rooms
		String roomProgress = String.format("Room: %d/17 (%.2f%%)", player.getRoomID(), (player.getRoomID() - 1) / 17.0 * 100);
		
		if (player.getRoomID() == 18) {
			//roomProgress = "YOU ESCAPED!";
			resp.sendRedirect(req.getContextPath() + "/gameOver");
		}
		
		req.setAttribute("room", roomProgress);

		req.getRequestDispatcher("/_view/game.jsp").forward(req, resp);
	}
}
