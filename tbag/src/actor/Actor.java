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

	public Actor(Game game, int roomID) {
		inventory = new Inventory();
		equippedItems = new Inventory();
		this.roomID = roomID;
		this.game = game;
	}
	
	public void grabItem(String identifier, Item toAdd) {
		inventory.addItem(identifier, toAdd);
	}
	
	public boolean dropItem(String identifier) {
		if (inventory.contains(identifier)) {
			Item drop = inventory.removeItem(identifier);
			
			if (drop != null) {
				getRoom().addItem(identifier, drop);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
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
			Actor.this.dropItem(identifier);
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
}
