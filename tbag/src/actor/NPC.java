package actor;

import dialogue.Node;
import game.Game;
import items.Item;
import map.RoomObject;

public class NPC extends Actor {
	private String name;
	private String description;
	private Node currentNode;
	private Node previousNode;
	private Node rootNode;
	private boolean talkedTo;
	private boolean canTalkTo;
	private boolean done;
	private RoomObject unlockObstacle;
	private Item requiredItem;
	
	
	public NPC(Game game, int roomID, String name, String description, Item requiredItem, RoomObject unlockObstacle) {
		super(game, roomID);
		this.name = name;
		this.description = description;
		talkedTo = false;
		done = false;
		this.unlockObstacle = unlockObstacle;
		this.requiredItem = requiredItem;
		canTalkTo = true;
		
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
	
	public Node getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(Node previousNode) {
		this.previousNode = previousNode;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public RoomObject getUnlockObstacle() {
		return unlockObstacle;
	}

	public void setUnlockObstacle(RoomObject unlockObstacle) {
		this.unlockObstacle = unlockObstacle;
	}

	public Item getRequiredItem() {
		return requiredItem;
	}

	public void setRequiredItem(Item requiredItem) {
		this.requiredItem = requiredItem;
	}
	
	public boolean isSolved() {
		return this.getInventory().contains(requiredItem);
	}

	public boolean CanTalkTo() {
		return canTalkTo;
	}

	public void setCanTalkTo(boolean canTalkTo) {
		this.canTalkTo = canTalkTo;
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}
}


