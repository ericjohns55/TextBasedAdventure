package actor;

import game.Game;

public class Player extends Actor {
	private String lastOutput;
	private int moves;
	
	public Player(Game game, int roomID) {
		super(game, roomID);
		
		this.lastOutput = "Type in \"look\" to start.";
		this.moves = 0;
	}
	
	public Player(int roomID) {
		super(roomID);
	}

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
