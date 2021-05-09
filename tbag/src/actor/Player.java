package actor;

import game.Game;

public class Player extends Actor {
	private String lastOutput;
	private int moves;
	
	// constructor with Game that can be filled in later
	public Player(Game game, int roomID) {
		super(game, roomID);
		
		this.lastOutput = "Type in \"look\" to start.";
		this.moves = 0;
	}
	
	public Player(int roomID) {
		super(roomID);
	}
	
	// these values are stored in the csvs for persistence in logins

	public String getLastOutput() {
		return lastOutput;
	}

	public void setLastOutput(String lastOutput) {
		this.lastOutput = lastOutput;
	}

	public int getMoves() {
		return moves;
	}

	public void setMoves(int moves) {
		this.moves = moves;
	}
}
