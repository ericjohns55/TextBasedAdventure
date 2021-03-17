package cs320.windows98.tbag.player;

import cs320.windows98.tbag.inventory.Inventory;
import cs320.windows98.tbag.room.Room;

public class Player {
	private Inventory inventory;

	private Room room; 
	private int roomID;

	
	public Player() {
		inventory = new Inventory();
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public int getRoomID()
	{
		return roomID;
	}


	public Room getRoom() {
		return room;
	}
}
