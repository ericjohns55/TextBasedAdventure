package game;

import items.Item;
import map.Room;
import object.Object;
import obstacles.Door;

import java.util.HashMap;

import actor.Player;
import input.Command;

public class Game {
	private HashMap<Integer, Room> rooms;
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

	public void createRooms() {
		Room room1 = new Room(
				"You enter into room with a table and a door in the westward direction.", 1);
		Room room2 = new Room("You enter into an empty room with a door to the east.", 2);

		room1.addExit("west", room2);
		
		room2.addExit("east", room1);
		
		Door door = new Door("Probably leads to another room...", "west", true, "key");
		door.setLocked(true);
		room1.addObstacle("door", door);

		Item key = new Item("key");
		key.setWeight(0.1);
		key.setDescription("This key seems to be able to unlock a door.");
		
		room1.addItem("key", key);
		
		Item knife = new Item("knife");
		knife.setWeight(1.0);
		knife.setDescription("A knife, good for cutting and stabbing. ");
		
		room2.addItem("knife", knife);
		
		Item note = new Item("note");
		note.setWeight(0.1);
		note.setDescription("this will be a clue to a puzzle");
		
		room2.addItem("note", note);
		
		Item vile = new Item("vile");
		vile.setWeight(1.2);
		vile.setDescription("A vile filled with someones blood");
		
		room1.addItem("vile", vile);
		
		Item record = new Item("record");
		record.setWeight(0.1);
		record.setDescription("A vinyl record that says play me.");
		
		room1.addItem("record", record);
		
		Item jar = new Item("jar of eyes");
		jar.setWeight(5.0);
		jar.setDescription("Each eye is staring at you.");
		
		room1.addItem("jar of eyes", jar);
		
		Item shovel = new Item("shovel");
		shovel.setWeight(6.5);
		shovel.setDescription("A dirty shovel, good for digging.");
		
		room1.addItem("shovel", shovel);
		
		Item ax = new Item("ax");
		ax.setWeight(6.0);
		ax.setDescription("An ax, good for chopping wood.");
		
		room1.addItem("ax", ax);
		
		Item doll = new Item("doll");
		doll.setWeight(4.0);
		doll.setDescription("A raggedy doll.");
		
		room1.addItem("doll", doll);
		
		Item mask = new Item("mask");
		mask.setWeight(1.1);
		mask.setDescription("A classic WW1 gas mask.");
		
		room1.addItem("mask", mask);

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
	
	public void resetMoves() {
		moves = 0;
	}

	public void updateGameState() { }
	
	public void saveData() { };
	
	public void loadData() { };
}