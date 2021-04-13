package actor;

import game.Game;
import items.Inventory;
import items.Item;
import map.Room;

public class Actor {
	private Inventory inventory;
	private Inventory equippedItems;
	private Game game;

	private int roomID;
	private int inventoryID;
	private int actorID;

	public Actor(Game game, int roomID) {
		inventory = new Inventory();
		equippedItems = new Inventory();
		this.roomID = roomID;
		this.game = game;
		this.inventoryID = 0;
	}
	
	public void equipItem(String identifier, Item toAdd) {
		if (toAdd.inInventory() && toAdd.isEquippable()) {
			inventory.removeItem(identifier);
			equippedItems.addItem(identifier, toAdd);
		}
	}
	
	public void unEquipItem(String identifier, Item toRemove) {
		if (toRemove.isEquipped() && inventory.canAddItem(toRemove)) {
			equippedItems.removeItem(identifier);
			inventory.addItem(identifier, toRemove);
		}
		else if (toRemove.isEquipped() && !inventory.canAddItem(toRemove)) {
			equippedItems.removeItem(identifier);
			game.getRoom(roomID).addItem(identifier, toRemove);
		}
	}
	
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
		return game.getRoom(getRoomID());
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
}
