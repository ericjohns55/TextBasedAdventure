package actor;

import dialogue.Dialogue;
import game.Game;

public class NPC extends Actor {
	private String name;
	private String description;
	private Dialogue dialogue;
	
	public NPC(Game game, int roomID, String name, String description) {
		super(game, roomID);
		this.name = name;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Dialogue getDialogue() {
		return dialogue;
	}

	public void setDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
	}
}


