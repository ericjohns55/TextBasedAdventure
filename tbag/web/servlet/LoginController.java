package servlet;

import database.DatabaseProvider;
import database.DerbyDatabase;
import database.IDatabase;

public class LoginController {
	private IDatabase database;
	
	public LoginController() {
		DatabaseProvider.setInstance(new DerbyDatabase(0));	// game ID in parameter does not really matter here
		database = DatabaseProvider.getInstance();	
	}
	
	public boolean attemptLogin(String username, String password) {
		return database.validateLogin(username, password);
	}
	
	public int createAccount(String username, String password) {
		database.addUser(username, password);	// add user to database
		
		int gameID = getGameID(username, password);	// grab new game ID	
		
		database.loadInitialData(gameID);	// load data for game ID
		
		return gameID;
	}
	
	public int getGameID(String username, String password) {
		return database.getGameID(username, password);	// grab game ID from database
	}
}
