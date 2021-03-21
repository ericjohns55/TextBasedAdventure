package cs320.windows98.tbag.actor;

import cs320.windows98.tbag.game.Game;

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
