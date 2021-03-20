package cs320.windows98.tbag.game;

import cs320.windows98.tbag.actor.Player;
import cs320.windows98.tbag.input.Command;
import cs320.windows98.tbag.items.Item;
import cs320.windows98.tbag.room.Room;

import java.util.HashMap;

public class Game {
	private HashMap<Integer, Room> rooms;
	private int moves;
	private Player player;

	public Game() {
		this.moves = -1;
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

	public void createRooms() {
		Room room1 = new Room(
				"You enter into room with a table and a door in the westward direction.", 1);
		Room room2 = new Room("You enter into an empty room with a door to the east.", 2);

		room1.addExit("west", room2);
		room2.addExit("east", room1);

		Item keys = new Item("keys");
		keys.setWeight(0.1);
		keys.setDescription("You see a set of keys sitting on the table.");
		
		room1.addItem("keys", keys);

		rooms.put(1, room1);
		rooms.put(2, room2);
	}
	
	public void addRoom(Room room) {
		rooms.put(room.getRoomID(), room);
	}

	public Room getRoom(int ID) {
		return rooms.get(ID);
	}
	
	public int getMoves() {
		return moves;
	}

	public void updateGameState() { }
	
	public void saveData() { };
	
	public void loadData() { };
}