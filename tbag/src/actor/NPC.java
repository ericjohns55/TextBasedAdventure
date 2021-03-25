package actor;

import game.Game;

public class NPC extends Actor {
	String dialogue;
	
	public NPC(Game game, int roomID, String dialogue) {
		super(game, roomID);
		this.dialogue = dialogue;
	}
	
	public String talk() {
		return dialogue;
	}
}
