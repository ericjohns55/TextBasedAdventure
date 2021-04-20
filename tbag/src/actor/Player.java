package actor;

import game.Game;

public class Player extends Actor {
	public Player(Game game, int roomID) {
		super(game, roomID);
	}
	
	public Player(int roomID) {
		super(roomID);
	}
}
