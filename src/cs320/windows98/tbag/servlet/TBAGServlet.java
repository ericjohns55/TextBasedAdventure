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
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("TBAG Servlet: doGet");
		
		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("TBAG Servlet: doPost");
		
		if (req.getParameter("submit").equals("Clear Game")) {
			req.setAttribute("story", "");
		} else {
			String text = req.getParameter("userInput");
			
			String story = req.getParameter("story");
			
			story += game.runCommand(text);
			
			//String output = game.runCommand(text);
			//story += game.runCommand(text);
			
//			Input userInput = new Input(text);
//			Command command = new Command(userInput);
//			
//			if (command.validate()) {
//				command.execute();
//				
//				String output = command.getOutput();
//				
//				story += output + "\n";
//			} else {
//				story += "=== INVALID COMMAND ===\n";
//				story += "You inputted: " + text + "\n";
//				story += "Your Action: " + userInput.getAction() + "\n";
//				story += "Your Subject: " + userInput.getSubject() + "\n";
//			}
			
			
			req.setAttribute("userInput", text);
			req.setAttribute("story", story + "\n");
		}
		

		req.getRequestDispatcher("/_view/tbag.jsp").forward(req, resp);
		
	}
}
