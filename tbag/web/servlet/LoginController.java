package servlet;

import database.DatabaseProvider;
import database.DerbyDatabase;
import database.IDatabase;

public class LoginController {
	private IDatabase database;
	
	public LoginController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		database = DatabaseProvider.getInstance();	
	}
	
	public boolean attemptLogin(String username, String password) {
		return database.validateLogin(username, password);
	}
	
	public void createAccount(String username, String password) {
		database.addUser(username, password);
	}
}
