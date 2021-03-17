package cs320.windows98.tbag.player;

import cs320.windows98.tbag.inventory.Inventory;
import cs320.windows98.tbag.room.Room;

public class Player {
	private Inventory inventory;
	private Rooom room; 
	
	public Player() {
		inventory = new Inventory();
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public Room getRoom() {
		return room;
	}
}
