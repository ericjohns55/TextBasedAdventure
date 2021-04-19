package actor;

import dialogue.Node;
import game.Game;

public class NPC extends Actor {
	private String name;
	private String description;
	private Node currentNode;
	private boolean talkedTo;
	private boolean done;
	
	public NPC(Game game, int roomID, String name, String description) {
		super(game, roomID);
		this.name = name;
		this.description = description;
		talkedTo = false;
		done = false;
		
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

	public boolean isTalkedTo() {
		return talkedTo;
	}

	public void setTalkedTo(boolean talkedTo) {
		this.talkedTo = talkedTo;
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
	
}


