package actor;

import java.util.TreeMap;

import dialogue.Node;
import game.Game;

public class NPC extends Actor {
	private String name;
	private String description;
	private TreeMap<Integer, Node> dialogue;
	
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
	
	public void addNode(Node n) {
        dialogue.put(n.getNodeID(), n);
    }
    
    public void removeNode(Node n) {
    	dialogue.remove(n.getNodeID(), n);
    }

	public TreeMap<Integer, Node> getDialogue() {
		return dialogue;
	}
	
}


