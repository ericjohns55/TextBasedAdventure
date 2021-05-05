package servlet;

import database.DatabaseProvider;
import database.DerbyDatabase;
import database.IDatabase;

public class LoginController {
	private IDatabase database;
	
	public LoginController() {
		DatabaseProvider.setInstance(new DerbyDatabase(0));
		database = DatabaseProvider.getInstance();	
	}
	
	public boolean attemptLogin(String username, String password) {
		return database.validateLogin(username, password);
	}
	
	public int createAccount(String username, String password) {
		database.addUser(username, password);
		
		System.out.println("CREATING ACCOUNT !---------------------------------------------------------");
		
		int gameID = getGameID(username, password);
		
		database.loadInitialData(gameID);
		
		return gameID;
	}
	
	public int getGameID(String username, String password) {
		return database.getGameID(username, password);
	}
}
