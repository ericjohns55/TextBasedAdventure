package cs320.windows98.tbag.game;

import cs320.windows98.tbag.input.Command;
import cs320.windows98.tbag.items.Item;
import cs320.windows98.tbag.player.Player;
import cs320.windows98.tbag.room.Room;

import java.util.HashMap;

public class Game {
	private HashMap<Integer, Room> rooms;
	private Room room;
	private int moves; 
	private Player player;
	
	
	public Game() {
		this.moves = 0;
		this.player = new Player(this, 1);
		this.rooms = new HashMap<Integer, Room>();
		
		createRooms();
	}
	
	public Player getPlayer() {
		return player;
	}

	public String runCommand(String input) {
		Command command = new Command(input, this);
		moves++;
		return command.execute();
	}
	// public void play()
	
	
	public void createRooms()
	{
		Room room1 = new Room("You enter into room with a table holding a set of keys and a door in the westward direction.", 1);
		Room room2 = new Room("You enter into an empty room with a door to the east.", 2);
		
		room1.setExit("west", room2);
		room2.setExit("east", room1);
		
		Item keys = new Item("keys");
		room1.addItem("keys", keys);

		rooms.put(1, room1);
		rooms.put(2, room2);
	}
	
	public Room getRoom(int ID) {
		return rooms.get(ID);
	}
	
	
	public void update()
	{
		
		
		
	}
	
}