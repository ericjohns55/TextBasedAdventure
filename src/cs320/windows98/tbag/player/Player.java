package cs320.windows98.tbag.player;

import cs320.windows98.tbag.game.Game;
import cs320.windows98.tbag.inventory.Inventory;
import cs320.windows98.tbag.items.Item;
import cs320.windows98.tbag.room.Room;

public class Player {
	private Inventory inventory;
	private Game game;

	private int roomID;

	public Player(Game game, int roomID) {
		inventory = new Inventory();
		
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
	
	public Inventory getInventory() {
		return inventory;
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
