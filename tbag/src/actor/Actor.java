package actor;

import game.Game;
import items.Inventory;
import map.Room;

public class Actor {
	private Inventory inventory;
	private Inventory equippedItems;
	private Game game;

	private int roomID;
	private int inventoryID;
	private int actorID;
	private int gameID;

	public Actor(Game game, int roomID) {
		inventory = new Inventory();
		equippedItems = new Inventory();
		this.roomID = roomID;
		this.game = game;
	}
	
	// instantiates game to null: can be filled in later
	public Actor(int roomID) {
		this(null, roomID);
	}
	
	// mostly getters and setters to store state of the actor
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public Inventory getEquippedItems() {
		return equippedItems;
	}
	
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public int getRoomID()
	{
		return roomID;
	}

	public Room getRoom() {
		return game.getRoom();
	}

	public int getInventoryID() {
		return inventoryID;
	}

	public void setInventoryID(int inventoryID) {
		this.inventoryID = inventoryID;
	}

	public int getActorID() {
		return actorID;
	}

	public void setActorID(int actorID) {
		this.actorID = actorID;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
	
	public int getGameID() {
		return gameID;
	}
}
